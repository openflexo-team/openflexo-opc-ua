package org.openflexo.ta.opcua.model;

import java.util.List;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;

@ModelEntity
@ImplementationClass(OPCNamespace.OPCNamespaceImpl.class)
public interface OPCNamespace extends OPCObject, ResourceData<OPCServer> {

	@PropertyIdentifier(type = OPCServer.class)
	public static final String OPC_SERVER_KEY = "OPCServer";

	/**
	 * Return {@link OPCServer} where this {@link OPCNamespace} is defined
	 *
	 * @return
	 */
	@Getter(value = OPC_SERVER_KEY)
	public OPCServer getServer();

	/**
	 * Sets {@link OPCServer} where this {@link OPCNamespace} is defined
	 *
	 * @param aServer
	 */
	@Setter(OPC_SERVER_KEY)
	public void setServer(OPCServer aServer);

	@PropertyIdentifier(type = OPCNode.class, cardinality = Getter.Cardinality.LIST)
	public static final String OPC_NODES_KEY = "namespaces";

	/**
	 * Return all {@link OPCNode} defined in this {@link OPCNamespace}
	 *
	 * @return
	 */
	@Getter(value = OPC_NODES_KEY, cardinality = Getter.Cardinality.LIST, inverse = OPCNode.OPC_NAMESPACE_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(CloningStrategy.StrategyType.CLONE)
	public List<OPCNode> getAllNodes();

	// TODO : how to define getRootNodes?

	@Adder(OPC_NODES_KEY)
	@PastingPoint
	public void addToNamespace(OPCNode aNode);

	@Remover(OPC_NODES_KEY)
	public void removeFromNamespace(OPCNode aNode);

	public int getIndex();

	public static abstract class OPCNamespaceImpl extends OPCObjectImpl implements OPCNamespace {

		@Override
		public int getIndex() {
			// TODO : is this reasonable? is this coherent with OPC side of things? (go have a look at the doc)
			return getServer().getNamespaces().indexOf(this);
		}

	}

}
