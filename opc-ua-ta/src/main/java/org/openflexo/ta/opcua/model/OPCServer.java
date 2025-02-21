package org.openflexo.ta.opcua.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.rm.OPCServerResource;
import org.openflexo.ta.opcua.utils.OPCDiscovery;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.OPCServerModelSlot;

/**
 * Represents a connection to an OPC UA server within OpenFlexo.
 *
 * <p>For more details on OPC UA integration in OpenFlexo, see
 * {@link org.openflexo.ta.opcua}.</p>
 *
 * <p>The {@code OPCServer} class manages the connection to an OPC UA server,
 * enabling access to its namespaces and nodes. It utilizes the Eclipse Milo
 * library for communication and handles the discovery of the server's address
 * space.</p>
 *
 * <h2>Server Identification</h2>
 * <p>An OPC UA server is uniquely identified by its URI, constructed from:</p>
 *  <ul>
 *   <li><b>Hostname:</b> The network address of the server.</li>
 *   <li><b>Bind Port:</b> The port used for the OPC UA connection.</li>
 *   <li><b>Application Name:</b> The registered name of the server.</li>
 * </ul>
 *
 * <p>For details on OPC UA server discovery and registration, refer to the
 * <a href="https://reference.opcfoundation.org/Core/Part12/v105/">OPC UA Discovery Specification</a>.</p>
 *
 * <h2>Key Responsibilities</h2>
 * <ul>
 *   <li>Managing the {@link OpcUaClient} connection to the server.</li>
 *   <li>Discovering and organizing {@link OPCNamespace} and {@link OPCNode}
 *       instances representing the server's address space.</li>
 *   <li>Providing methods to read and write data to the server's nodes.</li>
 * </ul>
 *
 * <p>For related resources and model slots, refer to
 * {@link OPCServerResource} and
 * {@link OPCServerModelSlot}.</p>
 *
 * @see OPCNamespace
 * @see OPCNode
 * @see OpcUaClient
 *
 * @author Luka, Sylvain
 */

@ModelEntity
@ImplementationClass(value = OPCServer.OPCServerImpl.class)
public interface OPCServer extends OPCObject, ResourceData<OPCServer> {

	@PropertyIdentifier(type = String.class)
	public static final String HOSTNAME_KEY = "hostname";

	@Getter(value = HOSTNAME_KEY)
	public String getHostname();

	@Setter(value = HOSTNAME_KEY)
	public void setHostname(String aHostname);

	@PropertyIdentifier(type = Integer.class)
	public static final String BIND_PORT_KEY = "bindPort";

	@Getter(value = BIND_PORT_KEY)
	public Integer getBindPort();

	@Setter(value = BIND_PORT_KEY)
	public void setBindPort(Integer aBindPort);

	@PropertyIdentifier(type = String.class)
	public static final String APPLICATION_NAME_KEY = "applicationName";

	@Getter(value = APPLICATION_NAME_KEY)
	public String getApplicationName();

	@Setter(value = APPLICATION_NAME_KEY)
	public void setApplicationName(String aName);

	@PropertyIdentifier(type = String.class)
	public static final String BIND_ADDRESS_KEY = "bindAddress";

	@Getter(value = BIND_ADDRESS_KEY)
	public String getBindAddress();

	@Setter(value = BIND_ADDRESS_KEY)
	public void setBindAddress(String aBindAddress);

	public boolean isConnected();

	public OpcUaClient getClient();

	public void shutdownClient();

	@PropertyIdentifier(type = OPCNamespace.class, cardinality = Getter.Cardinality.LIST)
	public static final String NAMESPACES_KEY = "namespaces";

	/**
	 * Return all {@link OPCNamespace} defined in this {@link OPCServer}
	 *
	 * @return
	 */
	@Getter(value = NAMESPACES_KEY, cardinality = Getter.Cardinality.LIST, inverse = OPCNamespace.OPC_SERVER_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(CloningStrategy.StrategyType.CLONE)
	public List<OPCNamespace> getNamespaces();

	@Adder(NAMESPACES_KEY)
	@PastingPoint
	public void addToNamespaces(OPCNamespace aNamespace);

	@Remover(NAMESPACES_KEY)
	public void removeFromNamespaces(OPCNamespace aNamespace);

	public OPCNamespace getNamespace(Integer anIndex);

	@Override
	public String getUri();

	@Override
	public OPCServerResource getResource();

	public void performDiscovery();

	public static abstract class OPCServerImpl extends OPCObjectImpl implements OPCServer {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(OPCObjectImpl.class.getPackage().getName());

		@Override
		public OPCServer getResourceData() {
			return this;
		}

		@Override
		public OPCServerResource getResource() {
			return (OPCServerResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public String toString() {
			return super.toString() + "-" + getResource();
		}

		@Override
		public String getBindAddress() {
			// illustration purpose
			String returned = (String) performSuperGetter(BIND_ADDRESS_KEY);
			return returned;
		}

		@Override
		public String getUri() {
			// TODO : handle cases where bindAddress has to be used instead
			return "opc.tcp://" + getHostname() + ":" + getBindPort() + "/" + getApplicationName();
		}

		@Override
		public OPCNamespace getNamespace(Integer anIndex) {
			for (OPCNamespace namespace : getNamespaces()) {
				if (namespace.getIndex().equals(anIndex))
					return namespace;
			}
			return null;
		}

		private OpcUaClient client;

		@Override
		public boolean isConnected() {
			if (client == null) return false;
			try {
				return client.getSession().thenApply(Objects::nonNull).get();
			} catch (ExecutionException | InterruptedException e) {
				return false;
			}
		}

		@Override
		public OpcUaClient getClient() {
			final String uri = getUri();
			if (client == null) {
				try {
					logger.info("Creating an OpcUaClient to connect to " + uri);
					client = OpcUaClient.create(getUri());
				} catch (UaException e) {
					logger.warning("Exception while creating the client: " + e.getMessage());
					return null;
				}
			}
			if (isConnected()) return client;
			try {
				client.connect().get();
				if (isConnected()) {
					logger.info("Connected to " + uri);
				} else {
					logger.warning("Something went wrong while connecting to " + uri);
				}
			} catch (InterruptedException | ExecutionException e) {
				logger.warning("Exception while connecting a client: " + e.getMessage());
			}
			return client;
		}

		@Override
		public void shutdownClient() {
			if (!isConnected())
				return;
			try {
				client.disconnect().get();
			} catch (InterruptedException | ExecutionException e) {
				System.err.println(e.getMessage());
			}
		}

		@Override
		public void performDiscovery() {
			logger.info("Perform discovery for " + this);
			OPCDiscovery.discover(this);
		}

		@Override
		public String getDisplayableName() {
			return getUri();
		}

		@Override
		public String getDisplayableDescription() {
			return "OPCServer: " + getUri();
		}

	}

}
