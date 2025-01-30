package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
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
	public static final String OPC_PARENT = "parent";

	@Getter(value = OPC_PARENT)
	public OPCInstanceNode getParent();

	@Setter(value = OPC_PARENT)
	public void setParent(OPCInstanceNode aParent);

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

	@PropertyIdentifier(type = Node.class)
	public static final String NODE = "node";

	@Getter(value = NODE, ignoreType = true)
	public N getNode();

	@Setter(NODE)
	public void setNode(N aNode);

	public static abstract class OPCNodeImpl extends OPCObject.OPCObjectImpl implements OPCNode {

		@Override
		public String getUri() {
			OPCNode parent = getParent();
			if (parent != null)
				return parent.getQualifiedName() + getName() + "/";
			return getNamespace().getUri() + getName() + "/";
		}

		@Override
		public String getQualifiedName() {
			OPCNode parent = getParent();
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

	}

}
