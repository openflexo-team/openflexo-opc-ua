package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.types.OPCIdentifierType;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.OPCObject;
import org.openflexo.ta.opcua.model.OPCServer;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCNode.OPCNodeImpl.class)
@Imports({ @Import(OPCVariableNode.class), @Import(OPCObjectNode.class) })
public interface OPCNode<N extends UaNode> extends OPCObject, ResourceData<OPCServer> {

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
	public static final String IDENTIFIER_STRING_KEY = "identifierString";

	@Getter(IDENTIFIER_STRING_KEY)
	public String getIdentifierString();

	@Setter(IDENTIFIER_STRING_KEY)
	public void setIdentifierString(String string);

	@PropertyIdentifier(type = OPCIdentifierType.class)
	public static final String IDENTIFIER_TYPE_KEY = "identifierType";

	@Getter(IDENTIFIER_TYPE_KEY)
	public OPCIdentifierType getIdentifierType();

	@Setter(IDENTIFIER_TYPE_KEY)
	public void setIdentifierType(OPCIdentifierType type);

	public Object getIdentifier();

	public void setIdentifier(Object identifier);

	public NodeId getNodeId();

	public N getNode();

	public String getName();

	public String getQualifiedName();

	public static abstract class OPCNodeImpl<N extends UaNode> extends OPCObject.OPCObjectImpl implements OPCNode<N> {

		@Override
		public String getUri() {
			return getNamespace().getServer().getUri() + "#nsu=" + getNamespace().getUri() +";s=" + getQualifiedName();
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

		public Object getIdentifier() {
			return getIdentifierType().parseString(getIdentifierString());
		}

		public void setIdentifier(Object identifier) {
			setIdentifierString(identifier.toString());
			setIdentifierType(OPCIdentifierType.fromInstance(identifier));
			nodeId = getIdentifierType().makeNodeId(getNamespace().getIndex(), getIdentifierString());
		}

		private NodeId nodeId;

		public NodeId getNodeId() {
			return nodeId;
		}

		@Override
		public String getName() {
			return getNode().getBrowseName().getName();
		}

		@Override
		public String getQualifiedName() {
			OPCNode<?> parent = getParent();
			if (parent != null)
				return parent.getQualifiedName() + "." + getName();
			return getName();
		}

	}

}
