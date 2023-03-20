package dp_detection_in_sources;

import DP_Templates_Def.TDef_Templates;
import DP_Templates_Def.TDef_Template;
import DP_Templates_Def_Parser.TemplatesParser;
import Matching.SearchDesignPatterns;
import General.Global_Debug_Info;
import General.Global_Feedback;

/**
 *
 * @author E.M. van Doorn
 */

public class DP_detection_in_sources {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Global_Debug_Info debugInfo;
        TDef_Templates templatesSystem, templatesDP;
        TDef_Template templateSystem;
        TemplatesParser templatesParser;
        SearchDesignPatterns searchDesignPatterns;        
        
        for (String arg: args){
            if (arg.startsWith("-f"))
                Global_Feedback.setFeedbackModus(arg);
        }
        
        debugInfo = Global_Debug_Info.getInstance();
        Global_Debug_Info.setShowDebug(false);
        
        String currentDir = System.getProperty("user.dir");
        Global_Feedback.showOrWite(0, "Current directory: " + currentDir);
       
        //TDef_Global_ClassNames root_templateContent;
        
        //TemplatesParser templatesParser = new TemplatesParser(args[0]);  
        //TemplatesParser templatesParser = new TemplatesParser(currentDir + "/templates.xml");

        templatesParser = new TemplatesParser(currentDir + "/inputSystem.xml");        
        templatesSystem = templatesParser.parse();
        templatesSystem.removeDependenciesForStandardJavaClasses();
        templateSystem = templatesSystem.getTemplates().get(0);
        
        Global_Feedback.showOrWite(0, "Java Sources are parsed.");
        Global_Debug_Info.setShowDebug(false);
        Global_Debug_Info.println(templatesSystem.toString());
        // System.exit(1);
        
        templatesParser = new TemplatesParser(currentDir + "/templates.xml");
        templatesDP = templatesParser.parse();
        Global_Feedback.showOrWite(0, "Template file for design patterns is parsed.\n");   
        
        searchDesignPatterns = new SearchDesignPatterns();
        searchDesignPatterns.searchDPs(templatesDP, templateSystem);
        // RESULTTAAT NOG RETOURNEREN EN TONEN
        
        Global_Feedback.close();
    }  
}
