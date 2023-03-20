package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Peninsula;
import DP_Templates_Def.TDef_TemplateContent;
import General.Constants;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class PeninsulaElement implements ProcessSAXTags {
    private TemplateContentElement templateContentElement;
    
    PeninsulaElement() {
        templateContentElement = new TemplateContentElement(Constants.PENINSULA);
    }
    
    @Override
    public void startElement(String qName, Attributes attributes) {
        templateContentElement.startElement(qName, attributes);
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        return templateContentElement.endElement(qName);
    }
        
    public TDef_Peninsula getPeninsulaDefinition() {
        TDef_TemplateContent tc;

        tc = templateContentElement.getTemplateContent();

        return new TDef_Peninsula(tc);
    }
}
