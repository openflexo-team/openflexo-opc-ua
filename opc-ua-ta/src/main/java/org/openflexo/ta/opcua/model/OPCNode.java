package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;

@ModelEntity(isAbstract = true)
public interface OPCNode extends OPCObject, ResourceData<OPCServer> {

    @PropertyIdentifier(type = OPCServer.class)
    public static final String OPC_NAMESPACE_KEY = "namespace";

    /**
     * Return {@link OPCNamespace} where this {@link OPCNode} is defined
     *
     * @return
     */
    @Getter(value = OPC_NAMESPACE_KEY)
    public OPCNamespace getNamespace();

    /**
     * Sets {@link OPCNamespace} where this {@link OPCNode} is defined
     *
     * @param aNamespace
     */
    @Setter(OPC_NAMESPACE_KEY)
    public void setNamespace(OPCNamespace aNamespace);

    @PropertyIdentifier(type = String.class)
    public static final String OPC_NAME = "name";

    /**
     * Return this {@link OPCNode} name
     *
     * @return
     */
    @Getter(value = OPC_NAME)
    public String getName();

    /**
     * Sets this {@link OPCNode} name
     *
     * @param aName
     */
    @Setter(OPC_NAME)
    public void setServer(String aName);

}
