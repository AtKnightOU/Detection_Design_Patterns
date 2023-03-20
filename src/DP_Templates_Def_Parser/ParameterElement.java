package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Name;
import DP_Templates_Def.TDef_Parameter;
import General.Global_Feedback;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class ParameterElement implements ProcessSAXTags {

    private TDef_Parameter parameterDefinition;
    private ProcessSAXTags handler;

    public ParameterElement() {
        handler = null;
        parameterDefinition = null;
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        if (handler != null) {
            // Should not occur
            handler.startElement(qName, attributes);

            return;
        }
        
        checkAttribute(qName);

        if (parameterDefinition == null) {
            String type, s;
            TDef_Name tdef_parameterName;

            tdef_parameterName = new NameElement(attributes).getTDef_Name();

            s = "type";
            type = "";
            if (attributes.getIndex(s) != -1) {
                type = attributes.getValue(s);
            }

            parameterDefinition = new TDef_Parameter(type, tdef_parameterName);
        } else {
            if (!qName.equals("parameter")) {
                errorMessage(qName);
                System.exit(1);
            }
        }
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        checkAttribute(qName);

        return null;
    }
    
    private void checkAttribute(String qName) {
        if (!qName.equals("parameter")) {
            errorMessage(qName);
            System.exit(1);
        }
    }

    private void errorMessage(String qName) {
        Global_Feedback.showOrWite(0, "Unexpected tag in ParameterElement: " + qName);
        System.exit(1);
    }

    public TDef_Parameter getParameter() {
        return parameterDefinition;
    }
}
