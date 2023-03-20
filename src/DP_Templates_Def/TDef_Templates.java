package DP_Templates_Def;

import General.Global_Feedback;
import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Templates {
    private ArrayList <TDef_Template> templates;
    
    public TDef_Templates() {
        templates = new ArrayList();
    }
    
    public void add(TDef_Template template) {
        templates.add(template);
    }
    
    public ArrayList<TDef_Template> getTemplates() {
        return templates;
    }
    
    public void checkInheritancesAndRealizations() {
        Global_Feedback.showOrWite(0, "TDef_Templates: checkInheritancesAndRealizations Komt nog");
        //for(TDef_Template template : templates) 
        //    template.checkInheritancesAndRealizations();
    }
    
    public void removeDependenciesForStandardJavaClasses() {
        for (TDef_Template templ : templates) {
            templ.removeDependenciesForStandardJavaClasses();
        }
    }
          
    @Override
    public String toString() {
        String result = "Templates";
        
        for (TDef_Template templ : templates) {
            result += "\n\n" + templ.toString();
        }
            
        return result;    
    } 
}
