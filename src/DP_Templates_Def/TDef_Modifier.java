package DP_Templates_Def;

import General.Constants;
import General.Global_Feedback;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Modifier {
    private int modifierInt;

    public TDef_Modifier() {
        this("public");
    }

    public TDef_Modifier(String modifierStr) {
        if (Constants.modifierStrOk(modifierStr)) {
            this.modifierInt = Constants.getInt(modifierStr);
        } else {
            errorMessage("Invalid modifier " + modifierStr);
            System.exit(1);
        }
    }
    
    public boolean isPrivate() {
        return Constants.isPrivateInt(modifierInt);
    }
    
    
    public boolean isMatch(TDef_Modifier modifier) {
        return modifierInt == modifier.modifierInt;
    }

    private void errorMessage(String msg) {
        Global_Feedback.showOrWite(0, msg);
        System.exit(1);
    } 
    
    @Override
    public String toString() {
        return Constants.getStr(modifierInt);
    }
}
