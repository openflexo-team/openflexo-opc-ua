package org.openflexo.ta.opcua.model;

import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.pamela.PamelaMetaModelLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.pamela.factory.PamelaModelFactory;
import org.openflexo.ta.opcua.rm.OPCServerResource;

import java.util.logging.Logger;

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

    public OPCNamespace makeOPCNamespace() {
        return newInstance(OPCNamespace.class);
    }
    public OPCNamespace makeOPCNamespace(OPCServer server) {
        OPCNamespace returned = makeOPCNamespace();
        returned.setServer(server); // TODO : is it needed? Or does inverse annotation handles it
        server.addToNamespaces(returned);
        return returned;
    }

    public OPCNode makeUANode(String value) {
        OPCNode returned = newInstance(OPCNode.class);
        // TODO : initialize node here
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
