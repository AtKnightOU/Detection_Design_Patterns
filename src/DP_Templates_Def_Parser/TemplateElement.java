package DP_Templates_Def_Parser;

/**
 *
 * @author E.M. van Doorn
 */

import DP_Templates_Def.TDef_Name;
import DP_Templates_Def.TDef_Template;
import DP_Templates_Def.TDef_TemplateContent;
import General.Constants;
import org.xml.sax.Attributes;

public class TemplateElement implements ProcessSAXTags {

    private TDef_Name templateName;
    private boolean firstCall;
    private TemplateContentElement templateContentElement;

    TemplateElement() {
        templateName = null;
        firstCall = true;
        templateContentElement = new TemplateContentElement(Constants.TEMPLATE);
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        if (firstCall) {
            firstCall = false;
            templateName = new NameElement(attributes).getTDef_Name();
        }

        templateContentElement.startElement(qName, attributes);
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        return templateContentElement.endElement(qName);
    }

    public TDef_Template getTemplate() {
        TDef_TemplateContent tc;

        tc = templateContentElement.getTemplateContent();
        tc.generateEdges();
        // edges can only be generated when all classes of the template has
        // been parsed.

        return new TDef_Template(templateName, tc);
    }
}
