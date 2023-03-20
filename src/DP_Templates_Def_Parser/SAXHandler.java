package DP_Templates_Def_Parser;

/**
 *
 * @author E.M. van Doorn
 */

import DP_Templates_Def.TDef_Templates;
import General.Constants;
import General.Global_Feedback;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

    private ProcessSAXTags handler;
    private TemplateElement templateElement;
    private TDef_Templates templates;
    private boolean templatesTagRead;

    public SAXHandler() {
        handler = null;
        templates = new TDef_Templates();
        templatesTagRead = false;
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {

        if (handler != null) {
            handler.startElement(qName, attributes);

            return;
        }

        switch (qName) {
            case "templates":
                if (templatesTagRead) {
                    Global_Feedback.showOrWite(0, "The tag <templates> may occur only once.");
                    System.exit(1);
                }
                templatesTagRead = true;
                break;

            case Constants.TEMPLATE:
                templateElement = new TemplateElement();
                handler = templateElement;
                handler.startElement(qName, attributes);
                break;

            default:
                Global_Feedback.showOrWite(0, "SAXHandler: startElement unexpected tag: " 
                        + qName);
                System.exit(1);
        }
    }

    @Override
    public void endElement(String uri, String localName,
            String qName) throws SAXException {
        
        if (handler != null) {
            handler = handler.endElement(qName);
        }

        if (qName.equals(Constants.TEMPLATE)) {
            templates.add(templateElement.getTemplate());
        }
    }

    public TDef_Templates getTemplates() {
        return templates;
    }
}
