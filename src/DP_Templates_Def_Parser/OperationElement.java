package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Modifier;
import DP_Templates_Def.TDef_Name;
import DP_Templates_Def.TDef_Operation;
import General.Constants;
import General.Global_Feedback;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class OperationElement implements ProcessSAXTags {

    private TDef_Operation operationDefinition;
    private ParameterElement parameterElement;
    private ProcessSAXTags handler;
    private TDef_Name tdef_operationName;

    public OperationElement() {
        tdef_operationName = null;
        operationDefinition = null;
        parameterElement = null;
        handler = null;
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        if (handler != null) {
            handler.startElement(qName, attributes);

            return;
        }
        
        if (operationDefinition == null) {
            String s, occursMultipleStr, staticStr, abstractStr, str;

            tdef_operationName = new NameElement(attributes).getTDef_Name();
            operationDefinition = new TDef_Operation(tdef_operationName);

            s = "modifier";
            if (attributes.getIndex(s) != -1) {
                TDef_Modifier modifier;
                
                modifier = new TDef_Modifier(attributes.getValue(s));
                operationDefinition.setModifier(modifier);
            }

            s = "occursMultiple";
            if (attributes.getIndex(s) != -1) {
                occursMultipleStr = attributes.getValue(s);
                operationDefinition.setOccursMultipleBool(Constants.getBoolValue(occursMultipleStr));
            }

            s = "isStatic";
            if (attributes.getIndex(s) != -1) {
                staticStr = attributes.getValue(s);
                operationDefinition.setStaticStr(staticStr);
            }
            
            s = "isAbstract";
            if (attributes.getIndex(s) != -1) {
                abstractStr = attributes.getValue(s);
                operationDefinition.setAbstractStr(abstractStr);
            }
            
            s = "calledBy";
            if (attributes.getIndex(s) != -1) {
                str = attributes.getValue(s);
                operationDefinition.setCalledByStr(str);
            }
            
            s = "correspondTo";
            if (attributes.getIndex(s) != -1) {
                str = attributes.getValue(s);
                operationDefinition.setCorrespondToStr(str);
            }
            
            s = "returnType"; /* Not described in EBNF definition */
            if (attributes.getIndex(s) != -1) {
                str = attributes.getValue(s);
                operationDefinition.setReturnType(str);
            }
        } else {
            switch (qName) {
                case "parameter":
                    parameterElement = new ParameterElement();
                    handler = parameterElement;
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

            if (!qName.equals("parameter")) {
                errorMessage(qName);
                System.exit(1);
            }

            operationDefinition.addParameter(parameterElement.getParameter());

            return this;
        }

        if (!qName.equals("operation")) {
            errorMessage(qName);
            System.exit(1);
        }

        return null;
    }

    public TDef_Operation getOperation() {
        return operationDefinition;
    }

    void errorMessage(String qName) {
        Global_Feedback.showOrWite(0, "Unexpected tag in OperationElement: " + qName + 
                           " in operation: " + tdef_operationName.toString());
        Global_Feedback.showOrWite(0, operationDefinition.toString());
        System.exit(1);
    }
}
