package org.openflexo.ta.opcua.utils;

import org.openflexo.ta.opcua.model.OPCServer;

/**
 * Thrown when an {@link OPCServer} cannot be reached, ie when no {@link org.eclipse.milo.opcua.sdk.client.OpcUaClient} could be connected to
 * its endpoint.
 *
 * This is an unchecked exception because it may be raised from contexts whose signature cannot be extended (resource deserializing, binding
 * evaluation), but it is always meant to be reported to the user with the URI of the unreachable server.
 *
 * @author Sylvain
 */
public class OPCConnectionException extends RuntimeException {

	private final String serverUri;

	public OPCConnectionException(String serverUri) {
		super("Cannot connect to OPC UA server " + serverUri);
		this.serverUri = serverUri;
	}

	public String getServerUri() {
		return serverUri;
	}

}
