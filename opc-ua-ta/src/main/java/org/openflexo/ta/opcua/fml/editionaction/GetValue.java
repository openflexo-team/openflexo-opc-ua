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

package org.openflexo.ta.opcua.fml.editionaction;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.OPCServerModelSlot;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.model.nodes.OPCVariableNode;

@ModelEntity
@ImplementationClass(GetValue.GetValueImpl.class)
@XMLElement
@FML("GetValue")
public interface GetValue extends OPCAction<OPCNode, Object> {

	public static abstract class GetValueImpl extends TechnologySpecificActionDefiningReceiverImpl<OPCServerModelSlot, OPCNode, Object>
			implements GetValue {

		private static final Logger logger = Logger.getLogger(GetValue.class.getPackage().getName());

		@Override
		public Type getAssignableType() {
			return OPCNode.class;
		}

		@Override
		public Object execute(RunTimeEvaluationContext evaluationContext) {

			OPCNode<?> node = getReceiver(evaluationContext);

			if (node instanceof OPCVariableNode) {
				try {
					return ((OPCVariableNode) node).getNode().readValue().getValue().getValue();
				} catch (Exception e) {
					logger.warning(e.getMessage());
				}
			}

			logger.warning("Attempting to read value from " + node.getUri() + " (" + node.getClass() + ")");

			return null;

		}

	}

}
