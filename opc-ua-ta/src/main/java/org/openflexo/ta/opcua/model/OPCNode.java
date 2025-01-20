package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCNode.OPCNodeImpl.class)
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

    @PropertyIdentifier(type = OPCFolderNode.class)
    public static final String OPC_PARENT = "parent";

    @Getter(value = OPC_PARENT)
    public OPCFolderNode getParent();

    @Setter(value = OPC_PARENT)
    public void setParent(OPCFolderNode aParent);

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

    public String getQualifiedName();

    public static abstract class OPCNodeImpl extends OPCObject.OPCObjectImpl implements OPCNode {

        @Override
        public String getQualifiedName() {
            // TODO : check if format is okay
            OPCFolderNode parent = getParent();
            if (parent != null) return parent.getQualifiedName() + "." + getName();
            return "[" + getNamespace().getIndex() + "] " + getName();
        }

    }

}
