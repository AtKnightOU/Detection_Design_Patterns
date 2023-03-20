package DP_Templates_Def;

import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_RepeatingGroup {
    private TDef_TemplateContent templateContent;
    
    public TDef_RepeatingGroup(TDef_TemplateContent tc) {
        templateContent = tc;
        templateContent.setPartOfRepeatingGroup(true);
    }
    
    public void setParentContent(TDef_TemplateContent parent) {
        templateContent.setParentContent(parent);
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
    
    @Override
    public String toString() {
        return "RepeatingGroup -->\n" +
                templateContent.toString() +
                "<-- RepeatingGroup\n";
    }   
}
