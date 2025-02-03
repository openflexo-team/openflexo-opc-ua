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

package org.openflexo.ta.opcua.fml;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.rm.OPCServerResource;

/**
 * Implements {@link ActorReference} for {@link OPCNode} object
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(OPCNodeActorReference.OPCNodeActorReferenceImpl.class)
@XMLElement
@FML("OPCNodeActorReference")
public interface OPCNodeActorReference extends ActorReference<OPCNode> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public abstract static class OPCNodeActorReferenceImpl extends ActorReferenceImpl<OPCNode> implements OPCNodeActorReference {

		private static final Logger logger = FlexoLogger.getLogger(OPCNodeActorReference.class.getPackage().toString());

		private OPCNode object;
		private String objectURI;

		public OPCServer getOPCServer() {
			if (getOPCServerResource() != null) {
				try {
					return getOPCServerResource().getResourceData();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				} catch (FlexoException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public OPCServerResource getOPCServerResource() {
			ModelSlotInstance<?, ?> msInstance = getModelSlotInstance();
			if (msInstance != null && msInstance.getResource() instanceof OPCServerResource) {
				return (OPCServerResource) msInstance.getResource();
			}
			return null;
		}

		@Override
		public OPCNode getModellingElement(boolean forceLoading) {

			// TODO : retrieve Milo node from connection
			// On se dit avec Luka que le client (ou la connexion OPCUA) doit etre dans un OPCServer

			if (object == null && objectURI != null && getOPCServer() != null && getOPCServer().getNamespaces() != null) {
				// TODO : make sure it's reasonable to search all nodes here
				for (OPCNamespace namespace : getOPCServer().getNamespaces()) {
					for (OPCNode node : namespace.getAllNodes()) {
						if (node.getUri().equals(objectURI))
							return node;
					}
				}
			}
			if (object == null) {
				logger.warning("Could not retrieve object " + objectURI);
			}
			return object;

		}

		@Override
		public void setModellingElement(OPCNode object) {
			this.object = object;
			if (object != null) {
				objectURI = object.getUri();
			}
		}

		@Override
		public String getObjectURI() {
			if (object != null) {
				return "" + object.getUri();
			}
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}

	}

}
