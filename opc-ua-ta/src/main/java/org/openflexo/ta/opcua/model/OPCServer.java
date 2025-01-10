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

package org.openflexo.ta.opcua.model;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.opcua.rm.OPCServerResource;

/**
 * Represents an OPC UA server<br>
 * 
 * @author sylvain, luka
 *
 */
@ModelEntity
@ImplementationClass(value = OPCServer.OPCServerImpl.class)
public interface OPCServer extends OPCObject, ResourceData<OPCServer> {

	@PropertyIdentifier(type = UANode.class, cardinality = Cardinality.LIST)
	public static final String NODES_KEY = "nodes";

	/**
	 * Return all {@link UANode} defined in this {@link OPCServer}
	 * 
	 * @return
	 */
	@Getter(value = NODES_KEY, cardinality = Cardinality.LIST, inverse = UANode.OPC_SERVER_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<UANode> getNodes();

	@Adder(NODES_KEY)
	@PastingPoint
	public void addToNodes(UANode aNode);

	@Remover(NODES_KEY)
	public void removeFromNodes(UANode aNode);

	@Override
	public OPCServerResource getResource();

	/**
	 * Default base implementation for {@link OPCServer}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class OPCServerImpl extends OPCObjectImpl implements OPCServer {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(OPCObjectImpl.class.getPackage().getName());

		@Override
		public OPCServer getResourceData() {
			return this;
		}

		@Override
		public OPCServerResource getResource() {
			return (OPCServerResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public String toString() {
			return super.toString() + "-" + getResource();
		}

	}

}
