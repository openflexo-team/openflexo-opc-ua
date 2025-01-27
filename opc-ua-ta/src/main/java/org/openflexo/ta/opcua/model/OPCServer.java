package org.openflexo.ta.opcua.model;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.rm.OPCServerResource;

@ModelEntity
@ImplementationClass(value = OPCServer.OPCServerImpl.class)
public interface OPCServer extends OPCObject, ResourceData<OPCServer> {

	@PropertyIdentifier(type = String.class)
	public static final String URI_KEY = "uri";

	@Override
	@Getter(value = URI_KEY)
	public String getUri();

	@Setter(value = URI_KEY)
	public void setUri(String anUri);

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

	public String getUrl();

	@Override
	public OPCServerResource getResource();

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
			// TODO : illustration purpose
			String returned = (String) performSuperGetter(BIND_ADDRESS_KEY);
			return returned;
		}

		@Override
		public String getUrl() {
			// TODO : handle cases where bindAddress has to be used instead
			return "opc.tcp://"+getHostname()+":"+getBindPort()+"/"+getApplicationName();
		}

		@Override
		public OPCNamespace getNamespace(Integer anIndex) {
			// TODO : Reasonable?
			for (OPCNamespace namespace : getNamespaces()) {
				if (namespace.getIndex().equals(anIndex)) return namespace;
			}
			return null;
		}

	}

}
