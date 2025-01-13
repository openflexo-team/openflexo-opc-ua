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

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactories;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.ta.opcua.fml.binding.OPCUABindingFactory;
import org.openflexo.ta.opcua.rm.OPCServerResourceFactory;
import org.openflexo.ta.opcua.rm.OPCServerResourceRepository;

/**
 * A {@link TechnologyAdapter} dedicated to manage OPC-UA technology
 * 
 * @author sylvain
 * 
 */
@DeclareModelSlots({ OPCServerModelSlot.class })
// You might declare your own types here
// @DeclareTechnologySpecificTypes({ YourCustomType.class })
@DeclareResourceFactories({ OPCServerResourceFactory.class })
public class OPCUATechnologyAdapter extends TechnologyAdapter<OPCUATechnologyAdapter> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OPCUATechnologyAdapter.class.getPackage().getName());

	private static final OPCUABindingFactory BINDING_FACTORY = new OPCUABindingFactory();

	@Override
	public String getName() {
		return "OPC-UA technology adapter";
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/OPCUATechnologyAdapter";
	}

	@Override
	public void ensureAllRepositoriesAreCreated(FlexoResourceCenter<?> rc) {
		super.ensureAllRepositoriesAreCreated(rc);
		getOPCServerResourceRepository(rc);

	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public OPCUABindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public String getIdentifier() {
		return "OPCUA";
	}

	public OPCServerResourceFactory getOPCServerResourceFactory() {
		return getResourceFactory(OPCServerResourceFactory.class);
	}

	@SuppressWarnings("unchecked")
	public <I> OPCServerResourceRepository<I> getOPCServerResourceRepository(FlexoResourceCenter<I> resourceCenter) {
		OPCServerResourceRepository<I> returned = resourceCenter.retrieveRepository(OPCServerResourceRepository.class, this);
		if (returned == null) {
			returned = OPCServerResourceRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, OPCServerResourceRepository.class, this);
		}
		return returned;
	}

}
