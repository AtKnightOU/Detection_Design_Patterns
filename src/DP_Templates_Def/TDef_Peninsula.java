package DP_Templates_Def;

import General.Global_Debug_Info;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Peninsula  {   
    private TDef_TemplateContent templateContent;
    
    public TDef_Peninsula(TDef_TemplateContent tc) {
        templateContent = tc;
        tc.setPartOfPeninsula(true);
        Global_Debug_Info.println("TDef_Peninsula IS NOG GEEN SCHIEREILAND");
        Global_Debug_Info.println("TDef_Peninsula: BRUGGEHOOFDCLASS en OVERIGE CLASSES NOG BEPALEN.");
        Global_Debug_Info.println("================================================");
    }
     
    public ArrayList<TDef_Edge> getAllEdges() {
        return templateContent.getAllEdges();
    }
    
    public ArrayList<TDef_Class> getAllClasses() {
        return templateContent.getAllClasses();
    }
    
    public ArrayList<String> getAllClassNames() {
        return templateContent.getAllClassNames();
    }
    
    void setParentContent(TDef_TemplateContent parent) {
        templateContent.setParentContent(parent);
    }
      
    @Override
    public String toString() {
        return "Peninsula -->\n" +
                templateContent.toString() +
                "<-- Peninsula\n";
    }  
}
