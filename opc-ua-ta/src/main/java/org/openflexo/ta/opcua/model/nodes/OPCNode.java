package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.OPCObject;
import org.openflexo.ta.opcua.model.OPCServer;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCNode.OPCNodeImpl.class)
@Imports({ @Import(OPCVariableNode.class), @Import(OPCObjectNode.class) })
public interface OPCNode<N extends Node> extends OPCObject, ResourceData<OPCServer> {

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

	@PropertyIdentifier(type = OPCInstanceNode.class)
	public static final String PARENT_KEY = "parent";

	@Getter(value = PARENT_KEY)
	public OPCInstanceNode<?> getParent();

	@Setter(value = PARENT_KEY)
	public void setParent(OPCInstanceNode<?> aParent);

	public boolean isRoot();

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";

	/**
	 * Return this {@link OPCNode} name
	 *
	 * @return
	 */
	@Getter(value = NAME_KEY)
	public String getName();

	@Setter(value = NAME_KEY)
	public void setName(String aName);

	@PropertyIdentifier(type = String.class)
	public static final String IDENTIFIER_KEY = "identifier";

	@Getter(IDENTIFIER_KEY)
	public String getIdentifier();

	@Setter(IDENTIFIER_KEY)
	public void setIdentifier(String anIdentifier);

	public String getQualifiedName();

	@PropertyIdentifier(type = Node.class)
	public static final String NODE_KEY = "node";

	@Getter(value = NODE_KEY, ignoreType = true)
	public N getNode();

	@Setter(NODE_KEY)
	public void setNode(N aNode);

	public static abstract class OPCNodeImpl<N extends Node> extends OPCObject.OPCObjectImpl implements OPCNode<N> {

		@Override
		public String getUri() {
			OPCNode<?> parent = getParent();
			if (parent != null)
				return parent.getQualifiedName() + getName() + "/";
			return getNamespace().getUri() + getName() + "/";
		}

		@Override
		public String getQualifiedName() {
			OPCNode<?> parent = getParent();
			if (parent != null)
				return parent.getQualifiedName() + "." + getName();
			return getNamespace().getIndex() + "." + getName();
		}

		@Override
		public OPCServer getResourceData() {
			return getNamespace().getServer();
		}

		@Override
		public String getDisplayableName() {
			return getName();
		}

		@Override
		public String getDisplayableDescription() {
			return "OPCNode: " + getName();
		}

		@Override
		public boolean isRoot() {
			return false;
		}

	}

}
