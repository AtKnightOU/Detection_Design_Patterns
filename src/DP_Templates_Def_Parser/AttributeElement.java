package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Attribute;
import DP_Templates_Def.TDef_Name;
import General.Global_Feedback;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class AttributeElement implements ProcessSAXTags {
    private TDef_Attribute attributeDefinition;

    public AttributeElement() {
        attributeDefinition = null;
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        String type, modifier, staticStr;
        TDef_Name tdef_name;
        
        checkAttribute(qName);

        if (attributeDefinition == null) {
            String s;

            tdef_name = new NameElement(attributes).getTDef_Name();

            s = "type";
            if (attributes.getIndex(s) != -1) {
                type = attributes.getValue(s);
            } else {
                type = "";
            }

            s = "modifier";
            if (attributes.getIndex(s) != -1) {
                modifier = attributes.getValue(s);
            } else {
                modifier = "";
            }

            s = "isStatic";
            if (attributes.getIndex(s) != -1) {
                staticStr = attributes.getValue(s);
            } else {
                staticStr = "false";
            }
            
            attributeDefinition = new TDef_Attribute(type, tdef_name, staticStr, modifier);
        }
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        checkAttribute(qName);

        return null;
    }
    
    private void checkAttribute(String qName) {
       if (!qName.equals("attribute")) {
            errorMessage(qName);
            System.exit(1);
       } 
    }

    private void errorMessage(String qName) {
        Global_Feedback.showOrWite(0, "Unexpected tag in AttributeElement: " + qName);
        System.exit(1);
    }

    public TDef_Attribute getAttribute() {
        return attributeDefinition;
    }
}
