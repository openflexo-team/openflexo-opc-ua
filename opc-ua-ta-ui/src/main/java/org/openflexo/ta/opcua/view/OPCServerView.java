/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.ta.opcua.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.container.FIBTab;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent.CustomComponentParameter;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.gina.utils.FIBInspector;
import org.openflexo.icon.UtilsIconLibrary;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.ta.opcua.OPCUATechnologyAdapter;
import org.openflexo.ta.opcua.model.OPCObject;
import org.openflexo.ta.opcua.model.OPCServer;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.ModuleView;
import org.openflexo.view.SelectionSynchronizedFIBView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for an XSD meta model<br>
 * Underlying representation is supported by OntologyView implementation.
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class OPCServerView extends SelectionSynchronizedFIBView implements ModuleView<OPCServer> {

	protected static final Logger logger = Logger.getLogger(OPCServerView.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/OPCServerView.fib");
	// public static final Resource XMLType_FIB_FILE = ResourceLocator.locateResource("Fib/FIBPanelXMLType.fib");
	// public static final Resource XMLProperty_FIB_FILE = ResourceLocator.locateResource("Fib/FIBPanelXMLProperty.fib");

	protected final FlexoController controller;

	protected OPCServer representedObject;

	protected final FlexoPerspective perspective;

	// Properties used to iteract with the view

	protected String filteredName;
	protected final List<OPCObject> matchingValues;
	protected OPCObject selectedValue;
	protected boolean isSearching = false;
	private boolean allowsSearch = true;

	public OPCServerView(OPCServer object, FlexoController controller, FlexoPerspective perspective) {
		super(null, controller, FIB_FILE, controller.getTechnologyAdapter(OPCUATechnologyAdapter.class).getLocales());
		this.controller = controller;
		this.representedObject = object;
		this.perspective = perspective;
		setDataObject(this);
		matchingValues = new ArrayList<>();
	}

	@Override
	public OPCServer getRepresentedObject() {
		return representedObject;
	}

	public OPCServer getOpcServer() {
		return getRepresentedObject();
	}

	/**
	 * Retrieve the browser widget so that it can be queried
	 * 
	 * @return
	 */
	protected JFIBBrowserWidget retrieveFIBBrowserWidget() {
		if (getFIBComponent() instanceof FIBContainer) {
			List<FIBComponent> listComponent = ((FIBContainer) getFIBComponent()).getAllSubComponents();
			for (FIBComponent c : listComponent) {
				if (c instanceof FIBBrowser) {
					return (JFIBBrowserWidget) getFIBController().viewForComponent(c);
				}
			}
		}
		return null;
	}

	/**
	 * This method is used to retrieve all potential values when implementing completion<br>
	 * Completion will be performed on that selectable values<br>
	 * Default implementation is to iterate on all values of browser, please take care to infinite loops.<br>
	 * 
	 * Override when required
	 */
	protected Vector<OPCObject> getAllSelectableValues() {
		Vector<OPCObject> returned = new Vector<>();
		JFIBBrowserWidget browserWidget = retrieveFIBBrowserWidget();
		if (browserWidget == null) {
			return null;
		}
		Iterator<Object> it = browserWidget.getBrowserModel().recursivelyExploreModelToRetrieveContents();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof OPCObject) {
				returned.add((OPCObject) o);
			}
		}
		// System.out.println("Returned: (" + returned.size() + ") " + returned);
		return returned;
	}

	public String getFilteredName() {
		return filteredName;
	}

	public void setFilteredName(String filteredName) {
		this.filteredName = filteredName;
	}

	public List<OPCObject> getMatchingValues() {
		return matchingValues;
	}

	public void search() {
		if (StringUtils.isNotEmpty(getFilteredName())) {
			logger.info("Searching " + getFilteredName());
			matchingValues.clear();
			for (OPCObject o : getAllSelectableValues()) {
				if (o.getDisplayableName().indexOf(getFilteredName()) > -1) {
					if (!matchingValues.contains(o)) {
						matchingValues.add(o);
					}
				}
			}

			isSearching = true;
			getPropertyChangeSupport().firePropertyChange("isSearching", false, true);
			getPropertyChangeSupport().firePropertyChange("matchingValues", null, matchingValues);

			if (matchingValues.size() == 1) {
				selectValue(matchingValues.get(0));
			}
		}

	}

	public boolean getAllowsSearch() {
		return allowsSearch;
	}

	@CustomComponentParameter(name = "allowsSearch", type = CustomComponentParameter.Type.OPTIONAL)
	public void setAllowsSearch(boolean allowsSearch) {
		this.allowsSearch = allowsSearch;
	}

	public void dismissSearch() {
		logger.info("Dismiss search");

		isSearching = false;
		getPropertyChangeSupport().firePropertyChange("isSearching", true, false);
	}

	public boolean isSearching() {
		return isSearching;
	}

	public OPCObject getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(OPCObject selectedValue) {
		OPCObject oldSelected = this.selectedValue;
		this.selectedValue = selectedValue;
		getPropertyChangeSupport().firePropertyChange("selectedValue", oldSelected, selectedValue);
	}

	public void selectValue(OPCObject selectedValue) {
		getFIBController().selectionCleared();
		getFIBController().objectAddedToSelection(selectedValue);
	}

	public ImageIcon getCancelIcon() {
		return UtilsIconLibrary.CANCEL_ICON;
	}

	public ImageIcon getSearchIcon() {
		return UtilsIconLibrary.SEARCH_ICON;
	}

	/**
	 * Remove ModuleView from controller.
	 */
	@Override
	public void deleteModuleView() {
		this.controller.removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Update right view to have Freeplane icon scrollbar.
	 */
	@Override
	public void willShow() {
		// TODO
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		// TODO
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	/**
	 * Selects the FIB Panel to display depending of selected Object type
	 * 
	 * @param object
	 * @return
	 */
	public Resource getFibForXMLObject(OPCObject object) {
		/*if (object instanceof XMLType) {
			return XMLType_FIB_FILE;
		}
		else if (object instanceof XMLProperty) {
			return XMLProperty_FIB_FILE;
		}
		else
			return null;*/
		System.out.println("Que dois je retourner pour " + object);
		return null;
	}

	public FIBInspector inspectorForObject(Object object) {

		System.out.println("Pour l'objet " + object);
		System.out.println("flexoController: " + getFlexoController());
		if (getFlexoController() == null) {
			return null;
		}

		System.out.println("On retourne: " + getFlexoController().getModuleInspectorController().inspectorForObject(object));

		return getFlexoController().getModuleInspectorController().inspectorForObject(object);
	}

	public FIBTab basicInspectorTabForObject(Object object) {
		FIBInspector inspector = inspectorForObject(object);
		if (inspector != null && inspector.getTabPanel() != null) {
			return (FIBTab) inspector.getTabPanel().getSubComponentNamed("BasicTab");
		}
		return null;
	}

}
