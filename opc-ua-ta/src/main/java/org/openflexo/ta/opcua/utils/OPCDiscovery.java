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
    private final List<OPCFolderNode> folderNodeStack;

    private OPCDiscovery(OpcUaClient aConnection, OPCServer aModel, OPCModelFactory aFactory) {
        this.connection = aConnection;
        this.model = aModel;
        this.factory = aFactory;
        this.namespaceMap = new HashMap<>();
        this.folderNodeStack = new ArrayList<>();
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

    private boolean isRoot() {
        return folderNodeStack.isEmpty();
    }

    private OPCFolderNode getCurrentFolder() {
        return folderNodeStack.get(folderNodeStack.size() - 1);
    }

    private void enterFolder(OPCFolderNode folder) {
        folderNodeStack.add(folder);
    }

    private void exitFolder() {
        folderNodeStack.remove(folderNodeStack.size() - 1);
    }

    private void browseNode(NodeId aNodeId) {
        BrowseDescription browse = makeBrowseDescription(aNodeId);
        try {
            BrowseResult browseResult = connection.browse(browse).get();
            List<ReferenceDescription> references = toList(browseResult.getReferences());
            for (ReferenceDescription ref : references) {
                final String identifier = ref.getNodeId().getIdentifier().toString();
                final String name = ref.getDisplayName().getText();
                switch (ref.getNodeClass().getValue()) {
                    case 1: {
                        // Node is a folder
                        OPCFolderNode folderNode;
                        if (isRoot()) {
                            OPCNamespace namespace = getNamespace(ref.getNodeId().getNamespaceIndex().intValue());
                            folderNode = getFactory().makeOPCFolderNode(namespace, identifier, name);
                        } else {
                            folderNode = getFactory().makeOPCFolderNode(getCurrentFolder(), identifier, name);
                        }
                        System.out.println("Entering folder node " + folderNode.getQualifiedName());
                        enterFolder(folderNode);
                        ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
                        System.out.println("Exiting folder node " + folderNode.getQualifiedName());
                        exitFolder();
                        break;
                    }
                    case 2: {
                        // Node is a variable
                        if (!isRoot()) {
                            OPCVariableNode variableNode = getFactory().makeOPCVariableNode(getCurrentFolder(), identifier, name);
                            System.out.println("Added variable node " + variableNode.getQualifiedName());
                        } else {
                            System.out.println("Ignored variable node " + name + " (does not have a namespace?)");
                        }
                        break;
                    }
                    default: // TODO : display some warning
                }
                ref.getNodeId().toNodeId(connection.getNamespaceTable())
                        .ifPresent(nodeId -> browseNode(nodeId));
            }
        } catch (InterruptedException | ExecutionException e) {

        }
    }

}
