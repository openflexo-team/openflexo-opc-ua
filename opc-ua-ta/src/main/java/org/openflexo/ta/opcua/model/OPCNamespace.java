package org.openflexo.ta.opcua.model;

import java.util.List;
import java.util.stream.Collectors;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.nodes.OPCNode;

/**
 * Represents a namespace within an OPC UA server's address space in OpenFlexo.
 *
 * <p>For an overview of OPC UA integration in OpenFlexo, see {@link org.openflexo.ta.opcua}.</p>
 *
 * <p>The {@code OPCNamespace} class encapsulates a specific namespace identified
 * by a unique URI and index, organizing a collection of {@link OPCNode} instances
 * that belong to this namespace.</p>
 *
 * <h2>Key Responsibilities</h2>
 * <ul>
 *   <li>Maintaining the namespace URI and index, ensuring unique identification
 *       of nodes within the OPC UA server.</li>
 *   <li>Organizing and managing {@link OPCNode} instances associated with this
 *       namespace.</li>
 *   <li>Facilitating browsing and interaction with nodes in the namespace.</li>
 * </ul>
 *
 * <p>For more details on OPC UA namespaces and their role in creating unique
 * identifiers across different naming authorities, refer to the
 * <a href="https://reference.opcfoundation.org/DI/v102/docs/11.2">OPC UA Devices
 * Specification</a>.</p>
 *
 * @see OPCNode
 * @see OPCServer
 */
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
