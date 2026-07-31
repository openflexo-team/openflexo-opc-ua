package org.imta.opc.examples.minimal;

import java.util.Collections;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;

/**
 * Minimal example of a server, hopefully generic enough to not be OPC Specific
 */
public class MinimalServer extends OpcUaServer {

	/** Default port this example server binds to */
	static public final int DEFAULT_BIND_PORT = 4880;

	public MinimalServer() {
		this(DEFAULT_BIND_PORT);
	}

	/**
	 * Build a minimal server bound to supplied port.
	 *
	 * Tests must each use their own port: the test task forks several JVMs in parallel, and two servers cannot bind the same port (the
	 * losing one silently logs a BindException and never serves anything).
	 */
	public MinimalServer(int bindPort) {
		super(buildConfig(bindPort));
	}

	static public void main(String[] args) {
		MinimalServer server = new MinimalServer();
		MinimalNamespace namespace = new MinimalNamespace(server);
		server.startup();
		namespace.startup();
	}

	static private OpcUaServerConfig buildConfig(int bindPort) {
		// Define endpoint configurations
		// Matching URL: opc.tcp://localhost:<bindPort>/minimal
		EndpointConfiguration endpoint = EndpointConfiguration.newBuilder().setBindPort(bindPort).setHostname("localhost")
				.setPath("/minimal").setBindAddress("0.0.0.0").build();

		// Build server configuration
		OpcUaServerConfig serverConfig = OpcUaServerConfig.builder().setApplicationName(LocalizedText.english("Simple OPC UA Server"))
				.setApplicationUri("urn:example:minimal:server").setEndpoints(Collections.singleton(endpoint)).build();

		return serverConfig;
	}

}
