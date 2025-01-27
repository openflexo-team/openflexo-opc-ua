package org.openflexo.ta.opcua.model.nodes;

import org.openflexo.pamela.annotations.*;

import java.util.List;

@ModelEntity(isAbstract = true)
public interface OPCInstanceNode extends OPCNode {

    @PropertyIdentifier(type = OPCNode.class, cardinality = Getter.Cardinality.LIST)
    public static final String OPC_CHILDREN = "children";

    @Getter(value = OPC_CHILDREN, cardinality = Getter.Cardinality.LIST, inverse = OPCNode.OPC_PARENT)
    public List<OPCNode> getChildren();

    @Adder(value = OPC_CHILDREN)
    @PastingPoint
    public void addToChildren(OPCNode aNode);

    @Remover(value = OPC_CHILDREN)
    public void removeFromChildren(OPCNode aNode);

}
