package org.openflexo.ta.opcua.utils;

import com.google.errorprone.annotations.Var;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.openflexo.ta.opcua.model.*;
import org.openflexo.ta.opcua.model.nodes.OPCInstanceNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

public class OPCDiscovery {

    private final OpcUaClient connection;
    private final OPCServer model;
    private final OPCModelFactory factory;
    private final Map<Integer, OPCNamespace> namespaceMap;
    private final List<OPCInstanceNode> nodeStack;

    private OPCDiscovery(OpcUaClient aConnection, OPCServer aModel, OPCModelFactory aFactory) {
        this.connection = aConnection;
        this.model = aModel;
        this.factory = aFactory;
        this.namespaceMap = new HashMap<>();
        this.nodeStack = new ArrayList<>();
    }

    public static OPCServer discover(OpcUaClient aConnection, OPCModelFactory aFactory) {
        OPCServer model = aFactory.makeOPCServer();
        OPCDiscovery discovery = new OPCDiscovery(aConnection, model, aFactory);
        discovery.initialize();
        discovery.browseNode(Identifiers.ObjectsFolder);
        return model;
    }

    private void initialize() {
        String applicationUri = connection.getConfig().getEndpoint().getServer().getApplicationUri();
        model.setUri(applicationUri);
        String[] namespaceUriArray = connection.getNamespaceTable().toArray();
        for (int index = 0; index < namespaceUriArray.length; index++) {
            OPCNamespace namespace = getFactory().makeOPCNamespace(model, namespaceUriArray[index], index);
            namespaceMap.put(index, namespace);
        }
    }

    private OPCModelFactory getFactory() {
        return factory;
    }

    private OPCNamespace getNamespace(ExpandedNodeId aNodeId) {
        final int index = aNodeId.getNamespaceIndex().intValue();
        OPCNamespace returned = namespaceMap.get(index);
        if (returned != null) return returned;
        String uri = aNodeId.getNamespaceUri();
        // TODO : check if ok as a default uri.
        if (uri == null) uri = "urn:namespace:" + index + "/";
        returned = getFactory().makeOPCNamespace(model, uri, index);
        namespaceMap.put(index, returned);
        return returned;
    }

    private static BrowseDescription makeBrowseDescription(NodeId aNodeId) {
        return new BrowseDescription(
                aNodeId,
                BrowseDirection.Forward,
                Identifiers.References,
                true,
                uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
                uint(BrowseResultMask.All.getValue())
        );
    }

    private OPCInstanceNode getCurrentParent() {
        if (nodeStack.isEmpty()) return null;
        return nodeStack.get(nodeStack.size() - 1);
    }

    private void enterNode(OPCInstanceNode aNode) {
        nodeStack.add(aNode);
    }

    private void exitNode() {
        nodeStack.remove(nodeStack.size() - 1);
    }

    private void browseNode(NodeId aNodeId) {
        BrowseDescription browse = makeBrowseDescription(aNodeId);
        try {
            BrowseResult browseResult = connection.browse(browse).get();
            List<ReferenceDescription> references = toList(browseResult.getReferences());
            for (ReferenceDescription ref : references) {
                final Node node = connection.getAddressSpace().getNode(aNodeId);
                final String identifier = ref.getNodeId().getIdentifier().toString();
                final String name = ref.getBrowseName().getName();
                final int nodeClass = ref.getNodeClass().getValue();
                final OPCNamespace namespace = getNamespace(ref.getNodeId());
                final OPCInstanceNode parent = getCurrentParent();
                String indent = "";
                for (int i = 0; i < nodeStack.size(); i++) indent += "  ";
                switch (ref.getNodeClass().getValue()) {
                    case 1: {
                        // Node is an object
                        OPCObjectNode objectNode = getFactory().makeOPCObjectNode(namespace, parent, identifier, name);
                        System.out.println(indent + "Added object node " + objectNode.getQualifiedName());
                        enterNode(objectNode);
                        ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
                        exitNode();
                        break;
                    }
                    case 2: {
                        // Node is a variable
                        if (node instanceof VariableNode) {
                            VariableNode miloNode = (VariableNode) node;
                            //System.out.println(miloNode.getDataType());
                        }
                        OPCVariableNode variableNode = getFactory().makeOPCVariableNode(namespace, parent, identifier, name);
                        System.out.println(indent + "Added variable node " + variableNode.getQualifiedName());
                        enterNode(variableNode);
                        ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
                        exitNode();
                        break;
                    }
                    default: System.out.println(indent + "Unsupported node class " + nodeClass + " : " + name + " (" + identifier + ") ");
                }
            }
        } catch (InterruptedException | ExecutionException e) {

        } catch (UaException e) {
            throw new RuntimeException(e);
        }
    }

}
