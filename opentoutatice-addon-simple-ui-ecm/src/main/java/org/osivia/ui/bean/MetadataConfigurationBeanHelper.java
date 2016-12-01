/**
 * 
 */
package org.osivia.ui.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.forms.layout.api.LayoutDefinition;
import org.nuxeo.ecm.platform.forms.layout.service.WebLayoutManager;
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
    
    /** Default mode. */
    private static final String ANY_MODE = "any";
    /** Default metadata layout. */
    public static final String DEFAULT_METADATA_LAYOUT = "ottc_metadata";
    /** Show metadata in creation mode key. */
    public static final String ENABLED_IN_CREATION_MODE = "enabledInCreation";
    
    /** Content label key. */
    public static final String CONTENT_LABEL = "contentLabel";
    /** Metadata label key. */
    public static final String METADATA_LABEL = "metadataLabel";
    /** Bootstrap right columns key. */
    public static final String BOOTSRAP_RIGHT_COLS = "bootstrapRightCols";
    /** Excluded default metadata types key. */
    public static final String EXCLUDED_DEFAULT_METADATA_TYPES = "excludedDefaultMetadataTypes";
    
    /** Map of document's types which do not use default metadata layout. */
    // FIXME: to move in Registry
    private static Map<String, String[]> customTypes;
    
    /** WebLayoutManager. */
    private static WebLayoutManager layoutManager;
    
    /**
     * Gets Layout Manager.
     * 
     * @return WebLayoutManager instance.
     */
    public static WebLayoutManager getLayoutManager(){
    	if(layoutManager == null){
    		layoutManager = (WebLayoutManager) Framework.getService(WebLayoutManager.class);
    		initLayoutManager();
    	}
    	return layoutManager;
    }
    
    /**
     * Custom (or excluded from default metadata behavior)
     * document's types infos.
     * 
     * @return custom document's types infos
     */
    public static Map<String, String[]> getCustomTypes(){
    	if(customTypes == null){
    		customTypes = new HashMap<String, String[]>(0);
    		getLayoutManager();
    	}
    	return customTypes;
    }
    
    /**
     * Initialize Layout manager with default metadata
     * exceptions rules.
     */
    private static void initLayoutManager() {
    	List<String> layoutNames = getLayoutManager().getLayoutDefinitionNames();
    	for(String layoutName : layoutNames){
    		LayoutDefinition layout = getLayoutManager().getLayoutDefinition(layoutName);
    		Map<String, Map<String, Serializable>> properties = layout.getProperties();
    		
    		if(properties != null && properties.containsKey(ANY_MODE)){
    			Map<String, Serializable> anyModProperties = properties.get(ANY_MODE);
    			
    			if(anyModProperties != null && anyModProperties.containsKey(EXCLUDED_DEFAULT_METADATA_TYPES)){
    				String[] excludedTypes = (String[]) anyModProperties.get(EXCLUDED_DEFAULT_METADATA_TYPES);
    				if(excludedTypes != null && excludedTypes.length > 0){
    					customTypes.put(layoutName, excludedTypes);
    				}
    			}
    		}
    	}
	}
    
    /**
     * Checks if metadata tab must be displayed
     * in creation mode.
     * 
     * @return true if if metadata tab must be displayed
     * in creation mode
     */
    public Boolean isEnabledInCreateMode(DocumentModel document) {
    	Map<String, Serializable> properties = getLayoutProperty(document);
    	return Boolean.valueOf((String) properties.get(ENABLED_IN_CREATION_MODE));
    }

	/**
     * Gets metadata layout's name to display.
     * 
     * @param document
     * @return metadata layout's name to display
     */
    public String getMetadataLayout(DocumentModel document){
    	String docType = document.getType();
    	
    	for(Entry<String, String[]> entry : getCustomTypes().entrySet()){
    		String[] excludedTypes = entry.getValue();
    		if(excludedTypes != null && ArrayUtils.contains(excludedTypes, docType)){
    			return entry.getKey();
    		}
    	}
    	
    	return DEFAULT_METADATA_LAYOUT;
    }
    
    /**
     * Gets content tab label.
     * 
     * @param document
     * @return content tab label
     */
    public String getContentLabel(DocumentModel document){
    	Map<String, Serializable> properties = getLayoutProperty(document);
    	return (String) properties.get(CONTENT_LABEL);
    }

    /**
     * Gets metadata tab label.
     * 
     * @param document
     * @return metadata tab label
     */
    public String getMetadataLabel(DocumentModel document){
    	Map<String, Serializable> properties = getLayoutProperty(document);
    	return (String) properties.get(METADATA_LABEL);
    }
    
    /**
     * Gets right columns number for Bootstrap.
     * 
     * @param document
     * @return right columns number for Bootstrap
     */
    public Integer getRightCols(DocumentModel document){
    	Map<String, Serializable> properties = getLayoutProperty(document);
    	return Integer.valueOf((String) properties.get(BOOTSRAP_RIGHT_COLS));
    }
    
    /**
	 * @param document
	 * @return
	 */
	private Map<String, Serializable> getLayoutProperty(DocumentModel document) {
		String layoutName = getMetadataLayout(document);
    	LayoutDefinition layout = getLayoutManager().getLayoutDefinition(layoutName);
    	Map<String, Serializable> properties = layout.getProperties(ANY_MODE);
		return properties != null ? properties : new HashMap<String, Serializable>(0);
	}
    
    /**
     * @return true if metadata must be inject in Nuxeo BO.
     */
    public boolean showMetadataInNx() {
        ToutaticeWebActionsBean webBean = (ToutaticeWebActionsBean) SeamComponentCallHelper.getSeamComponentByName("webActions");
        
        boolean isNotInPVContext = !webBean.isInPortalViewContext();
        boolean showInBO = Boolean.valueOf(Framework.getProperty(METADATA_IN_NX_BO));
        
        return isNotInPVContext && showInBO;
    }
    
    /**
     * @return true if addon opentoutatice-web-mode is installed in Nx.
     */
    public boolean isWebModeActive(){
        DocumentModel currentDocument = super.navigationContext.getCurrentDocument();
        if(currentDocument != null){
            return currentDocument.hasSchema("ottc_web");
        }
        return false;
    }

}
