package org.imta.opc.examples.minimal;

import java.util.List;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;

public class MinimalNamespace extends ManagedNamespaceWithLifecycle {

	static private String URI = "urn:example:minimal:namespace/";

	/** Handles subscriptions **/
	private final SubscriptionModel subscriptionModel;

	private final DateTime startDate;
	private Thread lifeline;

	public MinimalNamespace(OpcUaServer server) {
		super(server, URI);
		this.subscriptionModel = new SubscriptionModel(server, this);
		startDate = DateTime.now();
		getLifecycleManager().addLifecycle(subscriptionModel);
		getLifecycleManager().addStartupTask(this::startupTask);
		getLifecycleManager().addShutdownTask(this::shutdownTask);
		UaFolderNode dataFolderNodeId = createDataFolderNode();
		addTemperature(dataFolderNodeId);
	}

	private void startupTask() {
		lifeline = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(2_000);
				} catch (InterruptedException ignored) {

				}
			}
		});
		lifeline.start();
	}

	private void shutdownTask() {
		lifeline.interrupt();
		try {
			lifeline.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private UaFolderNode createDataFolderNode() {
		// Create and add a root "folder" for data variables
		NodeId dataNodeId = newNodeId("Data");
		UaFolderNode dataNode = new UaFolderNode(getNodeContext(), dataNodeId, newQualifiedName("Data"), LocalizedText.english("Data"));
		getNodeManager().addNode(dataNode);
		dataNode.addReference(new Reference(dataNode.getNodeId(), Identifiers.Organizes, Identifiers.ObjectsFolder.expanded(), false));
		return dataNode;
	}

	private void addTemperature(UaFolderNode dataFolderNode) {
		UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
				.setNodeId(newNodeId(dataFolderNode.getNodeId().getIdentifier() + "/" + "temperature"))
				.setBrowseName(newQualifiedName("temperature")).setDisplayName(LocalizedText.english("temperature"))
				.setDataType(Identifiers.Double).setTypeDefinition(Identifiers.BaseDataVariableType).build();
		node.setValue(new DataValue(new Variant(getTemperature())));
		node.getFilterChain().addLast(new AttributeFilter() {
		}, AttributeFilters.getValue(ctx -> new DataValue(new Variant(getTemperature()))));
		getNodeManager().addNode(node);
		dataFolderNode.addOrganizes(node);
	}

	private double getTemperature() {
		double elapsedTime = DateTime.now().getJavaTime() - startDate.getJavaTime();
		return Math.sin(elapsedTime / 5000) * 15 + 15;
	}

	@Override
	public void onDataItemsCreated(List<DataItem> list) {
		subscriptionModel.onDataItemsCreated(list);
	}

	@Override
	public void onDataItemsModified(List<DataItem> list) {
		subscriptionModel.onDataItemsModified(list);
	}

	@Override
	public void onDataItemsDeleted(List<DataItem> list) {
		subscriptionModel.onDataItemsDeleted(list);
	}

	@Override
	public void onMonitoringModeChanged(List<MonitoredItem> list) {
		subscriptionModel.onMonitoringModeChanged(list);
	}

}
