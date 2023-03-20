package DP_Templates_Def;

import General.Constants;
import General.Global_Feedback;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_Static {

    private int staticInt;

    TDef_Static() {
        this("false");
    }

    TDef_Static(String str) {
        if (Constants.staticStrOk(str)) {
            staticInt = Constants.getInt(str);
        } else {
            errorMessage("Invalid value for static: " + str);

            System.exit(1);
        }
    }

    boolean isStatic() {
        return Constants.isStaticInt(staticInt);
    }

    public boolean isMatch(TDef_Static staticObj) {
        return staticInt == staticObj.staticInt;

    }

    private void errorMessage(String msg) {
        Global_Feedback.showOrWite(0, msg);
        System.exit(1);
    }

    @Override
    public String toString() {
        return isStatic() ? "static" : "";
    }
}
