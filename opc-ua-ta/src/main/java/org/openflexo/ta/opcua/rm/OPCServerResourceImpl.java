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

package org.openflexo.ta.opcua.rm;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.resource.*;
import org.openflexo.ta.opcua.model.OPCModelFactory;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.toolbox.FileUtils;

/**
 * Default implementation for a resource storing a {@link OPCServer}
 * 
 * @author sylvain
 *
 */
public abstract class OPCServerResourceImpl extends PamelaResourceImpl<OPCServer, OPCModelFactory> implements OPCServerResource {

	private static final Logger logger = Logger.getLogger(OPCServerResourceImpl.class.getPackage().getName());

	public static final String HOSTNAME = "hostname";
	public static final String BIND_PORT = "bindport";
	public static final String APPLICATION_NAME = "applicationname";
	public static final String BIND_ADDRESS = "bindaddress";

	/**
	 * Convenient method to retrieve resource data
	 * 
	 * @return
	 */
	@Override
	public OPCServer getOPCServer() {
		try {
			return getResourceData();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected OPCServer performLoad() throws IOException, Exception {
		if (getFlexoIOStreamDelegate() == null) {
			throw new IOFlexoException("Cannot load document with this IO/delegate: " + getIODelegate());
		}

		System.out.println("On vient charger la resource");

		notifyResourceWillLoad();

		OPCServer returned = null;
		try {
			returned = load(getFlexoIOStreamDelegate());
			getInputStream().close();
		} catch (IOException e) {
			throw new IOFlexoException(e);
		}

		if (returned == null) {
			logger.warning("Cannot retrieve resource data from serialization artifact " + getIODelegate());
			return null;
		}

		notifyResourceLoaded();

		return returned;
	}

	/**
	 * Return type of {@link ResourceData}
	 */
	@Override
	public Class<OPCServer> getResourceDataClass() {
		return OPCServer.class;
	}

	/**
	 * Resource saving safe implementation<br>
	 * Initial resource is first copied, then we write in a temporary file, renamed at the end when the serialization has been successfully
	 * performed
	 */
	@Override
	protected void performSave(boolean clearIsModified) throws SaveResourceException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new SaveResourceException(getIODelegate());
		}

		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getIODelegate().getSerializationArtefact());
		}

		if (getFlexoIOStreamDelegate() instanceof FileIODelegate) {
			File temporaryFile = null;
			try {
				File fileToSave = ((FileIODelegate) getFlexoIOStreamDelegate()).getFile();
				// Make local copy
				makeLocalCopy(fileToSave);
				// Using temporary file
				temporaryFile = ((FileIODelegate) getIODelegate()).createTemporaryArtefact(".txt");
				if (logger.isLoggable(Level.FINE)) {
					logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
				}
				try (FileOutputStream fos = new FileOutputStream(temporaryFile)) {
					write(fos);
				}
				System.out.println("Renamed " + temporaryFile + " to " + fileToSave);
				FileUtils.rename(temporaryFile, fileToSave);
			} catch (IOException e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + getIODelegate().getSerializationArtefact());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getIODelegate(), e);
			}
		}
		else {
			write(getOutputStream());
		}

		getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
		if (clearIsModified) {
			notifyResourceStatusChanged();
		}
	}

	/**
	 * {@link ResourceData} internal loading implementation<br>
	 * (trivial here)
	 * 
	 * @param ioDelegate
	 * @return
	 * @throws IOException
	 */
	private <I> OPCServer load(StreamIODelegate<I> ioDelegate) throws IOException {

		OPCServer returned = getFactory().makeOPCServer();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(ioDelegate.getInputStream()))) {
			String nextLine = br.readLine();
			while (nextLine != null) {
				String[] tokens = nextLine.split("=");
				if (tokens.length == 2) {
					String key = tokens[0].trim().toLowerCase();
					String value = tokens[1].trim();
					// System.out.println("key=" + key + " value=" + value);
					switch (key) {
						case HOSTNAME:
							returned.setHostname(value);
							break;
						case BIND_PORT:
							returned.setBindPort(Integer.valueOf(value));
							break;
						case APPLICATION_NAME:
							returned.setApplicationName(value);
							break;
						case BIND_ADDRESS:
							returned.setBindAddress(value);
							break;
					}
				}
				nextLine = br.readLine();
			}
		}

		return returned;
	}

	@Override
	public void stopDeserializing() {
		super.stopDeserializing();
		// TODO : Discovery & populate OPCServer with its nodes. Here? Really?

		// TODO a voir
		getLoadedResourceData().performDiscovery();
	}

	/**
	 * {@link ResourceData} internal saving implementation<br>
	 * (trivial here)
	 * 
	 * @throws IOException
	 */
	private void write(OutputStream out) throws SaveResourceException {
		logger.info("Writing " + getIODelegate().getSerializationArtefact());
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out))) {
			bw.write(HOSTNAME + "=" + getOPCServer().getHostname());
			bw.newLine();
			bw.write(BIND_PORT + "=" + getOPCServer().getBindPort());
			bw.newLine();
			bw.write(APPLICATION_NAME + "=" + getOPCServer().getApplicationName());
			bw.newLine();
			bw.write(BIND_ADDRESS + "=" + getOPCServer().getBindAddress());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		logger.info("Wrote " + getIODelegate().getSerializationArtefact());
	}

}
