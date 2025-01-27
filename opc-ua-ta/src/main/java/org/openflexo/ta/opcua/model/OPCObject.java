package org.openflexo.ta.opcua.model;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.ta.opcua.OPCUATechnologyAdapter;

/**
 * Common API for all objects involved in OPC UA server
 *
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(value = OPCObject.OPCObjectImpl.class)
public interface OPCObject extends InnerResourceData<OPCServer>, TechnologyObject<OPCUATechnologyAdapter> {

	public OPCModelFactory getFactory();

	public String getUri();

	/**
	 * Default base implementation for {@link OPCObject}
	 *
	 * @author sylvain,luka
	 *
	 */
	public static abstract class OPCObjectImpl extends FlexoObject.FlexoObjectImpl implements OPCObject {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(OPCObjectImpl.class.getPackage().getName());

		@Override
		public OPCUATechnologyAdapter getTechnologyAdapter() {
			if (getResourceData() != null && getResourceData().getResource() != null) {
				return getResourceData().getResource().getTechnologyAdapter();
			}
			return null;
		}

		@Override
		public OPCModelFactory getFactory() {
			return getResourceData().getResource().getFactory();
		}

	}

}
