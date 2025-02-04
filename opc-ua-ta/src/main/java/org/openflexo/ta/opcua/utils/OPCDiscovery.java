package org.openflexo.ta.opcua.utils;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.openflexo.ta.opcua.model.OPCModelFactory;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.ta.opcua.model.nodes.OPCInstanceNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;

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

	public static void discover(OPCServer model, OPCModelFactory factory) {
		OPCDiscovery discovery = new OPCDiscovery(model.getClient(), model, factory);
		discovery.initialize();
		discovery.browseNode(Identifiers.ObjectsFolder);
	}

	public static void discover(OPCServer model) {
		discover(model, model.getFactory());
	}

	private void initialize() {
		// String applicationUri = connection.getConfig().getEndpoint().getServer().getApplicationUri();
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
		if (returned != null)
			return returned;
		String uri = aNodeId.getNamespaceUri();
		returned = getFactory().makeOPCNamespace(model, uri, index);
		namespaceMap.put(index, returned);
		return returned;
	}

	private static BrowseDescription makeBrowseDescription(NodeId aNodeId) {
		return new BrowseDescription(aNodeId, BrowseDirection.Forward, Identifiers.References, true,
				uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()), uint(BrowseResultMask.All.getValue()));
	}

	private OPCInstanceNode getCurrentParent() {
		if (nodeStack.isEmpty())
			return null;
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
			final NamespaceTable namespaceTable = connection.getNamespaceTable();
			for (ReferenceDescription ref : references) {
				final Node node = connection.getAddressSpace().getNode(aNodeId);
				final String identifier = ref.getNodeId().getIdentifier().toString();
				final String name = ref.getBrowseName().getName();
				final int nodeClass = ref.getNodeClass().getValue();
				final OPCNamespace namespace = getNamespace(ref.getNodeId());
				final OPCInstanceNode parent = getCurrentParent();
				String indent = "";

				for (int i = 0; i < nodeStack.size(); i++)
					indent += "  ";
				switch (ref.getNodeClass().getValue()) {
					case 1: {
						// Node is an object
						OPCObjectNode objectNode = getFactory().makeOPCObjectNode((ObjectNode) node, namespace, parent, identifier, name);
						System.out.println(indent + "Added object node " + objectNode.getQualifiedName());
						enterNode(objectNode);
						ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
						exitNode();
						break;
					}
					case 2: {
						// Node is a variable
						VariableNode miloNode;
						try {
							miloNode = connection.getAddressSpace().getVariableNode(ref.getNodeId().toNodeId(namespaceTable).get());
						} catch (Exception e) {
							System.err.println(e);
							break;
						}
						OPCVariableNode variableNode = getFactory().makeOPCVariableNode(miloNode, namespace, parent, identifier, name);
						System.out.println(indent + "Added variable node " + variableNode.getUri());
						enterNode(variableNode);
						ref.getNodeId().toNodeId(connection.getNamespaceTable()).ifPresent(this::browseNode);
						exitNode();
						break;
					}
					default:
						System.out.println(indent + "Unsupported node class " + nodeClass + " : " + name + " (" + identifier + ") ");
				}
			}
		} catch (InterruptedException | ExecutionException e) {

		} catch (UaException e) {
			throw new RuntimeException(e);
		}
	}

}
