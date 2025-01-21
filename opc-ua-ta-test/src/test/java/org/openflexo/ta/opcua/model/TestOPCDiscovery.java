package org.openflexo.ta.opcua.model;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
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
        OpcUaClient connection = OpcUaClient.create("opc.tcp://localhost:4880/minimal");
        connection.connect().get();

        // Create the model root
        OPCModelFactory factory = new OPCModelFactory(null, null);
        OPCServer model = factory.makeOPCServer();

        // Discover namespaces and nodes
        OPCDiscovery.browseAndPopulate(connection, model, factory);

        // Check result

        // Shutdown the client and server
        connection.disconnect().get();
        server.shutdown();
    }

}
