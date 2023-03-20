package DP_Templates_Def_Parser;

/**
 *
 * @author E.M. van Doorn
 */

import DP_Templates_Def.TDef_Name;
import General.Global_Feedback;
import org.xml.sax.Attributes;


public class NameElement {
    private TDef_Name tdef_name;
    
    NameElement(Attributes attributes) {
        boolean isPrescribed;
        String name;
        
        isPrescribed = false;
        name = ""; // makes compiler happy;

        if (attributes.getIndex("name") != -1) {
            name = attributes.getValue("name");
        }
        else if (attributes.getIndex("prescribed_name") != -1) {
            name = attributes.getValue("prescribed_name");
            isPrescribed = true;
        }
        else {
            Global_Feedback.showOrWite(0, "NameElement: Name parameter is missing" );
            System.exit(1);
        }  
        
        tdef_name = new TDef_Name(name, isPrescribed);
    }
    
    TDef_Name getTDef_Name() {
        return tdef_name;
    }
    
    @Override
    public String toString() {
        return tdef_name.toString();
    }  
}
