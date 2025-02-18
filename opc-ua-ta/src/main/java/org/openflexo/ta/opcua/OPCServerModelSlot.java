/**
 * 
 * Copyright (c) 2025, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.ta.opcua;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.*;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.fml.OPCNamespaceActorReference;
import org.openflexo.ta.opcua.fml.OPCNamespaceRole;
import org.openflexo.ta.opcua.fml.OPCNodeActorReference;
import org.openflexo.ta.opcua.fml.OPCNodeRole;
import org.openflexo.ta.opcua.fml.editionaction.AddOPCNode;
import org.openflexo.ta.opcua.fml.editionaction.GetValue;
import org.openflexo.ta.opcua.fml.editionaction.SelectOPCNode;
import org.openflexo.ta.opcua.fml.editionaction.SelectUniqueOPCNode;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

/**
 * Represents a model slot for integrating an OPC UA server within OpenFlexo.
 *
 * <p>The {@code OPCServerModelSlot} acts as a bridge between OpenFlexo and an OPC UA server,
 * enabling models to interact with real-time data using the
 * <a href="https://github.com/eclipse/milo">Eclipse Milo</a> library.</p>
 *
 * <p>For an overview of the OPC UA integration in OpenFlexo, see {@link org.openflexo.ta.opcua}.</p>
 *
 * <h2>Purpose</h2>
 * <p>In OpenFlexo, a <i>ModelSlot</i> defines how an external data source is accessed within a Virtual Model.
 * The {@code OPCServerModelSlot} provides access to data through an OPC UA server.</p>
 *
 * <h2>OPC UA Integration</h2>
 * <p>Through this ModelSlot, OpenFlexo can:</p>
 * <ul>
 *   <li>Connect to OPC UA servers via {@link OpcUaClient}.</li>
 *   <li>Browse and retrieve server metadata dynamically.</li>
 *   <li>Access real-time values of OPC UA nodes.</li>
 * </ul>
 *
 * <h2>Interaction with the OPC UA Model</h2>
 * <p>The OPC UA server structure is represented as follows:</p>
 * <ul>
 *   <li>{@link OPCServer} - The connected OPC UA server.</li>
 *   <li>{@link OPCNamespace} - A namespace containing OPC UA nodes.</li>
 *   <li>{@link OPCNode} - A node representing variables, objects, or folders.</li>
 * </ul>
 *
 * <h2>FML (Flexo Modeling Language) Integration</h2>
 * <p>The {@code OPCServerModelSlot} enables Flexo Modeling Language (FML) scripts to interact with OPC UA data.
 * It supports:</p>
 * <ul>
 *   <li>Roles: {@link OPCNamespaceRole}, {@link OPCNodeRole}.</li>
 *   <li>Actions: {@link GetValue} (read values from variable nodes).</li>
 *   <li>Fetch requests: {@link SelectOPCNode} (search for nodes).</li>
 * </ul>
 *
 * <p>For details on how OpenFlexo structures OPC UA data, see {@link org.openflexo.ta.opcua}.</p>
 *
 * @author Sylvain, Luka
 */
@DeclareFlexoRoles({ OPCNamespaceRole.class, OPCNodeRole.class })
@DeclareEditionActions({ AddOPCNode.class, GetValue.class })
@DeclareFetchRequests({ SelectUniqueOPCNode.class, SelectOPCNode.class })
@DeclareActorReferences({ OPCNamespaceActorReference.class, OPCNodeActorReference.class })
@ModelEntity
@ImplementationClass(OPCServerModelSlot.OPCServerModelSlotImpl.class)
@XMLElement
@FML("OPCServerModelSlot")
public interface OPCServerModelSlot extends FreeModelSlot<OPCServer> {

	public static abstract class OPCServerModelSlotImpl extends FreeModelSlotImpl<OPCServer> implements OPCServerModelSlot {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(OPCServerModelSlot.class.getPackage().getName());

		@Override
		public Class<OPCUATechnologyAdapter> getTechnologyAdapterClass() {
			return OPCUATechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OPCNodeRole.class.isAssignableFrom(patternRoleClass)) {
				return "node";
			}
			return null;
		}

		@Override
		public Type getType() {
			return OPCServer.class;
		}

		@Override
		public OPCUATechnologyAdapter getModelSlotTechnologyAdapter() {
			return (OPCUATechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
