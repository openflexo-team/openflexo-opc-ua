package org.openflexo.ta.opcua.model;

import java.util.List;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

@ModelEntity
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

}
