package General;

/**
 *
 * @author E.M. van Doorn
 */
import java.util.HashMap;

public class Constants {

    public final static String ASSOCIATION = "ASSOCIATION",
            ASSOCIATION_DIRECTED = "ASSOCIATION_DIRECTED",
            ONETOMANY = "ONETOMANY",
            INHERITANCE = "INHERITANCE",
            DEPENDENCY = "DEPENDENCY",
            ABSTRACT = "ABSTRACT", 
            INTERFACE = "INTERFACE", 
            ABSTRACT_INTERFACE = "ABSTRACT_INTERFACE";
    
    public final static String PRIVATE = "private", 
            PUBLIC = "public";
    
    public final static String TEMPLATE = "template";
    public final static String PENINSULA = "peninsula";
    public final static String REPEATING_GROUP = "repeating_group";
    
    public final static String EMPTY = "";

    private static HashMap<String, Integer> strToInt;
    private static HashMap<Integer, String> intToStr;
    private static String edgeTypes[] = {
        ASSOCIATION, ASSOCIATION_DIRECTED, ONETOMANY,
        INHERITANCE, DEPENDENCY };
    
    private static String modifiers[] = {
        "", PUBLIC, PRIVATE // first should be ""
    };
    private static String statics[] = {
        "", "true", "false"};           // first should be ""
    private static String abstracts[] = {
        "", "true", "false"};         // first should be ""
    private static String classTypes[] = {
        "", ABSTRACT, INTERFACE, ABSTRACT_INTERFACE
    };                            // first should be ""

    public Constants() {
        strToInt = new HashMap();
        intToStr = new HashMap();

        addEdgesTypes();
        addModifiers();
        addStatics();
        addAbstracts();
        addClassTypes();
    }

    //----------------------------------------------------------
    private void addEdgesTypes() {
        int startIndex = 100;
        int tmp;

        for (int i = 0; i < edgeTypes.length; i++) {
            tmp = startIndex + i;
            
            Global_Debug_Info.println("Constantes " + tmp + " " + edgeTypes[i]);
            
            strToInt.put(edgeTypes[i], tmp);
            intToStr.put(tmp, edgeTypes[i]);
        }
    }

    public static boolean edgeTypeStrOk(String type) {
        for (String edgeType : edgeTypes) {
            if (edgeType.equals(type)) {
                return true;
            }
        }

        return false;
    }

    //----------------------------------------------------------
    private void addModifiers() {
        int startIndex = 110;
        int tmp;

        for (int i = 0; i < modifiers.length; i++) {
            if (!strToInt.containsKey(modifiers[i])) {
                tmp = startIndex + i;
                
                Global_Debug_Info.println("Constantes " + tmp + " " + modifiers[i]);
                
                strToInt.put(modifiers[i], tmp);
                intToStr.put(tmp, modifiers[i]);
            }
        }
    }

    public static boolean modifierStrOk(String modifier) {
        for (String modifier1 : modifiers) {
            if (modifier1.equals(modifier)) {
                return true;
            }
        }

        return false;
    }

    //----------------------------------------------------------
    private void addStatics() {
        int startIndex = 120;
        int tmp;

        for (int i = 0; i < statics.length; i++) {
            if (!strToInt.containsKey(statics[i])) {
                tmp = startIndex + i;
                Global_Debug_Info.println("Constantes " + tmp + " " + statics[i]);
                strToInt.put(statics[i], tmp);
                intToStr.put(tmp, statics[i]);
            }
        }
    }

    public static boolean staticStrOk(String str) {
        for (String statik : statics) {
            if (statik.equals(str)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isStaticInt(int value) {
        return value == getInt(statics[1]);
    }

    //----------------------------------------------------------
    private void addAbstracts() {
        int startIndex = 130;
        int tmp;

        for (int i = 0; i < abstracts.length; i++) {
            if (!strToInt.containsKey(abstracts[i])) {
                tmp = startIndex + i;
                Global_Debug_Info.println("Constantes " + tmp + " " + abstracts[i]);
                strToInt.put(abstracts[i], tmp);
                intToStr.put(tmp, abstracts[i]);
            }
        }
    }
    
    public static boolean isAbstractInt(int value) {
        return value == getInt(statics[1]);
    }

    public static boolean abstractStrOk(String str) {
        for (String abstract1 : abstracts) {
            if (abstract1.equals(str)) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean isPrivateInt(int value) {
        return strToInt.get(PRIVATE) == value;
    }

    public static boolean isAbstractTypeInt(int value) {
        return value == getInt(ABSTRACT);
    }
    
    public static boolean isInterfaceInt(int value) {
       return value == strToInt.get(INTERFACE) ;
    }
    
    public static boolean isInheritanceInt(int value) {
       return value == strToInt.get(INHERITANCE) ;
    }
    
    public static boolean isDependencyInt(int value) {
       return value == strToInt.get(DEPENDENCY) ;
    }

    //----------------------------------------------------------
    private void addClassTypes() {
        int startIndex = 140;
        int tmp;

        for (int i = 0; i < classTypes.length; i++) {
            if (!strToInt.containsKey(classTypes[i])) {
                tmp = startIndex + i;
                
                Global_Debug_Info.println("Constantes " + tmp + " " + classTypes[i]);

                strToInt.put(classTypes[i], tmp);
                intToStr.put(tmp, classTypes[i]);
            }
        }
    }

    public static boolean classTypeOk(String str) {
        for (String classType : classTypes) {
            if (classType.equals(str)) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean isAbstractInterfaceInt(int classTypeInt) {
       return classTypeInt == strToInt.get(ABSTRACT_INTERFACE); 
    }
    

    public static boolean isConcreteClass(int classTypeInt) {
        return classTypeInt == strToInt.get("");
        // So, not abstract, interface, abstract_interface
    }

    //----------------------------------------------------------
    public static boolean getBoolValue(String boolStr) {
        if (boolStr.toLowerCase().equals("true")) {
            return true;
        }

        if (boolStr.toLowerCase().equals("false")) {
            return false;
        }

        Global_Feedback.showOrWite(0, boolStr + " is not a boolean");
        System.exit(1);

        return false; // dummy value
    }

    //----------------------------------------------------------
    public static String getStr(int index) {
        return intToStr.get(index);
    }

    public static int getInt(String str) {
        return strToInt.get(str);
    }
    
    //----------------------------------------------------------
    public static boolean isEmpty(String str) {
        return str.equals(EMPTY);
    }
}
