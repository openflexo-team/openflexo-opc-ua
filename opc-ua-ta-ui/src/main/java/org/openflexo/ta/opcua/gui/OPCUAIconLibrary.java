/**
 * 
 * Copyright (c) 2018, Openflexo
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

package org.openflexo.ta.opcua.gui;

import java.util.logging.Logger;

import javax.swing.*;

import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.ta.opcua.model.OPCNamespace;
import org.openflexo.ta.opcua.model.OPCObject;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.ta.opcua.model.nodes.OPCNode;
import org.openflexo.ta.opcua.model.nodes.OPCObjectNode;

public class OPCUAIconLibrary {

	private static final Logger logger = Logger.getLogger(OPCUAIconLibrary.class.getPackage().getName());

	public static final ImageIconResource OPC_UA_TA_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/opc-ua-ta-32x32.png"));

	public static final ImageIconResource OPC_UA_TA_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/opc-ua-ta-16x16.png"));
	public static final ImageIconResource OPC_SERVER_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OPCServer.png"));
	public static final ImageIconResource OPC_NODE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OPCNode.png"));
	public static final ImageIconResource OPC_FOLDER_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OPCFolder.png"));

	public static final ImageIconResource OPC_NAMESPACE_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OPCNamespace-64x64.png"));
	public static final ImageIconResource OPC_NAMESPACE_MEDIUM_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OPCNamespace-32x32.png"));
	public static final ImageIconResource OPC_NAMESPACE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OPCNamespace.png"));

	public static ImageIcon iconForObject(Class<? extends OPCObject> objectClass) {
		if (OPCServer.class.isAssignableFrom(objectClass)) {
			return OPC_SERVER_ICON;
		}
		else if (OPCNamespace.class.isAssignableFrom(objectClass)) {
			return OPC_NAMESPACE_ICON;
		}
		else if (OPCObjectNode.class.isAssignableFrom(objectClass)) {
			return OPC_FOLDER_ICON;
		}
		else if (OPCNode.class.isAssignableFrom(objectClass)) {
			return OPC_NODE_ICON;
		}
		logger.warning("No icon for this class " + objectClass);
		return null;
	}
}
