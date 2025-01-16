package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.rm.OPCServerResource;

import java.util.List;
import java.util.logging.Logger;

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

    }

}
