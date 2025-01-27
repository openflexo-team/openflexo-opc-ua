package org.openflexo.ta.opcua.model;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.utils.OPCDiscovery;

import java.util.concurrent.ExecutionException;

public class TestOPCDiscovery {

    @Test
    public void test() throws UaException, ExecutionException, InterruptedException, ModelDefinitionException {
        // Start the server
        MinimalServer server = new MinimalServer();
        MinimalNamespace namespace = new MinimalNamespace(server);
        server.startup();
        namespace.startup();

        // Make the client (connection)
        // This client is anonymous. To specify an application name, endpoint and security certificate see "create"
        OpcUaClient connection = OpcUaClient.create("opc.tcp://localhost:4880/minimal");
        connection.connect().get();

        // Create the factory
        OPCModelFactory factory = new OPCModelFactory(null, null);

        // Discover namespaces and nodes
        OPCServer model = OPCDiscovery.discover(connection, factory);

        // Check result
        // TODO : do that

        // Shutdown the client and server
        connection.disconnect().get();
        server.shutdown();

        // Output discovered model
        for (OPCNamespace ns : model.getNamespaces()) {
            for (OPCNode node : ns.getAllNodes()) {
                System.out.println(node.getQualifiedName());
            }
        }
    }

}
