package org.openflexo.ta.opcua.model;

import java.util.logging.Logger;

import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.pamela.PamelaMetaModelLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.pamela.factory.PamelaModelFactory;
import org.openflexo.ta.opcua.rm.OPCServerResource;

/**
 * A {@link PamelaModelFactory} used to manage a {@link OPCServer}<br>
 * One instance of this class should be used for each {@link OPCServerResource}
 *
 * @author sylvain,luka
 *
 */
public class OPCModelFactory extends PamelaModelFactory implements PamelaResourceModelFactory<OPCServerResource> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OPCModelFactory.class.getPackage().getName());

	private final OPCServerResource resource;
	private PamelaResourceImpl.IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	public OPCModelFactory(OPCServerResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(PamelaMetaModelLibrary.retrieveMetaModel(OPCServer.class));
		this.resource = resource;
		setEditingContext(editingContext);
	}

	@Override
	public OPCServerResource getResource() {
		return resource;
	}

	public OPCServer makeOPCServer() {
		return newInstance(OPCServer.class);
	}

	public OPCNamespace makeOPCNamespace(OPCServer server, Integer anIndex) {
		OPCNamespace returned = newInstance(OPCNamespace.class);
		returned.setIndex(anIndex);
		server.addToNamespaces(returned);
		return returned;
	}

	public OPCVariableNode makeOPCVariableNode(OPCNamespace namespace, OPCNode parent, String identifier, String name) {
		OPCVariableNode returned = newInstance(OPCVariableNode.class);
		returned.setParent(parent);
		returned.setIdentifier(identifier);
		returned.setName(name);
		namespace.addToNamespace(returned);
		return returned;
	}

	public OPCFolderNode makeOPCFolderNode(OPCNamespace namespace, OPCNode parent, String identifier, String name) {
		OPCFolderNode returned = newInstance(OPCFolderNode.class);
		returned.setParent(parent);
		returned.setIdentifier(identifier);
		returned.setName(name);
		namespace.addToNamespace(returned);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {
		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new PamelaResourceImpl.IgnoreLoadingEdits(resource));
			System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {
		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

}
