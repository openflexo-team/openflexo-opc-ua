package org.openflexo.ta.opcua.milo;

import static org.junit.Assert.assertEquals;

import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.Test;

public class TestMinimalServer {

	// TODO : discuss JaCoCo, code coverage utility that has issues handling large methods.

	/**
	 * Port dedicated to this test class: the test task runs several forked JVMs in parallel, so each test class starting a
	 * {@link MinimalServer} must use a distinct port (see {@link MinimalServer#MinimalServer(int)}).
	 */
	private static final int BIND_PORT = 4882;

	private MinimalServer server;

	@Test
	public void test() {
		// Start the server
		server = new MinimalServer(BIND_PORT);
		MinimalNamespace namespace = new MinimalNamespace(server);
		server.startup();
		namespace.startup();

		// Test something
		OpcUaNamespace serverNamespace = server.getOpcUaNamespace();
		assertEquals(0, serverNamespace.getNamespaceIndex().intValue());

		// Shutdown the server
		server.shutdown();
	}

}
