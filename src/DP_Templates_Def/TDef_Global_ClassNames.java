package DP_Templates_Def;

import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */

/* Repeating groups and peninsula's may be nested.
   This causes a problem.  Example:
   <repeating_group>
             <class name="AbstrProd"   type="ABSTRACT_INTERFACE"/>

	     <repeating_group>
                 <class name="Prod"/>
                 <edge node1="Prod" node2="AbstrProd" type="INHERITANCE"/>
                 <!-- PROBLEM --!>
              </repeating_group>
   The class AbstrProd is not visible inside the nested repeating group.
   This can be solved by a reference to the outer repeating group and
   a recursive search algorithm. However a Singleton class that contains all 
   classnames is a simpler solution.
*/

public class TDef_Global_ClassNames { 
    private static TDef_Global_ClassNames instance = null;
    private static ArrayList <String> classNames;
    
    //private constructor to avoid client applications to use constructor
    private TDef_Global_ClassNames(){
       classNames = null;
    }

    public static TDef_Global_ClassNames getInstance(){
        if(instance == null){
            instance = new TDef_Global_ClassNames();
            classNames = new ArrayList();
        }
        
        return instance;
    }
    
    public void addClassName(String className) {
        classNames.add(className);
    }
    
    public static ArrayList <String> allClassNames() {
        ArrayList <String> result;
        
        result = new ArrayList();
        result.addAll(classNames);
        
        return result;
    }
    
    public static void destroy() {
        instance = null;
        classNames = null;
    }
}
