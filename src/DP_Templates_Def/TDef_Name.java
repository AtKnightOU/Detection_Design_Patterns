package DP_Templates_Def;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Name {
    private boolean isPrescribed;
    private String name;
    private boolean isMultiple;
    
    public TDef_Name(String name, boolean isPrescribed) {
        this.name = name;
        this.isPrescribed = isPrescribed;
        
        if (name.contains("[")) {
            isMultiple = true;
            this.name = this.name.replace("[]", "");
            
        }
        else
            isMultiple = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int length() {
        return name.length();
    }
    
    public boolean isMultiple() {
        return isMultiple;
    }
    
    public boolean isMatch(TDef_Name tdef_name) {
        if (isPrescribed)
            return name.equals(tdef_name.name);
        
        return true;
    }
    
    @Override
    public String toString() {
        String result;
        
        result = isPrescribed ? "prescribed " + name : name;
        if (isMultiple) {
            result += " (Array)";
        }
        
        return result;     
    }
}
