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

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.OPCServerModelSlot;
import org.openflexo.ta.opcua.model.UANode;
import org.openflexo.ta.opcua.model.OPCServer;

@ModelEntity
@ImplementationClass(AddUANode.AddXXLineImpl.class)
@XMLElement
@FML("AddUANode")
public interface AddUANode extends OPCUAAction<UANode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String LINE_NUMBER_KEY = "lineNumber";

	@Getter(value = LINE_NUMBER_KEY)
	@XMLAttribute
	public DataBinding<Integer> getLineNumber();

	@Setter(LINE_NUMBER_KEY)
	public void setLineNumber(DataBinding<Integer> lineNumber);

	public static abstract class AddXXLineImpl extends TechnologySpecificActionDefiningReceiverImpl<OPCServerModelSlot, OPCServer, UANode>
			implements AddUANode {

		private static final Logger logger = Logger.getLogger(AddUANode.class.getPackage().getName());

		private DataBinding<Integer> lineNumber;

		@Override
		public Type getAssignableType() {
			return UANode.class;
		}

		@Override
		public UANode execute(RunTimeEvaluationContext evaluationContext) {

			UANode line = null;

			OPCServer resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					Integer lineNumber = getLineNumber().getBindingValue(evaluationContext);
					if (lineNumber != null) {
						System.out.println("TODO: addLine not implemented");
						resourceData.setIsModified();
						resourceData.setModified(true);

					}
					else {
						logger.warning("Create a row requires a index");
					}
				}
				else {
					logger.warning("Cannot add line in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			return line;

		}

		@Override
		public DataBinding<Integer> getLineNumber() {
			if (lineNumber == null) {
				lineNumber = new DataBinding<>(this, Integer.class, DataBinding.BindingDefinitionType.GET);
				lineNumber.setBindingName("lineNumber");
			}
			return lineNumber;
		}

		@Override
		public void setLineNumber(DataBinding<Integer> lineNumber) {
			if (lineNumber != null) {
				lineNumber.setOwner(this);
				lineNumber.setDeclaredType(Integer.class);
				lineNumber.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				lineNumber.setBindingName("lineNumber");
			}
			this.lineNumber = lineNumber;
		}

	}

	@DefineValidationRule
	public static class LineNumberBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddUANode> {
		public LineNumberBindingIsRequiredAndMustBeValid() {
			super("'lineNumber'_binding_is_required_and_must_be_valid", AddUANode.class);
		}

		@Override
		public DataBinding<Integer> getBinding(AddUANode object) {
			return object.getLineNumber();
		}

	}

}
