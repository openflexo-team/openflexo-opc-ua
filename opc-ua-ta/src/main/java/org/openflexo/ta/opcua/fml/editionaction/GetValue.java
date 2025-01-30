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

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.OPCServerModelSlot;
import org.openflexo.ta.opcua.model.nodes.OPCNode;

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

			OPCNode node = getReceiver(evaluationContext);

			System.out.println("On cherche a obtenir la valeur de " + node);

			System.out.println("Prout: " + node.getNode());

			if (node.getNode() instanceof UaVariableNode) {

				UaVariableNode miloNode = (UaVariableNode) node.getNode();

				Object value;
				try {
					System.out.println("miloNode.readValue()=" + miloNode.readValue());
					System.out.println("miloNode.readValue().getValue()=" + miloNode.readValue().getValue());
					System.out.println("miloNode.readValue().getValue().getValue()=" + miloNode.readValue().getValue().getValue());
					value = miloNode.readValue().getValue().getValue();
					System.out.println("Read value: " + value);
				} catch (UaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				System.out.println("Je ne sais pas trop comment gerer " + node.getNode());
			}
			return null;

		}

	}

}
