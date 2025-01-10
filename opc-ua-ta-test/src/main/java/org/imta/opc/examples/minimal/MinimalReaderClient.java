package org.imta.opc.examples.minimal;

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.concurrent.ExecutionException;

public class MinimalReaderClient extends MinimalBrowserClient {

    public MinimalReaderClient() {
        super("opc.tcp://localhost:4880/minimal");
    }

    static public void main(String[] args) throws UaException, ExecutionException, InterruptedException {
        MinimalReaderClient client = new MinimalReaderClient();
        client.start();
        NodeId nodeId = new NodeId(2, "Data/temperature");
        client.watchVariable(nodeId);
        client.stop();
    }

    public void watchVariable(NodeId nodeId) throws UaException, InterruptedException {
        UaVariableNode node = getClient().getAddressSpace().getVariableNode(nodeId);
        while (true) {
            getLogger().info("temperature="+node.readValue().getValue().getValue());
            Thread.sleep(2000);
        }
    }

}
