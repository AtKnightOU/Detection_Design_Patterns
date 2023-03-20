package Matching;

import DP_Templates_Def.*;
import General.Constants;
import General.Global_Feedback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author ed
 */
public class SearchDesignPatterns {

    private TDef_TemplateContent templateContent_DP = null;
    private TDef_TemplateContent templateContent_System = null;

    public void searchDPs(TDef_Templates templatesDP, TDef_Template templateSystem) {
        // templatesDP are templates of design patterns.
        // templatesSystem contains one template representing the system of  
        // interest that may contain design patterns.

        // TDef_TemplateContent templateContent_DP;
        // TDef_TemplateContent templateContent_System;
        Solution solution;
        SolutionResult new_solution;

        templateContent_System = templateSystem.getTemplateContent();
        templateContent_System.getAllEdgesOrdered();
        // This method also checks wether the classes form an connected graph.
        // If not, feedback is generated.

        for (TDef_Template template_designDP : templatesDP.getTemplates()) {
            Global_Feedback.showOrWite(1, "\nSearch template: " + template_designDP.getName());

            System.gc();
            // may improve the performance

            templateContent_DP = template_designDP.getTemplateContent();

            solution = new Solution(template_designDP.getName(),
                    templateContent_System.getAllClassNames());

            if (templateContent_DP.getAllEdges().size() == 0) {
                // Singleton ??
                new_solution = matchNoEdges(templateContent_DP,
                        templateContent_System,
                        solution);

                if (!new_solution.isMatched()) {
                    toonNietHerkend();
                }
            } else {
                templateContent_System.setAllEdgesNotMatched();
                templateContent_DP.setAllEdgesNotMatched();

                new_solution = matchTemplateContent(templateContent_DP.getAllEdgesOrdered(),
                        templateContent_System.getAllEdgesOrdered(), 0, solution);

            }

            if (new_solution.isMatched()) {
                Global_Feedback.showOrWite(1, new_solution.getSolution().toString());
                Global_Feedback.showOrWite(1, "Fase: Extra check match of classes");

                ClassesMatch laatsteControle = new ClassesMatch();

                if (laatsteControle.classesMatch(new_solution.getSolution(),
                        templateContent_DP, templateContent_System)) {
                    toonHerkend(new_solution.getSolution());
                    //System.out.println("Herkend " + template_designDP.getName());
                } else {
                    System.out.println("Helaas " + template_designDP.getName());
                    toonNietHerkend();
                    Global_Feedback.showOrWite(1, "==================================\n\n");
                }

                
            }
        }

        // System.out.println("");

        /* COMMENTAAR NOG VERBETEREN resultaat: lijst met overeenkomsten (inclusief fouten/feedback)
         *  gesorteerd op mate van overeenkomst.
         *  maxFouten is o.a. het aantal edges dat niet gematched kon worden.
         */
        Global_Feedback feedbacks = Global_Feedback.getInstance();

        // System.out.println("FEEDBACK:");
        // for (String s : feedbacks.allFeedback()) {
        //     System.out.println(s);
        //}
        //System.out.println("-----------------------------------------------------------------\n\n");
    }

    private void toonNietHerkend() {
        Global_Feedback.showOrWite(1, "HELAAS: classes komen NIET overeen");
        Global_Feedback.showOrWite(1, "==================================");
    }

    private void toonHerkend(Solution solution) {
        Global_Feedback.showOrWite(1, "\nClasses match!\n");
        Global_Feedback.showOrWite(0, "Design pattern detected.");
        Global_Feedback.showOrWite(0, solution.toString());
        Global_Feedback.showOrWite(0, "======================================\n\n");
        // Global_Feedback.showOrWite(1, "SUCCES: Classes match!!");
    }

    SolutionResult matchNoEdges(TDef_TemplateContent templateContent_DP,
            TDef_TemplateContent templateContent_System,
            Solution solution) {
        // The template which has no edges should have precise one Class.

        ArrayList<TDef_Class> dp_classes;
        TDef_Class dp_class;
        boolean addedMatch;

        dp_classes = templateContent_DP.getAllClasses();

        if (dp_classes.size() != 1) {
            Global_Feedback.showOrWite(0, "FOUT: Waarschijnlijk heeft het template 0 of meer dan een class.");
            return new SolutionResult(false, solution);
        }

        dp_class = dp_classes.get(0);

        for (TDef_Class sys_class : templateContent_System.getAllClasses()) {

            if (!solution.isMatchedSysName(sys_class.getClassName())) {
                solution.addMatch(sys_class.getClassName(), dp_class.getClassName(), "");
                addedMatch = true;
            } else {
                addedMatch = false;
            }

            if (dp_class.isMatch(sys_class, solution)) {
                return new SolutionResult(true, solution);
            }

            if (addedMatch) {
                // Overbodig Global_Feedback.showOrWite(0, "");
                solution.removeMatch(sys_class.getClassName());
            }
        }

        return new SolutionResult(false, solution);
    }

    SolutionResult matchTemplateContent(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges,
            int dpIndex, Solution solution) {

        TDef_Edge dp_edge, system_edge;
        Solution cloneSolution;
        SolutionResult solutionResult;

        if (solutionTemplateFound(allDP_Edges, allSystem_Edges, dpIndex, solution)) {
            // 8 juni
            // return new SolutionResult(true, solution);
            return new SolutionResult(false, solution);
        }

        dp_edge = allDP_Edges.get(dpIndex);
        // dp_edge that have to be matched in this method.

        if (dp_edge.isMatched()) {
            return matchTemplateContent(allDP_Edges, allSystem_Edges,
                    dpIndex + 1, solution);
        }

        cloneSolution = solution.clone();

        for (int sysIndex = 0; sysIndex < allSystem_Edges.size(); sysIndex++) {
            system_edge = allSystem_Edges.get(sysIndex);

            int teller;
            teller = 0;
            for (TDef_Edge edge : allSystem_Edges) {
                if (edge.isMatched()) {
                    teller++;
                    //System.out.println("sys_edge " + edge);
                }
            }
            //System.out.println("aantal gematchte Sys_edges " + teller);

            teller = 0;
            for (TDef_Edge edge : allDP_Edges) {
                if (edge.isMatched()) {
                    teller++;
                }
            }
            //System.out.println("aantal gematchte DP_edges " + teller);

            if (teller == 0) {
                for (TDef_Edge edge : allSystem_Edges) {
                    if (edge.isMatched()) {
                        edge.setMatched(false);
                        //System.out.println("DIRTY correct error");
                    }
                }
            }

            //System.out.println("aantal toegewezen keys " + solution.aantalToegewezenKeys());
            if (system_edge.isMatched()) {
                continue;
                // Try next system_edge
            }

            if (solution.alreadyMatched(dp_edge, system_edge)) {
                // Classes in dp_edge and system_edge coincidently 
                // already matched by earlier matching.

                system_edge.setMatched(true);
                dp_edge.setMatched(true);

                solutionResult = matchTemplateContent(allDP_Edges,
                        allSystem_Edges, dpIndex + 1, cloneSolution);

                if (!solutionResult.isMatched()) {
                    system_edge.setMatched(false);
                    dp_edge.setMatched(false);
                }

                return solutionResult;
                // ===================
            }

            if (dp_edge.isPartOfrepeatingGroup()) {
                solutionResult = matchRepeatingGroup(allDP_Edges, allSystem_Edges,
                        dpIndex, dp_edge, sysIndex, solution);

                // dp_edge must be part of the repeating_group.
                // So, there are two final posibilities (matched / not matched).
                if (!solutionResult.isMatched()) {
                    system_edge.setMatched(false);
                    dp_edge.setMatched(false);
                }

                return solutionResult;
                // ===================
            }

            solutionResult = matchRecursiveEdge(allDP_Edges, allSystem_Edges,
                    dpIndex, dp_edge, system_edge, solution);

            if (solutionResult.isMatched()) {
                return solutionResult;
                // ===================
            }

            // Try to match dp_edge with another system_edge
            system_edge.setMatched(false);
            dp_edge.setMatched(false);

        }    // next system_edge   

        // No system_edge match with dp_edge
        return new SolutionResult(false, cloneSolution);
    }

    private boolean solutionTemplateFound(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges,
            int dpIndex, Solution solution) {

        int teller = 0;

        for (TDef_Edge tmp_edge : allDP_Edges) {
            if (tmp_edge.isMatched()) {
                teller++;
            }
        }

        //System.out.println("aantal gematchte dp_edges: " + teller + " van de " + allDP_Edges.size());
        // System.out.println("dp_edges:");
        if (teller >= allDP_Edges.size()) {
            /*
            System.out.println("Controleren en corrigeren oplossing");
            for (TDef_Edge edge : allDP_Edges) {
                if (edge.isMatched()) {
                   System.out.println(edge);
                }
            }
            
            System.out.println("sys_edges:");
            for (TDef_Edge edge : allSystem_Edges) {
                if (edge.isMatched()) {
                    System.out.println(edge);
                }
            }
             */

            solution.checkAndCorrect(allDP_Edges, allSystem_Edges);
            // START !!

            if (solution.noValues()) {
                return true;
            }

            Global_Feedback.showOrWite(1, "Einde Fase 1: structuurovereenkomst gevonden.");

            // 8 juni
            Global_Feedback.showOrWite(1, solution.toString());

            Global_Feedback.showOrWite(1, "Start Fase 2: controle per class");
            ClassesMatch laatsteControle = new ClassesMatch();

            if (laatsteControle.classesMatch(solution,
                    templateContent_DP, templateContent_System)) {
                toonHerkend(solution);
                // Is verplaatst naar tooonhHerkend()
                // Global_Feedback.showOrWite(1, "\nClasses match!\n");
                // Global_Feedback.showOrWite(0, "Design pattern detected.");
                // Global_Feedback.showOrWite(0, solution.toString());
                // Global_Feedback.showOrWite(0, "======================================\n\n");
            } else {
                Global_Feedback.showOrWite(1, "\nHELAAS classes komen NIET overeen.");
                Global_Feedback.showOrWite(1, "Dit is NIET het gezochte design pattern.");
                Global_Feedback.showOrWite(1, "========================================\n\n");
            }

            return true;
        }

        if (dpIndex >= allDP_Edges.size()) {
            Global_Feedback.showOrWite(0, "solutionTemplateFound: SHOULD NOT OCCUR. REPORT THIS ERROR");
            System.exit(1);

            return true;
        }

        return false;
    }

    SolutionResult matchRepeatingGroup(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges, int dpIndex,
            TDef_Edge dp_edge, int sysIndex, Solution solution) {

        ArrayList<TDef_Edge> rpgDP_Edges;
        ArrayList<TDef_Edge> changedSystem_Edges;
        SolutionResult solutionResult;
        Solution growingSolutions;
        boolean rpgSolutionFound;

        rpgDP_Edges = dp_edge.getRpgDp_Edges(allDP_Edges);
        changedSystem_Edges = new ArrayList();

        rpgSolutionFound = false;

        growingSolutions = solution.clone();

        while (sysIndex < allSystem_Edges.size()) {
            // Notice sysIndex is also incremented in the in the caller: 
            // the method matchTemplateContent()

            solutionResult = matchRecursiveRepeatingGroup(rpgDP_Edges, dp_edge,
                    allSystem_Edges, changedSystem_Edges,
                    0, sysIndex, solution);

            if (solutionResult.isMatched()) {
                rpgSolutionFound = true;
                growingSolutions = solutionResult.getSolution().clone();
            }

            solution = growingSolutions.clone();

            sysIndex++;
        }

        if (rpgSolutionFound) {
            solution = growingSolutions.clone();

            for (TDef_Edge tmp_edge : rpgDP_Edges) {
                if (!tmp_edge.isMatched()) {
                    tmp_edge.setMatched(true);
                }
                // By this statment are the corresponding allDP_Edges setMatched(true)

            }

            SolutionResult tmp;

            tmp = matchTemplateContent(allDP_Edges, allSystem_Edges, dpIndex + 1,
                    solution);

            if (tmp.isMatched()) {
                // A complete solution is found.
                return tmp;
                // ========
            }

            // Although the necessary repeating group is found, 
            // no complete solution could be found. 
            // Trying to match dp_edge with another system_edge 
            // is not useful because dp_ege have to be part 
            // of the repeating group. 
            //  Repeating group that was searched for could not be found.
            for (TDef_Edge tmp_edge : rpgDP_Edges) {
                tmp_edge.setMatched(false);
            }

            for (TDef_Edge tmp_edge : changedSystem_Edges) {
                tmp_edge.setMatched(false);
            }
        }

        return new SolutionResult(false, solution);
    }

    private SolutionResult matchRecursiveEdge(ArrayList<TDef_Edge> allDP_Edges,
            ArrayList<TDef_Edge> allSystem_Edges,
            int dpIndex, TDef_Edge dp_edge, TDef_Edge system_edge,
            Solution solution) {

        Solution cloneSolution;
        SolutionResult tmpSolutionResult;
        HashMap<String, String> resultMatch;
        Set<String> keySet;

        resultMatch = matchEdges(dp_edge, system_edge, solution);

        keySet = resultMatch.keySet();

        if (!keySet.isEmpty()) {
            cloneSolution = solution.clone();

            system_edge.setMatched(true);
            dp_edge.setMatched(true);

            for (String key : keySet) {
                cloneSolution.addMatch(key, resultMatch.get(key), "");
            }

            tmpSolutionResult = matchTemplateContent(allDP_Edges,
                    allSystem_Edges, dpIndex + 1, cloneSolution);

            if (!tmpSolutionResult.isMatched()) {
                system_edge.setMatched(false);
                dp_edge.setMatched(false);
            }

            return tmpSolutionResult;
        }

        system_edge.setMatched(false);
        // probably not necessary.

        return new SolutionResult(false, solution);
    }

    SolutionResult matchRecursiveRepeatingGroup(
            ArrayList<TDef_Edge> allRpgDp_Edges, TDef_Edge dp_edge,
            ArrayList<TDef_Edge> allSystem_Edges,
            ArrayList<TDef_Edge> changedSystem_Edges,
            int rpgDpIndex,
            int parSysIndex, Solution solution) {

        TDef_Edge rpgDp_edge;
        TDef_Edge system_edge;
        HashMap<String, String> resultMatch;
        Set<String> keySet;
        Solution cloneSolution;

        if (repeatingGroupFound(allRpgDp_Edges, dp_edge, rpgDpIndex)) {
            return new SolutionResult(true, solution);
        }

        cloneSolution = solution.clone();

        rpgDp_edge = (rpgDpIndex == 0) ? dp_edge : allRpgDp_Edges.get(rpgDpIndex);

        // The first edge that has to be mathced is dp_edge.
        // This is realized in TDef_edge: getRpgDp_Edges()
        for (int sysIndex = parSysIndex; sysIndex < allSystem_Edges.size(); sysIndex++) {
            system_edge = allSystem_Edges.get(sysIndex);

            if (system_edge.isMatched()) {
                continue;
                // try next system_edge
            }

            if (solution.alreadyMatched(rpgDp_edge, system_edge)) {
                // Classes in dp_edge and system_edge already .

                SolutionResult tmpSolutionResult;

                system_edge.setMatched(true);
                changedSystem_Edges.add(system_edge);

                tmpSolutionResult = matchRecursiveRepeatingGroup(allRpgDp_Edges,
                        dp_edge,
                        allSystem_Edges, changedSystem_Edges,
                        rpgDpIndex + 1,
                        0,
                        cloneSolution);

                if (!tmpSolutionResult.isMatched()) {
                    system_edge.setMatched(false);
                    rpgDp_edge.setMatched(false);
                }

                return tmpSolutionResult;
                // ======================
            }

            resultMatch = matchRPGedges(rpgDp_edge, system_edge, solution);

            keySet = resultMatch.keySet();

            if (!keySet.isEmpty()) {
                SolutionResult tmpResult;

                cloneSolution = solution.clone();

                system_edge.setMatched(true);

                for (String key : keySet) {
                    cloneSolution.addMatch(key, resultMatch.get(key), "");
                }

                tmpResult = matchRecursiveRepeatingGroup(allRpgDp_Edges,
                        dp_edge,
                        allSystem_Edges, changedSystem_Edges,
                        rpgDpIndex + 1, 0,
                        cloneSolution);

                if (!tmpResult.isMatched()) {
                    system_edge.setMatched(false);
                    rpgDp_edge.setMatched(false);
                }

                return tmpResult;
            }

            // Try to match dp_edge with next systemEdge.
        }

        // All system_edges are tried to be matched, without success,
        // so there is no solution
        return new SolutionResult(false, cloneSolution);
    }

    private boolean repeatingGroupFound(ArrayList<TDef_Edge> allRpgDp_Edges,
            TDef_Edge dp_edge, int rpgDpIndex) {

        if (rpgDpIndex >= allRpgDp_Edges.size()) {
            return true;
        }

        if (!dp_edge.equals(allRpgDp_Edges.get(0))) {
            Global_Feedback.showOrWite(0, "repeatingGroupFound: Error: " + dp_edge
                    + " should be equal to " + allRpgDp_Edges.get(0));
        }

        return false;
    }

    private HashMap<String, String> matchEdges(TDef_Edge dp_edge,
            TDef_Edge system_edge,
            Solution solution) {

        HashMap<String, String> result;

        result = new HashMap();

        if (!edgeTypeMatch(dp_edge.getEdgeTypeStr(),
                system_edge.getEdgeTypeStr())) {

            return result;
        }

        String classInterfaceDPName1, classInterfaceDPName2;
        String classInterfaceSysName1, classInterfaceSysName2;
        String matchedName1, matchedName2;

        classInterfaceDPName1 = dp_edge.getNode1Name();
        classInterfaceDPName2 = dp_edge.getNode2Name();
        classInterfaceSysName1 = system_edge.getNode1Name();
        classInterfaceSysName2 = system_edge.getNode2Name();

        // DP_names and Sys_names are not matched to each other,
        // but can be matched to other classes.
        // This is checked in alreadyMatched().
        // Names of classInterface of DP and System can only be matched
        // when both Name1's or both Name2's are not matched.
        // DP_name1 and Sys_name1 are both not matched and
        // DP_name2 is matched to Sys_name2;
        if ((!solution.isMatchedDPName(classInterfaceDPName1))
                && (!solution.isMatchedSysName(classInterfaceSysName1))
                && (solution.isMatchedDPName(classInterfaceDPName2))
                && (solution.isMatchedSysName(classInterfaceSysName2))
                && (solution.getMatchedDPName(classInterfaceDPName2).equals(classInterfaceSysName2))) {

            result.put(classInterfaceSysName1, classInterfaceDPName1);

            return result;
        }

        // DP_name1 is matched to Sys_name1 and
        // DP_name2, Sys_name2 are both not matched 
        if ((solution.isMatchedDPName(classInterfaceDPName1))
                && (solution.isMatchedSysName(classInterfaceSysName1))
                && (solution.getMatchedDPName(classInterfaceDPName1).equals(classInterfaceSysName1))
                && (!solution.isMatchedDPName(classInterfaceDPName2))
                && (!solution.isMatchedSysName(classInterfaceSysName2))) {

            result.put(classInterfaceSysName2, classInterfaceDPName2);

            return result;
        }

        // DP_name1, DP_name2, Sys_name1 and Sys_name2 are not matched
        if ((!solution.isMatchedDPName(classInterfaceDPName1))
                && (!solution.isMatchedSysName(classInterfaceSysName1))
                && (!solution.isMatchedDPName(classInterfaceDPName2))
                && (!solution.isMatchedSysName(classInterfaceSysName2))) {

            boolean eq1, eq2;

            // if a class is associated to itself then the matched
            // classes also have to be associated to itself.
            // DP_name1, DP_name2, Sys_name1 and Sys_name2 are not matched
            // This may only happen when this is the first edge that has to be matched.
            eq1 = classInterfaceDPName1.equals(classInterfaceDPName2);
            eq2 = classInterfaceSysName1.equals(classInterfaceSysName2);
            if (eq1 == eq2) {
                result.put(classInterfaceSysName1, classInterfaceDPName1);
                result.put(classInterfaceSysName2, classInterfaceDPName2);
            }

            return result;
        }

        // In all other cases a match is not possible;
        return result; // empty hashmap
    }

    private HashMap<String, String> matchRPGedges(TDef_Edge dp_edge,
            TDef_Edge system_edge,
            Solution solution) {

        // matchEdges is called!!
        // The Three complicated boolean expressions are slightly different
        // from the booleans expressions in matchEdges.
        HashMap<String, String> result;

        result = new HashMap();

        if (!edgeTypeMatch(dp_edge.getEdgeTypeStr(),
                system_edge.getEdgeTypeStr())) {
            return result;
        }

        result = matchEdges(dp_edge, system_edge, solution);

        if (!result.isEmpty()) {
            return result;
        }

        //return new HashMap();
        // Code could be improved
        String classInterfaceDPName1, classInterfaceDPName2;
        String classInterfaceSysName1, classInterfaceSysName2;
        String matchedName1, matchedName2;

        classInterfaceDPName1 = dp_edge.getNode1Name();
        classInterfaceDPName2 = dp_edge.getNode2Name();
        classInterfaceSysName1 = system_edge.getNode1Name();
        classInterfaceSysName2 = system_edge.getNode2Name();

        // Above DP_names and Sys_names are not matched to each other.
        // This is checked in alreadyMatched().     
        // DP_name1 and Sys_name1 are both not matched and
        // Sys_name2 is matched to DP_name2;
        // Notice names of classes outsite a RPG may be matched only once.
        if ((solution.isMatchedDPName(classInterfaceDPName1))
                && (!solution.isMatchedSysName(classInterfaceSysName1))
                && (solution.isMatchedDPName(classInterfaceDPName2))
                && (solution.isMatchedSysName(classInterfaceSysName2))
                && (solution.isMatchedTo(classInterfaceSysName2, classInterfaceDPName2))
                && (classInRpg(classInterfaceDPName1))) {

            result.put(classInterfaceSysName1, classInterfaceDPName1);

            return result;
        }

        // DP_name1 is matched to Sys_name1 and
        // DP_name2, Sys_name2 are both not matched 
        if ((solution.isMatchedDPName(classInterfaceDPName1))
                && (solution.isMatchedSysName(classInterfaceSysName1))
                && (solution.isMatchedTo(classInterfaceSysName1, classInterfaceDPName1))
                && (solution.isMatchedDPName(classInterfaceDPName2))
                && (!solution.isMatchedSysName(classInterfaceSysName2))
                && (classInRpg(classInterfaceDPName2))) {

            result.put(classInterfaceSysName2, classInterfaceDPName2);

            return result;
        }

        // DP_name1, DP_name2, Sys_name1 and Sys_name2 are not matched
        // This may only happen when this is the first edge that has to be matched.
        if (solution.noMatches()
                && (solution.isMatchedDPName(classInterfaceDPName1))
                && (!solution.isMatchedSysName(classInterfaceSysName1))
                && (solution.isMatchedDPName(classInterfaceDPName2))
                && (!solution.isMatchedSysName(classInterfaceSysName2))) {

            result.put(classInterfaceSysName1, classInterfaceDPName1);
            result.put(classInterfaceSysName2, classInterfaceDPName2);

            return result;
        }

        // In all other cases a match is not possible;
        return result; // empty hashmap  
    }

    private boolean edgeTypeMatch(String dpType, String sysType) {
        if (dpType.equals(sysType)) {
            return true;
        }

        return dpType.equals(Constants.ABSTRACT_INTERFACE)
                && (sysType.equals(Constants.ABSTRACT)
                || sysType.equals(Constants.INHERITANCE));
    }

    private boolean classInRpg(String className) {
        return templateContent_DP.classInRpg(className);
    }
}
