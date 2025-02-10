package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
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

	public String getQualifiedName();

	@PropertyIdentifier(type = NodeId.class)
	public static final String NODE_ID_KEY = "nodeId";

	@Getter(value = NODE_ID_KEY, ignoreType = true)
	public NodeId getNodeId();

	@Setter(NODE_ID_KEY)
	public void setNodeId(NodeId nodeId);

	public N getNode();

	public String getName();

	public String getIdentifier();

	public static abstract class OPCNodeImpl<N extends UaNode> extends OPCObject.OPCObjectImpl implements OPCNode<N> {

		@Override
		public String getUri() {
			return getNamespace().getServer().getUri() + "#nsu=" + getNamespace().getUri() +";s=" + getQualifiedName();
		}

		@Override
		public String getQualifiedName() {
			OPCNode<?> parent = getParent();
			if (parent != null)
				return parent.getQualifiedName() + "." + getName();
			return getName();
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

		@Override
		public String getName() {
			return getNode().getBrowseName().getName();
		}

		@Override
		public String getIdentifier() {
			return getNodeId().getIdentifier().toString();
		}

	}

}
