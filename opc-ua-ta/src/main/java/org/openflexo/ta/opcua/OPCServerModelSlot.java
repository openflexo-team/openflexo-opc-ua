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
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.fml.OPCNamespaceActorReference;
import org.openflexo.ta.opcua.fml.OPCNamespaceRole;
import org.openflexo.ta.opcua.fml.OPCNodeActorReference;
import org.openflexo.ta.opcua.fml.OPCNodeRole;
import org.openflexo.ta.opcua.fml.editionaction.AddOPCNode;
import org.openflexo.ta.opcua.fml.editionaction.SelectOPCNode;
import org.openflexo.ta.opcua.fml.editionaction.SelectUniqueOPCNode;
import org.openflexo.ta.opcua.model.OPCServer;

/**
 * A ModelSlot allowing access to a OPC-UA Server
 * 
 * @author sylvain, luka
 * 
 */
@DeclareFlexoRoles({ OPCNamespaceRole.class, OPCNodeRole.class })
@DeclareEditionActions({ AddOPCNode.class })
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
