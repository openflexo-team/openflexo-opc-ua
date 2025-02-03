package org.openflexo.ta.opcua.model;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;
import org.openflexo.ta.opcua.utils.OPCDiscovery;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        assertEquals(3, model.getNamespaces().size());
        OPCNamespace bioreactorNamespace = model.getNamespace(2);
        List<OPCNode<?>> rootNodes = bioreactorNamespace.getRootNodes();
        assertEquals(1, rootNodes.size());
        OPCVariableNode temperatureNode = null;
        Object value1;
        try {
            OPCObjectNode dataNode = (OPCObjectNode) rootNodes.get(0);
            temperatureNode = (OPCVariableNode) dataNode.getChildren().get(0);
            value1 = temperatureNode.getNode().getValue().getValue().getValue();
            System.out.println(temperatureNode.getNode().getValue().getStatusCode());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Shutdown the client and server
        connection.disconnect().get();
        server.shutdown();

        // Try to access a value anyway.
        Object value2 = temperatureNode.getNode().getValue().getValue().getValue();
        System.out.println(temperatureNode.getNode().getValue().getStatusCode());

        // Output discovered model
        for (OPCNamespace ns : model.getNamespaces()) {
            for (OPCNode node : ns.getAllNodes()) {
                System.out.println(node.getQualifiedName());
            }
        }
    }

}
