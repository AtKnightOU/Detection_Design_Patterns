package DP_Templates_Def;

import java.util.ArrayList;
import General.Constants;
import General.Global_Feedback;
import Matching.Solution;
import java.util.HashSet;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_Class {

    private TDef_Name className;
    private int classTypeInt;
    /* Meant for abstract, interface, .... */
    private ArrayList<TDef_Attribute> attributes;
    private ArrayList<TDef_Operation> operations;
    private TDef_TemplateContent parentContent;

    public TDef_Class(TDef_Name className) {
        this.className = className;
        this.classTypeInt = Constants.getInt("");
        attributes = new ArrayList<>();
        operations = new ArrayList<>();
        parentContent = null;
    }

    public TDef_Class(String classTypeStr, TDef_Name className) {
        this(className);

        if (Constants.classTypeOk(classTypeStr)) {
            this.classTypeInt = Constants.getInt(classTypeStr);
        } else {
            errorMessage("Invalid classType " + classTypeStr
                    + "for class " + className);
            System.exit(1);
        }
    }

    public void addAttribute(TDef_Attribute attribute) {
        attributes.add(attribute);
    }

    public void addOperation(TDef_Operation operation) {
        operation.setContainingClass(this);
        // necessary for  Visitor pattern, correspondsTo attribute.
        operations.add(operation);
    }

    private void errorMessage(String msg) {
        Global_Feedback.showOrWite(0, msg + " in class " + className.toString());
        System.exit(1);
    }

    public String getClassName() {
        return className.getName();
    }

    public void generateAttributesEdges(
            TDef_TemplateContent templateContentCaller,
            HashSet classNamesInSource,
            HashSet standardMultipleTypes) {
        TDef_Edge edge;

        for (int i = 0; i < attributes.size(); i++) {
            edge = attributes.get(i).generateEge(className.getName(),
                    classNamesInSource, standardMultipleTypes);

            if (edge != null) {
                templateContentCaller.addEdge(edge);
            }
        }
    }

    public boolean isAbstract() {
        return Constants.isAbstractTypeInt(classTypeInt);
    }
    
    public boolean isInterface() {        
        return Constants.isInterfaceInt(classTypeInt);
    }

    public void generateDependenciesEdges(
            TDef_TemplateContent templateContentCaller,
            HashSet classNamesInSource, HashSet standardMultipleTypes) {

        for (int i = 0; i < operations.size(); i++) {
            ArrayList<TDef_Edge> edges;

            edges = operations.get(i).generateEdges(className.getName(),
                    classNamesInSource, standardMultipleTypes);
            // NOTE: a class --can-- be dependent of a class which also
            // is an attribute !!.
            // These dependencies are in the next statement left out.

            for (int j = 0; j < edges.size(); j++) {
                if (!attributesContainsClass(edges.get(j).getNode2Name())) {
                    templateContentCaller.addEdge(edges.get(j));
                }
            }
        }
    }

    private boolean attributesContainsClass(String className) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getBaseClassName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    public void setParentContent(TDef_TemplateContent parent) {
        parentContent = parent;
    }

    public boolean isMatch(TDef_Class tdef_class, Solution solution) {

        if (classTypeInt != tdef_class.classTypeInt) {
            if (Constants.isConcreteClass(classTypeInt)
                    || Constants.isConcreteClass(tdef_class.classTypeInt)) {
                // One of the classes is concrete and the other is not concrete.
                // Therefore ABSTRACT_INTERFACE is compared with ABSTRACT or INTERFACE
                Global_Feedback.showOrWite(1, errorMessage("class types", tdef_class));

                return false;
            }
        }

        if (!recursiveMatchAttributes(tdef_class.attributes, solution)) {
           Global_Feedback.showOrWite(1, errorMessage("attributes", tdef_class));

            return false;
        }

        if (!recursiveMatchOperations(tdef_class.operations, 
                tdef_class.isInterface(), solution)) {
            Global_Feedback.showOrWite(1, errorMessage("operations", tdef_class));

            return false;
        }

        return true;
    }

    private String  errorMessage(String error, TDef_Class tdef_class) {
        return "No match of " + error + " for class " + 
                tdef_class.getClassName() + " with class "
                + className.getName();
    }

    private boolean recursiveMatchAttributes(ArrayList<TDef_Attribute> attribs,
            Solution solution) {
        if (attributes.size() > attribs.size()) {
            return false;
            // SystemClass has not enough attributes.
        }

        if (attributes.isEmpty()) {
            return true;
        }

        boolean compared[] = new boolean[attribs.size()];

        for (int i = 0; i < compared.length; i++) {
            compared[i] = false;
        }

        return recursiveMatchAttributes2(0, attribs, compared, solution);
    }

    private boolean recursiveMatchAttributes2(int indexDP,
            ArrayList<TDef_Attribute> attribs, boolean compared[],
            Solution solution) {

        if (indexDP == attributes.size() - 1) {
            for (int i = 0; i < compared.length; i++) {
                if (!compared[i] && attributes.get(indexDP).
                        isMatch(attribs.get(i), solution)) {
                    return true;
                }
            }

            return false;
        }

        for (int i = 0; i < compared.length; i++) {
            if (!compared[i]) {
                if (attributes.get(indexDP).isMatch(attribs.get(i), solution)) {
                    compared[i] = true;

                    if (recursiveMatchAttributes2(indexDP + 1, attribs,
                            compared, solution)) {
                        return true;
                    }

                    compared[i] = false;
                }
            }
        }

        return false;
    }

    private boolean recursiveMatchOperations(ArrayList<TDef_Operation> opers,
            boolean classIsInterface, Solution solution) {
        if (operations.size() > opers.size()) {
            return false;
            // SystemClass has not enough operations.
        }

        if (operations.isEmpty()) {
            return true;
        }

        boolean compared[] = new boolean[opers.size()];

        for (int i = 0; i < compared.length; i++) {
            compared[i] = false;
        }

        return recursiveMatchOperation2(0, opers, classIsInterface, 
                compared, solution);
    }

    private boolean recursiveMatchOperation2(int indexDP,
            ArrayList<TDef_Operation> opers, boolean classIsInterface,
            boolean compared[], Solution solution) {

        if (indexDP == operations.size() - 1) {
            for (int i = 0; i < compared.length; i++) {
                if (!compared[i]
                        && operations.get(indexDP).isMatch(opers.get(i), 
                                classIsInterface, solution)) {
                    return true;
                }
            }

            return false;
        }

        for (int i = 0; i < compared.length; i++) {
            if (!compared[i]) {
                if (operations.get(indexDP).isMatch(opers.get(i), 
                        classIsInterface, solution)) {
                    compared[i] = true;

                    if (recursiveMatchOperation2(indexDP + 1, opers, 
                             classIsInterface, compared,
                            solution)) {

                        return true;
                    }

                    compared[i] = false;
                }
            }
        }

        return false;
    }

    public boolean hasMethodsWithParameterType(ArrayList<String> paramClassNames) {
        // necessary for correspondsTo attribute

        String operName;
        boolean usedParamClassNames[] = new boolean[paramClassNames.size()];

        for (int i = 0; i < operations.size(); i++) {
            for (int j = 0; j < usedParamClassNames.length; j++) {
                usedParamClassNames[j] = false;
            }
            
            operName = operations.get(i).getOperationName();

            if (hasParamClassNames(operName, paramClassNames, 
                    usedParamClassNames, 0, 0)) {
                return true;
            }
        }

        return false;
    }

    boolean hasParamClassNames(String operName,
            ArrayList<String> paramClassNames, boolean[] usedNames,
            int used, int indexOper) {

        TDef_Operation tdef_operation;
        String operationName;

        if (used == usedNames.length) {
            return true;
        }

        for (int i = indexOper; i < operations.size(); i++) {
            tdef_operation =  operations.get(i);
            
            operationName = tdef_operation.getOperationName();

            if (operationName.equals(operName)) {
                for (int j = 0; j < usedNames.length; j++) {
                    if (!usedNames[j] && 
                            tdef_operation.
                                    hasParameterType(paramClassNames.get(j))) {
                          usedNames[j] = true;
                          return hasParamClassNames(operName, paramClassNames, usedNames, used + 1, i + 1);
                    }
                }
            }
        }
        
        return false;
    }
    

    @Override
    public String toString() {
        String result;

        if (Constants.isAbstractTypeInt(classTypeInt)) {
            result = "abstract class ";
        } else if (Constants.isConcreteClass(classTypeInt)) {
            result = "class ";
        } else if (Constants.isInterfaceInt(classTypeInt)) {
            result = "interface ";
        } else if (Constants.isAbstractInterfaceInt(classTypeInt)) {
            result = "abstract_interface ";
        } else {
            // should not occur

            result = " " + Constants.getStr(classTypeInt) + " ";
            errorMessage("Error: Wrong classTypeInt: " + classTypeInt);
            // already done System.exit(1);
        }

        result += className
                + " <! See definition of edges for inheriting and realizations !>\n";

        for (int i = 0; i < attributes.size(); i++) {
            result += attributes.get(i).toString() + "\n";
        }

        for (int i = 0; i < operations.size(); i++) {
            result += operations.get(i).toString() + "\n";
        }

        return result + "\n";
    }
}
