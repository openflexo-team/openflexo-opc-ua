package org.imta.opc.examples.minimal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimalBrowserClient {

	private final String serverUrl;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private OpcUaClient client;

	public MinimalBrowserClient(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	static public void main(String[] args) throws UaException, ExecutionException, InterruptedException {
		MinimalBrowserClient browserClient = new MinimalBrowserClient("opc.tcp://localhost:4880/minimal");
		browserClient.start();
		browserClient.browse();
		browserClient.stop();
	}

	public Logger getLogger() {
		return logger;
	}

	public OpcUaClient getClient() {
		return client;
	}

	public void start() throws UaException, ExecutionException, InterruptedException {
		client = OpcUaClient.create(serverUrl);
		client.connect().get();
	}

	public void browse() {
		browseNode("", client, Identifiers.ObjectsFolder);
	}

	public void stop() throws ExecutionException, InterruptedException {
		client.disconnect().get();
	}

	private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
		BrowseDescription browse = new BrowseDescription(browseRoot, BrowseDirection.Forward, Identifiers.References, true,
				uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()), uint(BrowseResultMask.All.getValue()));

		try {
			BrowseResult browseResult = client.browse(browse).get();

			List<ReferenceDescription> references = toList(browseResult.getReferences());

			for (ReferenceDescription rd : references) {
				System.out.println(indent + " Node=" + rd.getBrowseName());

				// recursively browse to children
				rd.getNodeId().toNodeId(client.getNamespaceTable()).ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
		}
	}

}
