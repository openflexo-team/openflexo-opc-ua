package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.ta.opcua.OPCUATechnologyAdapter;

import java.util.logging.Logger;

/**
 * Common API for all objects involved in OPC UA server
 *
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface OPCObject extends InnerResourceData<OPCServer>, TechnologyObject<OPCUATechnologyAdapter> {

    public OPCModelFactory getFactory();

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
