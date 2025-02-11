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
    public MinimalServer() {
        super(buildConfig());
    }
    static public void main(String[] args) {
        MinimalServer server = new MinimalServer();
        MinimalNamespace namespace = new MinimalNamespace(server);
        server.startup();
        namespace.startup();
    }

    static private OpcUaServerConfig buildConfig() {
        // Define endpoint configurations
        // Matching URL: opc.tcp://localhost:4880/minimal
        EndpointConfiguration endpoint = EndpointConfiguration.newBuilder()
                .setBindPort(4880)
                .setHostname("localhost")
                .setPath("/minimal")
                .setBindAddress("0.0.0.0")
                .build();

        // Build server configuration
        OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
                .setApplicationName(LocalizedText.english("Simple OPC UA Server"))
                .setApplicationUri("urn:example:minimal:server")
                .setEndpoints(Collections.singleton(endpoint))
                .build();

        return serverConfig;
    }

}
