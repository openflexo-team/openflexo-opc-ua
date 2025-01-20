package org.openflexo.ta.opcua.milo;

import static org.junit.Assert.assertEquals;

import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.imta.opc.examples.minimal.MinimalNamespace;
import org.imta.opc.examples.minimal.MinimalServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMinimalServer {

    // TODO : discuss JaCoCo, code coverage utility that has issues handling large methods.

    private MinimalServer server;

    @Test
    public void test() {
        // Start the server
        server = new MinimalServer();
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
