package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;

import java.util.List;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCNode.OPCNodeImpl.class)
@Imports({@Import(OPCVariableNode.class), @Import(OPCFolderNode.class)})
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

    @PropertyIdentifier(type = OPCNode.class)
    public static final String OPC_PARENT = "parent";

    @Getter(value = OPC_PARENT)
    public OPCNode getParent();

    @Setter(value = OPC_PARENT)
    public void setParent(OPCNode aParent);

    @PropertyIdentifier(type = String.class)
    public static final String OPC_NAME = "name";

    /**
     * Return this {@link OPCNode} name
     *
     * @return
     */
    @Getter(value = OPC_NAME)
    public String getName();

    @Setter(value = OPC_NAME)
    public void setName(String aName);

    @PropertyIdentifier(type = String.class)
    public static final String OPC_IDENTIFIER = "identifier";

    @Getter(OPC_IDENTIFIER)
    public String getIdentifier();

    @Setter(OPC_IDENTIFIER)
    public void setIdentifier(String anIdentifier);

    public String getQualifiedName();

    @PropertyIdentifier(type = OPCNode.class, cardinality = Getter.Cardinality.LIST)
    public static final String OPC_CHILDREN = "children";

    @Getter(value = OPC_CHILDREN, cardinality = Getter.Cardinality.LIST, inverse = OPCNode.OPC_PARENT)
    public List<OPCNode> getChildren();

    @Adder(value = OPC_CHILDREN)
    @PastingPoint
    public void addToChildren(OPCNode aNode);

    @Remover(value = OPC_CHILDREN)
    public void removeFromChildren(OPCNode aNode);

    public static abstract class OPCNodeImpl extends OPCObject.OPCObjectImpl implements OPCNode {

        @Override
        public String getQualifiedName() {
            // TODO : check if format is okay
            // TODO : check if we have to use identifier instead
            OPCNode parent = getParent();
            if (parent != null) return parent.getQualifiedName() + "." + getName();
            return getIdentifier() + " [" + getNamespace().getIndex() + "] " + getName();
        }

        @Override
        public OPCServer getResourceData() {
            return getNamespace().getServer();
        }

    }

}
