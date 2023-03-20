package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Class;
import General.Constants;
import General.Global_Feedback;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */
public class ClassElement implements ProcessSAXTags {

    private ProcessSAXTags handler;
    private TDef_Class classDefinition;
    private AttributeElement attributeElement;
    private OperationElement operationElement;
    private NameElement classNameElement;

    ClassElement() {
        classNameElement = null;
        handler = null;
        attributeElement = null;
        operationElement = null;
        classDefinition = null;
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        if (handler != null) {
            handler.startElement(qName, attributes);

            return;
        }

        if (classDefinition == null) {
            String s, type;

            classNameElement = new NameElement(attributes);          
            
            type = "";
            s = "type";
            if (attributes.getIndex(s) != -1) {
                type = attributes.getValue(s);
            }
          
            // NEXT STATEMENT IS NOT RIGHT!!!
            // WHEN A CLASS "public abstract class"
            // THEN "public" IS IGNORED !!!
            s = "isAbstract";
            if (attributes.getIndex(s) != -1) {
                type = attributes.getValue(s);
                if (type.equals("true")) {
                    type = Constants.ABSTRACT;
                }
                else if (type.equals("false")) {
                    type = Constants.EMPTY;
                }
            } 
            
            if (type.length() != 0) {
                classDefinition = new TDef_Class(type, classNameElement.getTDef_Name());
            } else {
                classDefinition = new TDef_Class(classNameElement.getTDef_Name());
            }
        } else {
            switch (qName) {
                case "attribute":
                    attributeElement = new AttributeElement();
                    handler = attributeElement;
                    handler.startElement(qName, attributes);
                    break;

                case "operation":
                    operationElement = new OperationElement();
                    handler = operationElement;
                    handler.startElement(qName, attributes);
                    break;

                default:
                    errorMessage(qName);
                    System.exit(1);
                    break;
            }
        }
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        if (handler != null) {
            handler = handler.endElement(qName);

            switch (qName) {
                case "attribute":
                    classDefinition.addAttribute(attributeElement.getAttribute());
                    break;

                case "operation":
                    classDefinition.addOperation(operationElement.getOperation());
                    break;
            }

            return this;
        }

        if (!qName.equals("class")) {
            errorMessage(qName);
            System.exit(1);
        }

        return null;
    }

    public TDef_Class getClassDefinition() {
        return classDefinition;
    }

    void errorMessage(String qName) {
        Global_Feedback.showOrWite(0, "Unexpected tag in ClassElement: " + qName + 
                " in class: " + classNameElement.toString());
    }

    @Override
    public String toString() {
        return classDefinition.toString();
    }
}
