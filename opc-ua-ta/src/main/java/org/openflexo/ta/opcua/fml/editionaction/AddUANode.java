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
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.OPCServerModelSlot;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.ta.opcua.model.nodes.OPCNode;

@ModelEntity
@ImplementationClass(AddUANode.AddUANodeImpl.class)
@XMLElement
@FML("AddUANode")
public interface AddUANode extends OPCUAAction<OPCNode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String RELATIVE_PATH_KEY = "relativePath";

	@Getter(value = RELATIVE_PATH_KEY)
	@XMLAttribute
	public DataBinding<String> getRelativePath();

	@Setter(RELATIVE_PATH_KEY)
	public void setRelativePath(DataBinding<String> relativePath);

	public static abstract class AddUANodeImpl extends TechnologySpecificActionDefiningReceiverImpl<OPCServerModelSlot, OPCServer, OPCNode>
			implements AddUANode {

		private static final Logger logger = Logger.getLogger(AddUANode.class.getPackage().getName());

		private DataBinding<String> relativePath;

		@Override
		public Type getAssignableType() {
			return OPCNode.class;
		}

		@Override
		public OPCNode execute(RunTimeEvaluationContext evaluationContext) {

			OPCNode line = null;

			OPCServer resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String relativePath = getRelativePath().getBindingValue(evaluationContext);
					if (relativePath != null) {
						System.out.println("TODO: addUANode not implemented");
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
		public DataBinding<String> getRelativePath() {
			if (relativePath == null) {
				relativePath = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				relativePath.setBindingName("relativePath");
			}
			return relativePath;
		}

		@Override
		public void setRelativePath(DataBinding<String> relativePath) {
			if (relativePath != null) {
				relativePath.setOwner(this);
				relativePath.setDeclaredType(String.class);
				relativePath.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				relativePath.setBindingName("relativePath");
			}
			this.relativePath = relativePath;
		}

	}

	@DefineValidationRule
	public static class LineNumberBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddUANode> {
		public LineNumberBindingIsRequiredAndMustBeValid() {
			super("'relativePath'_binding_is_required_and_must_be_valid", AddUANode.class);
		}

		@Override
		public DataBinding<String> getBinding(AddUANode object) {
			return object.getRelativePath();
		}

	}

}
