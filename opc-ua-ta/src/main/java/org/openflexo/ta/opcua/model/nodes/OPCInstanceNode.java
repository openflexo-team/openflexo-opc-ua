package org.openflexo.ta.opcua.model.nodes;

import java.util.List;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;

@ModelEntity(isAbstract = true)
public interface OPCInstanceNode<N extends Node> extends OPCNode<N> {

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
