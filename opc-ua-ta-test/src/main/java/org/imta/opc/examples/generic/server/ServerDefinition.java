package org.imta.opc.examples.generic.server;

/**
 * A server base definition.
 *
 * Does not yet include advanced options, such as a security policy. Does not yet include information about what can be found.
 */
public class ServerDefinition {

	public String name;
	public String uri;

	public String hostname;
	public String path;
	public int port;
	public String address;

	public ServerDefinition(String name, String uri, String hostname, String path, int port, String address) {
		this.name = name;
		this.uri = uri;
		this.hostname = hostname;
		this.path = path;
		this.port = port;
		this.address = address;
	}

	public ServerDefinition(String name) {
		this(name, "urn:server:" + name, "localhost", "/" + name, 4880, "0.0.0.0");
	}

}
