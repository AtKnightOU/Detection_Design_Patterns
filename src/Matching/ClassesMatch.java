/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matching;

import DP_Templates_Def.TDef_Class;
import DP_Templates_Def.TDef_TemplateContent;
import General.Global_Debug_Info;
import General.Global_Feedback;

/**
 *
 * @author ed
 */
public class ClassesMatch {

    public boolean classesMatch(Solution solution,
            TDef_TemplateContent templateContent_DP,
            TDef_TemplateContent templateContent_System) {

        TDef_Class systemClass, dpClass;
        String dpClassName;
        boolean result;

        result = true;

        for (String sysClassName : templateContent_System.getAllClassNames()) {
                     
            if (solution.isMatchedSysName(sysClassName)) {
                Global_Feedback.showOrWite(1, "Class: " + sysClassName);
                
                systemClass = templateContent_System.getClass(sysClassName);

                dpClassName = solution.getMatchedSysName(sysClassName);
                
                dpClass = templateContent_DP.getClass(dpClassName);

                if (dpClass.isMatch(systemClass, solution)) {
                    Global_Debug_Info.println("Match " + addSpaces(sysClassName)+ " --> "
                            + dpClassName + " is correct\n");
                } else {
                    Global_Feedback.showOrWite(1, "Match " + addSpaces(sysClassName) + " --> "
                            + dpClassName + " is NOT correct\n");

                    result = false;
                }
            }
        }

        return result;
    }
    
    private String addSpaces(String name) {
        String result;
        
        result = name;
        
        for (int i = 25 - name.length(); i > 0; i--)
            result += " ";
        
        return result;
    }
}
