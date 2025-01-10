package org.imta.opc.examples.generic.server.opc;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;
import org.imta.opc.examples.generic.server.ServerDefinition;
import org.imta.opc.examples.generic.server.ServerRunner;

import java.util.Collections;

/**
 * Helper to set and start an OPC UA server given a ServerDefinition.
 *
 * See ServerRunner for how to interact with it.
 *
 * For OPC UA specifically, populating can be done interacting with the provided simple namespace.
 */
public class OPCServerRunner implements ServerRunner {

    private ServerDefinition definition;
    private OpcUaServer server;
    private OPCSimpleNamespace namespace;
    @Override
    public void initialize(ServerDefinition definition) {
        this.definition = definition;
        EndpointConfiguration endpoint = EndpointConfiguration.newBuilder()
                .setBindPort(definition.port)
                .setHostname(definition.hostname)
                .setPath(definition.path)
                .setBindAddress(definition.address)
                .build();
        OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
                .setApplicationName(LocalizedText.english("OPC Server \"" + definition.name + "\""))
                .setApplicationUri(definition.uri)
                .setEndpoints(Collections.singleton(endpoint))
                .build();
        server = new OpcUaServer(serverConfig);
        namespace = new OPCSimpleNamespace(server, definition.uri);
    }

    @Override
    public void populate() {}

    public OPCSimpleNamespace getNamespace() {
        return namespace;
    }

    @Override
    public void run() {
        server.startup();
        namespace.startup();
        //TODO: figure out why both are needed and if only one could work (it doesn't as is)
    }

    @Override
    public String getServerUrl() {
        return "opc.tcp://" + definition.hostname + ":" + definition.port + definition.path;
    }

    static public void main(String[] args) {
        ServerDefinition definition = new ServerDefinition("empty");
        ServerRunner runner = new OPCServerRunner();
        runner.initialize(definition);
        runner.populate();
        runner.run();
    }

}
