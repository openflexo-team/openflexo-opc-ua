package org.openflexo.ta.opcua.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;
import org.openflexo.ta.opcua.utils.OPCDiscovery;

public class TestOPCDiscovery {

	/**
	 * Port dedicated to this test class: the test task runs several forked JVMs in parallel, so each test class starting a
	 * {@link MinimalServer} must use a distinct port (see {@link MinimalServer#MinimalServer(int)}).
	 */
	private static final int BIND_PORT = 4881;

	@Test
	public void test() throws ModelDefinitionException {
		// Start the server
		MinimalServer server = new MinimalServer(BIND_PORT);
		MinimalNamespace namespace = new MinimalNamespace(server);
		server.startup();
		namespace.startup();

		// Create the factory and base model
		OPCModelFactory factory = new OPCModelFactory(null, null);
		OPCServer model = factory.makeOPCServerFromHostname("localhost", BIND_PORT, "minimal");

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
		} catch (Exception ignored) {
		}

		// Shutdown the server
		server.shutdown();
	}

}
