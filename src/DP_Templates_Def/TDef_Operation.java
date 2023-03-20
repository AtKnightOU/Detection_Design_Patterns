package DP_Templates_Def;

import General.Constants;
import General.Global_Feedback;
import Matching.Solution;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_Operation {

    private TDef_Name operationName;
    private TDef_Modifier modifier;
    private TDef_Static staticObj;
    private TDef_Abstract abstractObj; 
    private Boolean occursMultipleBool;
    private String calledByStr, correspondToStr;
    private String returnType;
    private ArrayList<TDef_Parameter> parameters;
    private TDef_Class containingClass;

    public TDef_Operation(TDef_Name operationName) {
        this.operationName = operationName;
        this.modifier = new TDef_Modifier();
        this.staticObj = new TDef_Static();
        this.abstractObj = new TDef_Abstract();
        this.occursMultipleBool = false;
        this.calledByStr = "";
        this.correspondToStr = "";
        this.returnType = "";
        /* Not documented in EBNF description */
        this.parameters = new ArrayList();
        this.containingClass = null;
    }

    public ArrayList<TDef_Edge> generateEdges(String className,
            HashSet<String> classNamesInSource,
            HashSet standardMultipleTypes) {
        ArrayList<TDef_Edge> result = new ArrayList();
        String parameterClassName;

        if (typeResultInDependency(returnType, className,
                classNamesInSource)) {
            result.add(new TDef_Edge(className, returnType, Constants.DEPENDENCY));
        }

        for (int i = 0; i < parameters.size(); i++) {
            parameterClassName = parameters.get(i).getTypeParameterName();

            if (typeResultInDependency(parameterClassName, className,
                    classNamesInSource)) {
                result.add(new TDef_Edge(className, parameterClassName, Constants.DEPENDENCY));
            }
        }

        return result;
    }

    private boolean typeResultInDependency(String classInOperationDef,
            String className, HashSet<String> classNamesInSource) {
        
        
        return (!classInOperationDef.equals(className)
                && (classNamesInSource.contains(classInOperationDef)));
    }

    public void addParameter(TDef_Parameter parameter) {
        parameters.add(parameter);
    }

    public void setModifier(TDef_Modifier modifier) {
        this.modifier = modifier;
    }

    public void setStaticStr(String staticStr) {
        this.staticObj = new TDef_Static(staticStr);
    }

    public void setAbstractStr(String abstrStr) {
        this.abstractObj = new TDef_Abstract(abstrStr);
    }

    public void setOccursMultipleBool(boolean occursMultipleBool) {
        this.occursMultipleBool = occursMultipleBool;
    }

    public void setCalledByStr(String calledByStr) {
        this.calledByStr = calledByStr;
    }

    public void setCorrespondToStr(String correspondToStr) {
        this.correspondToStr = correspondToStr;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public boolean isConstructor() {
        String str =  getContainingClass().getClassName();
        
        return str.equals(operationName.getName());
    }
    
    public boolean isPrivate() {
        return modifier.isPrivate();
    }
    
    public boolean isStatic() {
        return staticObj.isStatic();
    }
    
    public boolean isCalledBy() {
        return !calledByStr.equals("");
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public boolean isMatch(TDef_Operation tdef_operation, 
            boolean classIsInterface, Solution solution) {
        if (isCalledBy()) {
            // De parser heeft voor tdef_operation  vastgesteld of hij wordt 
            // aangeroepen door een method die tot dezelfde class behoort.
            
            if (!tdef_operation.isCalledBy()){
                Global_Feedback.showOrWite(1, tdef_operation.getOperationName() + 
                        " is not called by a method of its own class.");
                
                return false;
            }
        }
        
        if (!correspondToStr.equals("")) {
            TDef_Class containingClass;
            
            containingClass = tdef_operation.getContainingClass();
            
            if (!isCorresponding(solution, containingClass)) {
                Global_Feedback.showOrWite(1, "Corresponding " + 
                        errorMessage(tdef_operation));
                
                return false;
            }
        }
        
        if (!modifier.isMatch(tdef_operation.modifier)) {
            Global_Feedback.showOrWite(1, "Modifier " + errorMessage(tdef_operation));
        
            return false;
        }
        
        if (!staticObj.isMatch(tdef_operation.staticObj)) {
            Global_Feedback.showOrWite(1, "Static value " + errorMessage(tdef_operation));
            return false;
        }
        
        if (!classIsInterface && !abstractObj.isMatch(tdef_operation.abstractObj)) {
            // if SystemClass is an interface then operation is abstract.
            
            Global_Feedback.showOrWite(1, "Modifier abstract: " + errorMessage(tdef_operation));
            return false;
        }
        
        if (!operationName.isMatch(tdef_operation.operationName)) {
            Global_Feedback.showOrWite(1, errorMessage(tdef_operation));
            return false;
        }
        
        return recursiveMatch(tdef_operation.parameters, solution);
    }
    
    private boolean recursiveMatch(ArrayList<TDef_Parameter> params, 
            Solution solution) {
        
         if (parameters.size() > params.size()) {
            return false;
        }
         
        if (parameters.isEmpty())
            return true;
         
        boolean compared[] = new boolean[params.size()];
        
        for (int i = 0; i < compared.length; i++)
            compared[i] = false;
        
        return recursiveMatch2(0, params, compared, solution); 
    }
    
    private boolean recursiveMatch2(int indexDP, ArrayList<TDef_Parameter> params, 
            boolean compared[], Solution solution) {
            
        if (indexDP == parameters.size() - 1) {
            for (int i = 0; i < compared.length; i++) {
                if (!compared[i] && parameters.get(indexDP).
                                    isMatch(params.get(i), solution))
                    return true;    
            }
            
            return false;
        }
        
        for (int i = 0; i < params.size(); i++) {
            if (!compared[i]) {
                if (parameters.get(indexDP).isMatch(params.get(i), solution)) {
                    compared[i] = true;
                    
                    if (recursiveMatch2(indexDP + 1, params, compared, solution))
                        return true;
                    
                    compared[i] = false;
                }      
            }
        }
        
        return false;
    }
    
    private boolean isCorresponding(Solution solution, TDef_Class containingClass) {
        ArrayList<String> sys_names;
        
        sys_names = solution.getMatchedSysNames(correspondToStr);
        
        for (String s: sys_names)
            Global_Feedback.showOrWite(1, "corresponding className: " + s);
        
      // Check wether the  corresponderending class (Visitor i.c Visitor pattern)
      // has  methods with the  same name (visit) and  with  parametertypes 
      // equal to sys_names. 
      // (i.c. Blad and  Knoop in my example of Visitor pattern,)
         
         return containingClass.hasMethodsWithParameterType(sys_names);
    }
       
    public void setContainingClass(TDef_Class containingClass) {
        this.containingClass = containingClass;
    }
    
    public TDef_Class getContainingClass() {
        return containingClass;
    }
    
    private String errorMessage(TDef_Operation tdef_operation) {
        return "No match for operation " + tdef_operation.getOperationName() + 
                " with " + getOperationName();
    }
    
    public String getOperationName() {
        return operationName.getName();
    }
    
    public boolean hasParameterType(String parameterType) {
        for (TDef_Parameter parameter : parameters) {
            if (parameter.hasParameterType(parameterType))
                return true;
        }
        
        return false;
    }

    @Override 
    public String toString() {
        String result;

        result = modifier.toString();
        if (result.length() > 0) {
            result += " ";
        }

        if (abstractObj.toString().length() > 0) {
            result += abstractObj.toString() + " ";
        }

        if (staticObj.toString().length() > 0) {
            result += staticObj.toString() + " ";
        }

        if (returnType.length() > 0) {
            result += returnType + " ";
        }

        result += operationName.toString() + "(";

        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) {
                result += ", ";
            }

            result += parameters.get(i).toString();
        }

        result += ")";

        if (occursMultipleBool) {
            result += " occursMultiple ";
        }

        if (calledByStr.length() > 0) {
            result += " called by " + calledByStr;
        }

        if (correspondToStr.length() > 0) {
            result += " corresponds to " + correspondToStr;
        }

        return result;
    }
}
