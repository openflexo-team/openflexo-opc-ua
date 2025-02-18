package org.imta.opc.examples.generic.server.opc;

import java.util.List;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;

/**
 * A simple namespace that should be enough to manage folder, variable nodes and subscriptions.
 */
public class OPCSimpleNamespace extends ManagedNamespaceWithLifecycle {

	private final SubscriptionModel subscriptionModel;
	private Thread lifeline;

	public OPCSimpleNamespace(OpcUaServer server, String uri) {
		super(server, uri);
		this.subscriptionModel = new SubscriptionModel(server, this);
		getLifecycleManager().addLifecycle(subscriptionModel);
		getLifecycleManager().addStartupTask(this::startupTask);
		getLifecycleManager().addShutdownTask(this::shutdownTask);
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
