package DP_Templates_Def;

import General.*;
import Matching.Solution;
import java.util.HashSet;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_Attribute {

    private String typeAttributeName; // Read from Java source.
    private TDef_Name attributeName;
    private TDef_Modifier modifier;
    private TDef_Static staticObj;
    private boolean isOneToMany;
    private String baseClassName;

    public TDef_Attribute(TDef_Name attributeName) {
        this.attributeName = attributeName;
        typeAttributeName = "";
        baseClassName = "";
        isOneToMany = attributeName.isMultiple();
        
        staticObj = new TDef_Static("");
        modifier = new TDef_Modifier();
        isOneToMany = false;
    }

    public TDef_Attribute(String typeAttributeName, TDef_Name attributeName) {
        this(attributeName);
        this.typeAttributeName = typeAttributeName;
        
        isOneToMany = attributeName.isMultiple(); // e.g. String s[]
        analyzeTypeAttribute();
    }

    public TDef_Attribute(String typeAttributeName, TDef_Name attributeName,
            String staticStr, String modifierStr) {
        this(typeAttributeName, attributeName);

        staticObj = new TDef_Static(staticStr);

        modifier = new TDef_Modifier(modifierStr);
    }
    
    private void analyzeTypeAttribute() {
        String tmpClassName;
        
        tmpClassName = new String(this.typeAttributeName);
        
        if (this.typeAttributeName.contains("[")) {
            tmpClassName = this.typeAttributeName.replaceAll("\\[\\]", "");
            isOneToMany = true;
        }
        
        if (tmpClassName.contains("<")) {
            // E.g. ArrayList<baseClass>
            // ArrayList<HashMap<Key, baseClass>> 
            // ArrayList<HashMap<Key, ArrayList<baseClass>>>
            // HashMap<Key, baseClass>
            // HashMap<Key, ArrayList<baseClass>>
            // Key is supposed to be a String, Integer or another standard Java class.
            // However a class implementing hashcode() and equals() would be, 
            // allowed but we avoid this possibility.
            // So, before last >>>>> is the baseclass
            
            // remove last >..>
            // replace  all < with a space and split the string 
            // to extract baseClass 
            
            tmpClassName = tmpClassName.replaceAll(">", "");
            tmpClassName = tmpClassName.replaceAll("<", " ");
            
            String tmpNames[] = tmpClassName.split(" ");
            
            tmpClassName = tmpNames[tmpNames.length - 1];
            isOneToMany = true;
        }
        
        this.baseClassName = tmpClassName;
 
    }

    public boolean isStatic() {
        return staticObj.isStatic();
    }

    public String getName() {
        return attributeName.getName();
    }

    public String getType() {
        return typeAttributeName;
    }
    
    public String getBaseClassName() {
        return baseClassName;
    }
    
    public TDef_Edge generateEge(String className, HashSet<String> classNamesInSource,
            HashSet standardMultipleTypes) {
        if (classNamesInSource.contains(baseClassName)) {
            if ((className.equals(baseClassName) && isStatic()))
                // Example A Singleton contains an attribute static Singleton instance.
                // instance has no directed association to Singleton, because
                // by definition an association is between more than one object.
                // Notice, a static attribute is uniq in an class.
                return null;
        
            if (isOneToMany) 
                return new TDef_Edge(baseClassName, className, Constants.ONETOMANY);
            else
                return new TDef_Edge(className, baseClassName, Constants.ASSOCIATION_DIRECTED);
        }
        
        return null; // standard Java Class 
    }
    
    public boolean isMatch(TDef_Attribute tdef_attribute, Solution solution) {
        if (!modifier.isMatch(tdef_attribute.modifier)) {
            Global_Feedback.showOrWite(1, "Modifiers of attribute " + attributeName  + 
                    " differ from  modifiers of " + tdef_attribute.getName());
            
            return false;
        }
        
         if (!staticObj.isMatch(tdef_attribute.staticObj)) {
             Global_Feedback.showOrWite(1, "static-values differ " + attributeName);
             
             return false;
         }
            
         if (typeAttributeName.equals(""))
             return attributeName.isMatch(tdef_attribute.attributeName);
         
         Global_Debug_Info.println("TDef_Attribute isMatch(): attribute: type translate: type = " + 
                 typeAttributeName + "- " + 
                 tdef_attribute.typeAttributeName + 
                 " attributeName = " +  
                 solution.getMatchedSysName(tdef_attribute.typeAttributeName));
         
         return typeAttributeName.equals(solution.getMatchedSysName(tdef_attribute.typeAttributeName)) &&
                attributeName.isMatch(tdef_attribute.attributeName);        
    }


    @Override
    public String toString() {
        String result;

        result = modifier.toString();
        if (result.length() > 0) {
            result += " ";
        }

        result += staticObj.toString();
        if (staticObj.toString().length() > 0) {
            result += " ";
        }

        result += typeAttributeName;
        if (typeAttributeName.length() > 0) {
            result += " ";
        }

        result += attributeName.toString();

        return result;
    }
}
