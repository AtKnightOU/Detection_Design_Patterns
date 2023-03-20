package DP_Templates_Def;

import General.Global_Debug_Info;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_Template {
    private TDef_Name templateName;
    private TDef_TemplateContent templateContent;

    public TDef_Template(TDef_Name templateName) {
        this.templateName = templateName;
        templateContent = new TDef_TemplateContent();
    }

    public TDef_Template(TDef_Name templateName, TDef_TemplateContent tc) {
        this.templateName = templateName;
        templateContent = tc;
    }

    public void addClass(TDef_Class tdef_class) {
        templateContent.addClass(tdef_class);
    }

    public void addPeninsula(TDef_Peninsula peninsula) {
        templateContent.addPeninsula(peninsula);
    }

    public void addRepeatingGroup(TDef_RepeatingGroup repeatingGroup) {
        templateContent.addRepeatingGroup(repeatingGroup);
    }

    public void addEdge(TDef_Edge edge) {
        templateContent.addEdge(edge);
        Global_Debug_Info.println("TDef_Template: LET OP (komt dit voor??): " + edge.toString());
    } 
    
    public void removeDependenciesForStandardJavaClasses() {
        templateContent.removeDependenciesForStandardJavaClasses();
    }
    
    public String getName() {
        return templateName.getName();
    }
    
    public TDef_TemplateContent getTemplateContent() {
        return templateContent;
    }

    @Override
    public String toString() {
        String result;

        result = "\nTemplate " + templateName.toString() + " = \n";
        result += templateContent.toString();

        return result;
    }
}
