package General;

/**
 *
 * @author E.M. van Doorn
 */
public class Global_Debug_Info {
   private static Global_Debug_Info instance = null;
   private static boolean showDebug = false;
    
    // private constructor to avoid client applications to use constructor
    private Global_Debug_Info(){
    }

    public static Global_Debug_Info getInstance(){
        if(instance == null){
            instance = new Global_Debug_Info();
        }
        
        return instance;
    } 
    
    public static void setShowDebug(boolean value) {
        showDebug = value;
    }
    
    public static void println(String text) {
        if (showDebug)
            Global_Feedback.showOrWite(0, text);  
    }
}
