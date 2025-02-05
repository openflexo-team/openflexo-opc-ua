package org.openflexo.ta.opcua.model;

import java.util.List;
import java.util.stream.Collectors;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.nodes.OPCNode;

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

	@PropertyIdentifier(type = String.class)
	public static final String URI_KEY = "uri";

	@Override
	@Getter(URI_KEY)
	public String getUri();

	@Setter(URI_KEY)
	public void setUri(String anUri);

	@PropertyIdentifier(type = Integer.class)
	public static final String INDEX_KEY = "index";

	@Getter(INDEX_KEY)
	public Integer getIndex();

	@Setter(INDEX_KEY)
	public void setIndex(Integer anIndex);

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
	public List<OPCNode<?>> getAllNodes();

	@Adder(OPC_NODES_KEY)
	@PastingPoint
	public void addToNamespace(OPCNode aNode);

	@Remover(OPC_NODES_KEY)
	public void removeFromNamespace(OPCNode aNode);

	public List<OPCNode<?>> getRootNodes();

	public static abstract class OPCNamespaceImpl extends OPCObjectImpl implements OPCNamespace {

		@Override
		public OPCServer getResourceData() {
			return getServer();
		}

		@Override
		public String getDisplayableName() {
			return getUri() + ":" + getIndex();
		}

		@Override
		public String getDisplayableDescription() {
			return "OPCNamespace: " + getDisplayableName();
		}

		@Override
		public List<OPCNode<?>> getRootNodes() {
			return getAllNodes().stream().filter(OPCNode::isRoot).collect(Collectors.toList());
		}

	}

}
