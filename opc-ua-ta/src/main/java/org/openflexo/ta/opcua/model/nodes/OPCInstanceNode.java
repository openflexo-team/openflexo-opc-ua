package org.openflexo.ta.opcua.model.nodes;

import java.util.List;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.OPCObject;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCInstanceNode.OPCInstanceNodeImpl.class)
public interface OPCInstanceNode<N extends Node> extends OPCNode<N> {

	@PropertyIdentifier(type = OPCNode.class, cardinality = Getter.Cardinality.LIST)
	public static final String CHILDREN_KEY = "children";

	@Getter(value = CHILDREN_KEY, cardinality = Getter.Cardinality.LIST, inverse = OPCNode.PARENT_KEY)
	public List<OPCNode<?>> getChildren();

	@Adder(value = CHILDREN_KEY)
	@PastingPoint
	public void addToChildren(OPCNode<?> aNode);

	@Remover(value = CHILDREN_KEY)
	public void removeFromChildren(OPCNode<?> aNode);

	public static abstract class OPCInstanceNodeImpl<N extends Node> extends OPCNode.OPCNodeImpl<N> implements OPCInstanceNode<N> {

		@Override
		public boolean isRoot() {
			return getParent() == null;
		}

	}

}
