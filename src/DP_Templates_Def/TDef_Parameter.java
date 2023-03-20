package DP_Templates_Def;

import Matching.Solution;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Parameter {
    private String  typeParameterName;
    private TDef_Name parameterName;
    
    public TDef_Parameter() {
        parameterName = new TDef_Name("", false);
        typeParameterName = "";
    } 
    
    public TDef_Parameter(String typeParameterName, TDef_Name parameterName) {
        this.typeParameterName = typeParameterName;
        this.parameterName = parameterName;
    }
    
    public String getTypeParameterName() {
        return typeParameterName;
    }
    
    public boolean isMatch(TDef_Parameter tdef_parameter, Solution solution) {
        if (typeParameterName.equals(""))
            return parameterName.isMatch(tdef_parameter.parameterName);
        
        String dp_className = solution.getMatchedSysName(tdef_parameter.typeParameterName);
        
        if (dp_className == null)
            return typeParameterName.equals(tdef_parameter.typeParameterName);
        
        return typeParameterName.equals(dp_className);
    }
    
    public boolean hasParameterType(String parameterType) {
        return typeParameterName.equals(parameterType);
    }
    
    @Override
    public String toString() {
        String result = "";
        
        if (typeParameterName.length() > 0)
            result += typeParameterName;
        
        if (parameterName.length() > 0) {
            if (result.length() > 0)
                result += " ";
            
            result += parameterName;
        }
        
        return result;
    }
}
