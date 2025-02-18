/**

 * <p>This package defines the OPC UA Technology Adapter, allowing OpenFlexo to connect to
 * and interact with OPC UA servers using the <a href="https://github.com/eclipse/milo">Eclipse Milo</a> library.
 * It manages the connection, discovery, and representation of OPC UA data within OpenFlexo's modeling environment.</p>
 *
 * <h2>Core Components</h2>
 * <p>Several key components structure the integration:</p>
 *
 * <ul>
 *   <li><b>ModelSlot:</b> The {@link org.openflexo.ta.opcua.OPCServerModelSlot} defines how an OPC UA server
 *       is accessed within an OpenFlexo model.</li>
 *   <li><b>Resource:</b> The {@link org.openflexo.ta.opcua.rm.OPCServerResource} handles the persistence of OPC UA server configurations.</li>
 *   <li><b>ResourceData:</b> The {@link org.openflexo.ta.opcua.model.OPCServer} represents an OPC UA server instance,
 *       storing connection details and maintaining the {@link org.eclipse.milo.opcua.sdk.client.OpcUaClient}.</li>
 * </ul>
 *
 * <h2>OPC UA Model Representation</h2>
 * <p>The model follows the structure of an OPC UA address space:</p>
 *
 * <ul>
 *   <li><b>Namespaces:</b> Represented by {@link org.openflexo.ta.opcua.model.OPCNamespace}, identified by a URI.
 *       Each namespace contains a list of nodes.</li>
 *   <li><b>Nodes:</b> Defined by {@link org.openflexo.ta.opcua.model.nodes.OPCNode}, representing elements in the OPC
 *       UA address space. Nodes belong to a namespace, have a unique identifier, and maintain references to their
 *       parent (if applicable). Examples of specific node types</b> include
 *       {@link org.openflexo.ta.opcua.model.nodes.OPCVariableNode} and
 *       {@link org.openflexo.ta.opcua.model.nodes.OPCObjectNode}.</li>
 * </ul>
 *
 * <h2>Runtime Behavior</h2>
 * <p>When an {@link org.openflexo.ta.opcua.model.OPCServer} is first created, it automatically performs a discovery
 * of the server's structure, identifying namespaces and nodes. Nodes store minimal persistent information,
 * while additional details (such as data type and value) are dynamically retrieved from the corresponding
 * {@link org.eclipse.milo.opcua.sdk.client.nodes.UaNode}.</p>
 *
 * <p>Binding and interaction with OPC UA data is managed by the {@link org.openflexo.ta.opcua.fml.binding.OPCUABindingFactory},
 * enabling OpenFlexo expressions to access node values and properties.</p>
 *
 * <h2>References</h2>
 * <ul>
 *   <li><a href="https://reference.opcfoundation.org/v104/Core/docs/Part3/">OPC UA Part 3: Address Space Model</a></li>
 *   <li><a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/">OPC UA Part 4: Services</a></li>
 *   <li><a href="https://reference.opcfoundation.org/GDS/v105/docs/">OPC UA Part 12: Discovery</a></li>
 * </ul>
 *
 * @see org.openflexo.ta.opcua.OPCUATechnologyAdapter
 * @see org.openflexo.ta.opcua.OPCServerModelSlot
 * @see org.openflexo.ta.opcua.rm.OPCServerResource
 * @see org.openflexo.ta.opcua.model.OPCServer
 * @see org.openflexo.ta.opcua.model.OPCNamespace
 * @see org.openflexo.ta.opcua.model.nodes.OPCNode
 *
 * @author Luka
 */
package org.openflexo.ta.opcua;