/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * 
 * Contributors:
 * dchevrier
 */
package org.osivia.ui.bean;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.opentoutatice.ecm.attached.images.bean.OttcDocumentActionsBean;
import org.osivia.ui.constants.ExtendedSeamPrecedence;

import fr.toutatice.ecm.platform.core.constants.ToutaticeNuxeoStudioConst;


/**
 * @author David Chevrier.
 *
 */
@Name("documentActions")
@Scope(CONVERSATION)
@Install(precedence = ExtendedSeamPrecedence.ADDON)
public class MetadataDocumentActionsBean extends OttcDocumentActionsBean {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Default behavior: all Folderish ara shown in menu.
	 * @throws ClientException
	 */
	@Override
	@Observer(value = {EventNames.NEW_DOCUMENT_CREATED})
    public void initShowInMenu() throws ClientException {
		super.initShowInMenu();
		
		DocumentModel newDocument = navigationContext.getChangeableDocument();
		boolean folderish = newDocument.hasFacet("Folderish");
		
		newDocument.setPropertyValue(ToutaticeNuxeoStudioConst.CST_DOC_XPATH_TOUTATICE_SIM, folderish);
	}
	
}
