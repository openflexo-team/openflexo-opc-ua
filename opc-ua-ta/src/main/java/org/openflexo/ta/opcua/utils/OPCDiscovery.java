package org.openflexo.ta.opcua.utils;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.openflexo.ta.opcua.model.*;

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
    private final List<OPCNode> nodeStack;

    private OPCDiscovery(OpcUaClient aConnection, OPCServer aModel, OPCModelFactory aFactory) {
        this.connection = aConnection;
        this.model = aModel;
        this.factory = aFactory;
        this.namespaceMap = new HashMap<>();
        this.nodeStack = new ArrayList<>();
    }

    public static void browseAndPopulate(OpcUaClient aConnection, OPCServer aModel, OPCModelFactory aFactory) {
        OPCDiscovery discovery = new OPCDiscovery(aConnection, aModel, aFactory);
        discovery.browseNode(Identifiers.ObjectsFolder);
    }

    private OPCModelFactory getFactory() {
        return factory;
    }
    private OPCNamespace getNamespace(int anIndex) {
        return namespaceMap.computeIfAbsent(anIndex, i -> getFactory().makeOPCNamespace(model, i));
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

    private OPCNode getCurrentParent() {
        if (nodeStack.isEmpty()) return null;
        return nodeStack.get(nodeStack.size() - 1);
    }

    private void enterNode(OPCNode aNode) {
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
                final String identifier = ref.getNodeId().getIdentifier().toString();
                final String name = ref.getDisplayName().getText();
                final int nodeClass = ref.getNodeClass().getValue();
                final OPCNamespace namespace = getNamespace(ref.getNodeId().getNamespaceIndex().intValue());
                final OPCNode parent = getCurrentParent();
                String indent = "";
                for (int i = 0; i < nodeStack.size(); i++) indent += "  ";
                switch (ref.getNodeClass().getValue()) {
                    case 1: {
                        // Node is a folder
                        OPCFolderNode folderNode = getFactory().makeOPCFolderNode(namespace, parent, identifier, name);
                        System.out.println(indent + "Added object node " + folderNode.getQualifiedName());
                        enterNode(folderNode);
                        ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
                        exitNode();
                        break;
                    }
                    case 2: {
                        // Node is a variable
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

        }
    }

}
