package DP_Templates_Def_Parser;


import DP_Templates_Def.TDef_RepeatingGroup;
import DP_Templates_Def.TDef_TemplateContent;
import General.Constants;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class RepeatingGroupElement implements ProcessSAXTags {
    private TemplateContentElement templateContentElement;
    
    RepeatingGroupElement() {
        templateContentElement = new TemplateContentElement(Constants.REPEATING_GROUP);
    }
    
    @Override
    public void startElement(String qName, Attributes attributes) {
        templateContentElement.startElement(qName, attributes);
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        return templateContentElement.endElement(qName);
    }
    
    public TDef_RepeatingGroup getRepeatingGroupDefinition() {
        TDef_TemplateContent tc;

        tc = templateContentElement.getTemplateContent();

        return new TDef_RepeatingGroup(tc);
    }
}
