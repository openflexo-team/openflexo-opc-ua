package org.openflexo.ta.opcua.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.openflexo.pamela.factory.PamelaModelFactory;

import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;
import org.openflexo.ta.opcua.rm.OPCServerResource;
import org.openflexo.ta.opcua.rm.OPCServerResourceImpl;
import org.openflexo.ta.opcua.utils.OPCDiscovery;

public class TestOPCDiscovery {

    @Test
    public void test() throws ModelDefinitionException {
        // Start the server
        MinimalServer server = new MinimalServer();
        MinimalNamespace namespace = new MinimalNamespace(server);
        server.startup();
        namespace.startup();

        // Create the factory and base model
        OPCModelFactory factory = new OPCModelFactory(null, null);
        OPCServer model = factory.makeOPCServerFromHostname("localhost", 4880, "minimal");

        // Discover
        OPCDiscovery.discover(model, factory);

        // Check result
        assertEquals(3, model.getNamespaces().size());
        OPCNamespace bioreactorNamespace = model.getNamespace(2);
        List<OPCNode<?>> rootNodes = bioreactorNamespace.getRootNodes();
        assertEquals(1, rootNodes.size());
        OPCVariableNode temperatureNode = null;
        try {
            OPCObjectNode dataNode = (OPCObjectNode) rootNodes.get(0);
            temperatureNode = (OPCVariableNode) dataNode.getChildren().get(0);
            temperatureNode.getNode().readValue();
        } catch (Exception e) {
            fail(e.getMessage());
            model.shutdownClient();
            server.shutdown();
            return;
        }

        // Shutdown the client
        model.shutdownClient();

        // Test if one can still read a value
        try {
            temperatureNode.getNode().readValue();
            fail("Reading a value while client should be disconnected");
        } catch (Exception ignored) {}

        // Shutdown the server
        server.shutdown();
    }

}
