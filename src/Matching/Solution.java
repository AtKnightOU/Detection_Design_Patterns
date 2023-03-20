package Matching;

import DP_Templates_Def.TDef_Edge;
import General.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 *
 * @author ed
 */
public class Solution {

    private String name_DP;
    private HashMap<String, String> matchesSystemToDP;
    // key is classInterface name of System and value is  
    // a classInterface name of the design pattern.
    
    private ArrayList<String> feedback;

    Solution(String name_DP,
            ArrayList<String> allSystemClasses) {

        this.name_DP = name_DP;
        matchesSystemToDP = new HashMap();
        feedback = new ArrayList();

        for (String s : allSystemClasses) {
            matchesSystemToDP.put(s, Constants.EMPTY);
        }
    }

    private Solution(String name_DP) {
        // only to be used by clone()
        this.name_DP = name_DP;
        matchesSystemToDP = new HashMap();
        feedback = new ArrayList();
    }

    void addMatch(String className_System,
            String className_DP,
            String comment) {
        matchesSystemToDP.put(className_System, className_DP);       

        if ((comment != null) && comment.length() > 0) {
            feedback.add(comment);
        } 
        
    }
    
    void removeMatch(String className_System) {
        String dp_name;
 
        matchesSystemToDP.put(className_System, Constants.EMPTY);
    }
    

    boolean alreadyMatched(TDef_Edge dp_edge, TDef_Edge system_edge) {
        // a dp_edge can already be matched when it is part
        // of repeating group or a peninsula.
        String key1SystemName, key2SystemName, edgeType;

        key1SystemName = system_edge.getNode1Name();
        key2SystemName = system_edge.getNode2Name();
        edgeType = system_edge.getEdgeTypeStr();

        return dp_edge.equals(new TDef_Edge(
                matchesSystemToDP.get(key1SystemName),
                matchesSystemToDP.get(key2SystemName),
                edgeType));
    }

    public boolean isMatchedDPName(String dp_name) {
        return matchesSystemToDP.containsValue(dp_name);
        
    }

    public boolean isMatchedSysName(String sys_name) {
        if (!matchesSystemToDP.containsKey(sys_name)) {
            Global_Debug_Info.println("Solution: KLOPT DIT?? " + sys_name + 
                    " komt niet voor als key isMatchedSysName()");
            // This occurs when sys_name is a standard type or class 
            // such as int, double, String, ...
            
            return true; // MOET DIT FALSE ZIJN ????
                         // KLOPT ECHT !! 17 nov 2022
                         // Leesbaarheid verbeteren.
        }
        
        return !matchesSystemToDP.get(sys_name).equals(Constants.EMPTY);
    }

    public String getMatchedSysName(String sys_name) {
        // if sys_name is int, double, boolean, String ...
        // then null will be returned.
        
        // sys_name --> DP_name
        // return DP_name
        
        return matchesSystemToDP.get(sys_name);
    }

    public String getMatchedDPName(String dp_name) {
        // sys_name --> DP_name
        // return sys_name
        
        for (String key : matchesSystemToDP.keySet()) {
            if (matchesSystemToDP.get(key).equals(dp_name)) {
                return key;
            }
        }

        Global_Feedback.showOrWite(1, dp_name + " is not matched to a system class in getMatchedDPName()");

        return Constants.EMPTY;
    }

    public ArrayList<String> getMatchedSysNames(String dp_name) {
        ArrayList<String> result;

        result = new ArrayList();

        for (String key : matchesSystemToDP.keySet()) {
            if (matchesSystemToDP.get(key).equals(dp_name)) {
                result.add(key);
            }
        }

        return result;
    }

    public boolean isMatchedTo(String sys_name, String dp_name) {
        if (matchesSystemToDP.containsKey(sys_name)) {
            return matchesSystemToDP.get(sys_name).equals(dp_name);
        }

        return false;
    }

    public boolean noMatches() {
        if (matchesSystemToDP.isEmpty()) {
            Global_Feedback.showOrWite(0, "Solution: CAN NOT HAPPEN ...");
            System.exit(1);
            
            return true;
            // Makes compiler happy.
        }
        
        return false;    
    }
    
    public boolean noValues() {
        for (String k: matchesSystemToDP.keySet())
            if (!matchesSystemToDP.get(k).equals(Constants.EMPTY))
                return false;
        
        return true;
    }
    
    public void clearMatches() {        
        for (String key: matchesSystemToDP.keySet()) {
            matchesSystemToDP.put(key, Constants.EMPTY);
        }
    }

    @Override
    public Solution clone() {
        Solution solution = new Solution(name_DP);

        for (String name : matchesSystemToDP.keySet()) {
            solution.matchesSystemToDP.put(name, matchesSystemToDP.get(name));
        }

        solution.feedback.addAll(feedback);

        return solution;
    }

    void checkAndCorrect(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges) {

        ArrayList<String> sys_names1, sys_names2;
        boolean solutionChanged, dp_edge_matched, tmpBool;
        HashSetEdges sysEdgesSet; // for fast search

        sysEdgesSet = new HashSetEdges(allSystem_Edges);

        solutionChanged = true;
        while (solutionChanged) {
            solutionChanged = checkDP_edges(allDP_Edges, allSystem_Edges,
                    sysEdgesSet);

            tmpBool = checkDP_edges(allDP_Edges, allSystem_Edges,
                    sysEdgesSet);

            for (TDef_Edge dp_edge : allDP_Edges) {
                // Check whether there is an corresponding matched sys_edge
                // for dp_edge.

                sys_names1 = getMatchedSysNames(dp_edge.getNode1Name());
                sys_names2 = getMatchedSysNames(dp_edge.getNode2Name());

                dp_edge_matched = false;
                for (String sys_name2 : sys_names2) {
                    // Check whether there is an corresponding matched 
                    // sys_edge <..., sys_name2> 

                    for (String sys_name1 : sys_names1) {
                        if (sysEdgesSet.contains(sys_name1, sys_name2,
                                dp_edge.getEdgeTypeInt())) {
                            dp_edge_matched = true;
                            //break;
                        }
                    }

                    if (!dp_edge_matched) {
                        // MAYBE a sys_class could be added
                        // NOT YET IMPLEMENTED
                        
                        matchesSystemToDP.put(sys_name2, Constants.EMPTY);
                        solutionChanged = true;
                    }
                }
            }

            boolean tmp = checkMatches(allDP_Edges, allSystem_Edges,
                    sysEdgesSet);

            solutionChanged = solutionChanged || tmp;
        }


        /*   
            // Veiligheidshalve:
	    heeft elke class in DP een overeenkomstige class in System?

            Zo ja:
               toon oplossing;
 
               return (true, solution);
	    Zo Nee:
	       return (false, )
         */
    }

    private boolean checkDP_edges(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges,
            HashSetEdges sysEdgesSet) {

        boolean solutionChanged, tmpBool;
        ArrayList<String> sys_names1, sys_names2;

        solutionChanged = false;

        for (TDef_Edge dp_edge : allDP_Edges) {
            // Check whether there is an corresponding matched sys_edge 
            // for dp_edge.
            sys_names1 = getMatchedSysNames(dp_edge.getNode1Name());
            sys_names2 = getMatchedSysNames(dp_edge.getNode2Name());

            tmpBool = checkDP_edgesForName1(sys_names1, sys_names2,
                    dp_edge.getEdgeTypeInt(), sysEdgesSet);
            
            solutionChanged = solutionChanged || tmpBool;

            tmpBool = checkDP_edgesForName2(sys_names1, sys_names2,
                    dp_edge.getEdgeTypeInt(), sysEdgesSet);
            
            solutionChanged = solutionChanged || tmpBool;
        }

        return solutionChanged;
    }

    private boolean checkDP_edgesForName1(ArrayList<String> sys_names1,
            ArrayList<String> sys_names2, int edgeType,
            HashSetEdges sysEdgesSet) {

        boolean solutionChanged, dp_edge_matched;

        solutionChanged = false;
        dp_edge_matched = false;

        for (String sys_name1 : sys_names1) {
            // Check whether there is an corresponding matched 
            // sys_edge <..., sys_name2> 

            for (String sys_name2 : sys_names2) {
                if (sysEdgesSet.contains(sys_name1, sys_name2,
                        edgeType)) {
                    dp_edge_matched = true;
                    break;
                }
            }

            if (!dp_edge_matched) {
                // MAYBE a sys_class could be added
                // NOT YET IMPLEMENTED
                
                matchesSystemToDP.put(sys_name1, Constants.EMPTY);
                solutionChanged = true;
            }
        }

        return solutionChanged;
    }

    private boolean checkDP_edgesForName2(ArrayList<String> sys_names1,
            ArrayList<String> sys_names2, int edgeType,
            HashSetEdges sysEdgesSet) {

        boolean solutionChanged, dp_edge_matched;

        solutionChanged = false;
        dp_edge_matched = false;
       
        for (String sys_name2 : sys_names2) {
            // Check whether there is an corresponding matched 
            // sys_edge <..., sys_name2> 

            for (String sys_name1 : sys_names1) {
                if (sysEdgesSet.contains(sys_name1, sys_name2,
                        edgeType)) {
                    dp_edge_matched = true;
                    break;
                }
            }

            if (!dp_edge_matched) {
                // MAYBE a sys_class could be added
                // NOT YET IMPLEMENTED
                
                matchesSystemToDP.put(sys_name2, Constants.EMPTY);
                solutionChanged = true;
            }
        }

        return solutionChanged;
    }

    private boolean checkMatches(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges,
            HashSetEdges sysEdgesSet) {

        // Check for every matched class in System if it occurs in every 
        // related dp_edge.
        
        String dp_class;
        boolean solutionChanged;

        solutionChanged = false;

        for (String sys_class : matchesSystemToDP.keySet()) {
            boolean tmpBool;

            dp_class = matchesSystemToDP.get(sys_class);

            for (TDef_Edge dp_edge : allDP_Edges) {
                if (dp_edge.getNode1Name().equals(dp_class)) {
                    tmpBool = checkMatchesAndCorrectForName1(dp_class, 
                            allDP_Edges, sys_class, sysEdgesSet);
                    solutionChanged = solutionChanged || tmpBool;
                }

                if (dp_edge.getNode2Name().equals(dp_class)) {
                    tmpBool = checkMatchesAndCorrectForName2(dp_class, 
                            allDP_Edges, sys_class, sysEdgesSet);
                    solutionChanged = solutionChanged || tmpBool;
                }
            }
        }

        return solutionChanged;
    }

    private boolean checkMatchesAndCorrectForName1(String dp_class,
            ArrayList<TDef_Edge> allDP_Edges,
            String sys_class,
            HashSetEdges sysEdgesSet) {

        boolean found, solutionChanged;

        solutionChanged = false;
        //System.out.println("++ " + dp_class.toString());

        for (TDef_Edge dp_edge : allDP_Edges) {
            //System.out.println("-- " + dp_edge);
            

            //System.out.println("node 1 = " + dp_edge.getNode1Name());

            if (dp_edge.getNode1Name().equals(dp_class)) {
                found = false;

                // There should be an corresponding sys_edge
                //System.out.println("Zoek node2 = " + dp_edge.getNode2Name());
                for (String candidate : 
                        getMatchedSysNames(dp_edge.getNode2Name())) {
                    //System.out.println("## candidate " + candidate);
                    if (sysEdgesSet.contains(sys_class, candidate, 
                            dp_edge.getEdgeTypeInt())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    //System.out.println("Match incomplete, second class in edge " 
                    //        + dp_edge + " is missing, " + 
                    //        sys_class + " is removed." );
                    matchesSystemToDP.put(sys_class, Constants.EMPTY);
                    solutionChanged = true;
                }
            }
        }

        return solutionChanged;
    }

    private boolean checkMatchesAndCorrectForName2(String dp_class,
            ArrayList<TDef_Edge> allDP_Edges,
            String sys_class,
            HashSetEdges sysEdgesSet) {

        boolean found, solutionChanged;

        solutionChanged = false;
        for (TDef_Edge dp_edge : allDP_Edges) {

            if (dp_edge.getNode2Name().equals(dp_class)) {
                found = false;

                // There should be an corresponding sys_edge
                for (String candidate : 
                        getMatchedSysNames(dp_edge.getNode1Name())) {
                    
                    if (sysEdgesSet.contains(candidate, sys_class, 
                            dp_edge.getEdgeTypeInt())) {
                        found = true;
                    }
                }

                if (!found) {
                    //System.out.println("Match incomplete, first class in edge " 
                    //        + dp_edge + " is missing, " + 
                    //        sys_class + " is removed." );
                    matchesSystemToDP.put(sys_class, Constants.EMPTY);
                    solutionChanged = true;
                }
            }
        }

        return solutionChanged;
    }

    @Override
    public String toString() {
        String result;
        int maxLengte;
        TreeMap<String, String> sortedMatches;

        result =  "Design pattern: " + name_DP + "\n";
        result += "===============\n";
        
        sortedMatches = new TreeMap(matchesSystemToDP);
        maxLengte = "System names".length();

        for (String k : sortedMatches.keySet()) {
            if (k.length() > maxLengte) {
                maxLengte = k.length();
            }
        }
        
        maxLengte += 2;
        result += addSpaces("System names", maxLengte);
        result += "--> Template names\n";

        for (String k : sortedMatches.keySet()) {
            if (!sortedMatches.get(k).equals(Constants.EMPTY)) {
                result += addSpaces(k, maxLengte);
                result += "--> " + sortedMatches.get(k) + "\n";                
            }
        }

        for (int i = 0; i < feedback.size(); i++) {
            result += feedback.get(i) + "\n";
        }

        return result;
    }
    
    private String addSpaces(String str, int maxLengte) {
        for (int i = str.length() + 1; i < maxLengte; i++) {
                    str += " ";
        }
        
        return str;
    }
    
    int aantalToegewezenKeys() {
        int teller = 0;
        
        for (String key: matchesSystemToDP.keySet()) {
            if ( !matchesSystemToDP.get(key).equals(Constants.EMPTY))
                teller++;
        }
        
        return teller;
    }
}
