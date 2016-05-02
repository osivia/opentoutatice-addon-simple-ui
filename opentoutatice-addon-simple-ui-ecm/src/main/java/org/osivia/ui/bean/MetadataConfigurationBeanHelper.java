/**
 * 
 */
package org.osivia.ui.bean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.platform.ui.web.util.SeamComponentCallHelper;
import org.nuxeo.runtime.api.Framework;
import org.osivia.ui.constants.ExtendedSeamPrecedence;

import fr.toutatice.ecm.platform.service.fragments.configuration.ConfigurationBeanHelper;
import fr.toutatice.ecm.platform.web.document.ToutaticeWebActionsBean;


/**
 * @author david
 *
 */
@Name("config")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = ExtendedSeamPrecedence.ADDON)
public class MetadataConfigurationBeanHelper extends ConfigurationBeanHelper {
    
    /** Show metadata in Nuxeo BO indicator. */
    public static final String METADATA_IN_NX_BO = "ottc.metadata.show.nx.bo";
    
    /**
     * @return true if metadata must be inject in Nuxeo BO.
     */
    public boolean showMetadataInNx() {
        ToutaticeWebActionsBean webBean = (ToutaticeWebActionsBean) SeamComponentCallHelper.getSeamComponentByName("webActions");
        
        boolean isNotInPVContext = !webBean.isInPortalViewContext();
        boolean showInBO = Boolean.valueOf(Framework.getProperty(METADATA_IN_NX_BO));
        
        return isNotInPVContext && showInBO;
    }

}
