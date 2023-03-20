package DP_Templates_Def;


import General.Constants;
import General.Global_Feedback;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Abstract {
    private int abstractInt;
  
    TDef_Abstract() {
        this("false");
    }

    TDef_Abstract(String str) {
        if (Constants.abstractStrOk(str)) {
            abstractInt = Constants.getInt(str);
        } else {
            errorMessage("Invalid value for abstract: " + str);

            System.exit(1);
        }
    }

    boolean isAbstract() {
        return Constants.isAbstractInt(abstractInt); 
    }
    
    public boolean isMatch(TDef_Abstract tdef_abstract) {
        return abstractInt == tdef_abstract.abstractInt;
    }

    private void errorMessage(String msg) {
        Global_Feedback.showOrWite(0, msg);
        System.exit(1);
    }
    
    @Override
    public String toString() {
        return isAbstract() ? "abstract" : "";
    }  
}
