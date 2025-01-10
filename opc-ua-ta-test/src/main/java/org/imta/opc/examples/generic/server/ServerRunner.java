package org.imta.opc.examples.generic.server;

/**
 * A meant to be generic server runner interface.
 *
 * It is meant to be used as such: 1) call server.initialize(def); 2) call server.populate(); 3) call server.run.
 */
public interface ServerRunner {

    void initialize(ServerDefinition definition);
    void populate();

    void run();

    String getServerUrl();

}
