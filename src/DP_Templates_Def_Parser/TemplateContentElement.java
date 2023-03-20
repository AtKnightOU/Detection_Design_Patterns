package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_TemplateContent;
import DP_Templates_Def.TDef_Global_ClassNames;
import General.Constants;
import org.xml.sax.Attributes;

import DP_Templates_Def.TDef_Class;

/**
 *
 * @author E.M. van Doorn
 */

public class TemplateContentElement implements ProcessSAXTags {
    private String tagName;
    private ProcessSAXTags handler;
    private TDef_TemplateContent def_templateContent;
    private EdgeElement edgeElement;
    private ClassElement classElement;
    private PeninsulaElement peninsulaElement;
    private RepeatingGroupElement repeatingGroupElement;

    TemplateContentElement(String tagName) {
        this.tagName = tagName;
        handler = null;
        def_templateContent = null;
        edgeElement = null;
        classElement = null;
        peninsulaElement = null;
        repeatingGroupElement = null;
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        if (handler != null) {
            handler.startElement(qName, attributes);

            return;
        }

        if (def_templateContent == null) {
            def_templateContent = new TDef_TemplateContent();
        } else {
            switch (qName) {
                case "class":
                    classElement = new ClassElement();
                    handler = classElement;
                    handler.startElement(qName, attributes);
                    break;

                case "edge":
                    edgeElement = new EdgeElement();
                    handler = edgeElement;
                    handler.startElement(qName, attributes);
                    break;
                    
                case Constants.PENINSULA:
                    peninsulaElement = new PeninsulaElement();
                    handler = peninsulaElement;
                    handler.startElement(qName, attributes);
                    break;

                case Constants.REPEATING_GROUP:
                    repeatingGroupElement = new RepeatingGroupElement();
                    handler = repeatingGroupElement;
                    handler.startElement(qName, attributes);
                    break;
            }
        }
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        if (handler != null) {
            handler = handler.endElement(qName);

            switch (qName) {
                case "edge":
                    if (edgeElement != null) {
                        def_templateContent.addEdge(edgeElement.getEdgeDefinition());
                        edgeElement = null;
                    }
                    break;

                case "class":
                    if (classElement != null) {
                        TDef_Class tdef_class = classElement.getClassDefinition();
                        TDef_Global_ClassNames singleton = TDef_Global_ClassNames.getInstance();
                        singleton.addClassName(tdef_class.getClassName());
                        
                        def_templateContent.addClass(classElement.getClassDefinition());
                        classElement = null;
                    }
                    break;

                case Constants.PENINSULA:
                    if (peninsulaElement != null) {
                        def_templateContent.addPeninsula(peninsulaElement.getPeninsulaDefinition());
                        peninsulaElement = null;
                    }
                    break;

                case Constants.REPEATING_GROUP:
                    if (repeatingGroupElement != null) {
                        def_templateContent.addRepeatingGroup(repeatingGroupElement.getRepeatingGroupDefinition());
                        repeatingGroupElement = null;
                    }
                    break;
            }

            return this;
        }

        return qName.equals(tagName) ? null : this;
        // template, peninsula, repeatingGroup
    }

    public TDef_TemplateContent getTemplateContent() {
        return def_templateContent;
    }
}
