package DP_Templates_Def;

import General.Constants;
import General.Global_Debug_Info;
import General.Global_Feedback;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author E.M. van Doorn
 */
public class TDef_TemplateContent {

    private ArrayList<TDef_Class> classes;
    private ArrayList<TDef_Peninsula> peninsulas;
    private ArrayList<TDef_RepeatingGroup> repeatingGroups;
    private ArrayList<TDef_Edge> edges;
    private TDef_TemplateContent parentContent;
    private boolean partOfRepeatingGroup, partOfPeninsula;

    public TDef_TemplateContent() {
        classes = new ArrayList();
        peninsulas = new ArrayList();
        repeatingGroups = new ArrayList();
        edges = new ArrayList();
        parentContent = null;
        partOfRepeatingGroup = false;
        partOfPeninsula = false;
    }

    public void addClass(TDef_Class tdef_class) {
        classes.add(tdef_class);
        tdef_class.setParentContent(this);
    }

    public void addPeninsula(TDef_Peninsula peninsula) {
        peninsulas.add(peninsula);
        peninsula.setParentContent(this);
    }

    public void addRepeatingGroup(TDef_RepeatingGroup repeatingGroup) {
        repeatingGroups.add(repeatingGroup);
        repeatingGroup.setParentContent(this);
    }

    public void addEdge(TDef_Edge edge) {
        // is called from some Elements from SAX-parser
        
        // Notice Java sources do not contain an association 
        // but a directed association.
        // A template or a design can contain an association.
        
        if (edge.getEdgeTypeStr().equals(Constants.ASSOCIATION)){
            TDef_Edge edge1, edge2;
            
            edge1 = new TDef_Edge(edge.getNode1Name(), edge.getNode2Name(), 
                                  Constants.ASSOCIATION_DIRECTED);
            edge2 = new TDef_Edge(edge.getNode2Name(), edge.getNode1Name(), 
                                  Constants.ASSOCIATION_DIRECTED);
            addEdgeSecond(edge1);
            addEdgeSecond(edge2);
        }
        else {
            addEdgeSecond(edge);
        }    
    }
    
    
    private void addEdgeSecond(TDef_Edge edge) {
       if (!edges.contains(edge)) 
        // Prevents mutiple occurres of one edge.
        // This may occur by declaring more than one attribute of one class.
        {
            edge.setParentContent(this);

            if (edge.isInheritance()) {
                if (isNecessaryInHeritance(edge)) {
                    edges.add(edge);
                }
            } else {
                edges.add(edge);
            }
        } 
    }
    
    public void removeDependenciesForStandardJavaClasses() {
        ArrayList <String> allClassNames;
        ArrayList<TDef_Edge> newEdges;
        
        allClassNames = getAllClassNames();
        newEdges = new ArrayList();
        
        for (TDef_Edge dep_edge: edges)
            if (dep_edge.isDependency()) {
                if ((allClassNames.contains(dep_edge.getNode1Name())) &&
                     (allClassNames.contains(dep_edge.getNode2Name()))   ) {
                    newEdges.add(dep_edge);
                }
            }
            else
                newEdges.add(dep_edge);
        
        edges = newEdges;
    }
    
    public ArrayList<String> getAllClassNames() {
        ArrayList<String> result = new ArrayList();
        
        for (TDef_Class classObj: getAllClasses()){
            result.add(classObj.getClassName());
        }

        return result;
    }
    
    public ArrayList<TDef_Class> getAllClasses() {
        ArrayList<TDef_Class> result = new ArrayList();
        ArrayList<TDef_Class> tmp;

        if (classes.size() > 0) {
            for (TDef_Class cl : classes) {
                result.add(cl);
            }
        }

        for (TDef_Peninsula peninsula : peninsulas) {
            tmp = peninsula.getAllClasses();

            if (tmp != null) {
                result.addAll(tmp);
            }
        }

        for (TDef_RepeatingGroup rpg : repeatingGroups) {
            tmp = rpg.getAllClasses();

            if (tmp != null) {
                result.addAll(tmp);
            }
        }

        return result;
    }
    
    public boolean hasPeninsula() {
        return ! peninsulas.isEmpty();
    }

    public ArrayList<TDef_Edge> getAllEdges() {
        ArrayList<TDef_Edge> result = new ArrayList();
        ArrayList<TDef_Edge> tmp;

        if (edges.size() > 0) {
            result.addAll(edges);
        }

        for (TDef_Peninsula peninsula : peninsulas) {
            tmp = peninsula.getAllEdges();

            if (tmp != null) {
                result.addAll(tmp);
            }
        }

        for (TDef_RepeatingGroup rpg : repeatingGroups) {
            tmp = rpg.getAllEdges();

            if (tmp != null) {
                result.addAll(tmp);
            }
        }

        return result;

    }
    
    public void setAllEdgesNotMatched() {
        for (TDef_Edge edge: getAllEdges())
            edge.setMatched(false);
    }
    
    public ArrayList <TDef_Edge> getLocalEdgesOrdered() {
        return orderEdges(edges);
    }
    
    public ArrayList <TDef_Edge> getLocalEdges() {
        return edges;
    }

    public ArrayList<TDef_Edge> getAllEdgesOrdered() {
        // This method change the order of sequence of --all-- edges, 
        // so that every next edge connects to a former edge.
        // ---all-- edges means also the edges in repeating groups and
        // peninsulas.

        // This realizes a connected sequence of edges.
        // For example A->B, C->D, A->C  is changed to to A->B, A->C, C->D.
        // The ordering the sequence of edges of a design pattern 
        // improves the effeciency of process of finding 
        // a matching design pattern.
        // Ordering the sequence of edges of Java source is only a 
        // quality check.
        
        
        
        //- ArrayList<TDef_Edge> allEdges;

         return orderEdges(getAllEdges());

        
            /* DEZE CONTROLE MOET NOG UITGEVOERD WORDEN */
            // ========================================
            /*
            if (!found) {
                Global_Feedback feedback_storage = Global_Feedback.getInstance();

                feedback_storage.addFeedback("TemplateContent consists of two separate parts.");
                feedback_storage.addFeedback("Not connectable to earlier connected edges: " + edgeToConnect.toString());

            }
            */
    }
    
    private ArrayList<TDef_Edge> orderEdges(ArrayList<TDef_Edge> allEdges) {
        boolean found;
        TDef_Edge edgeToConnect, edgeTmp;
        String name1, name2, name3, name4;
        
        for (int i = 1; i < allEdges.size(); i++) {
            // The i-element of allEdges should have a classname which occurs
            // in the elements 0.. (i-1)

            found = false;
            for (int j = i; j < allEdges.size() && !found; j++) {
                // Does element j satifies these condition ?
                // yes --> found = true
                edgeToConnect = allEdges.get(j);
                name1 = edgeToConnect.getNode1Name();
                name2 = edgeToConnect.getNode2Name();

                for (int k = 0; k < i && !found; k++) {
                    name3 = allEdges.get(k).getNode1Name();
                    name4 = allEdges.get(k).getNode2Name();

                    if (name1.equals(name3) || name1.equals(name4)
                            || name2.equals(name3) || name2.equals(name4)) {
                        found = true;

                        if (j != i) {
                            edgeTmp = allEdges.get(j);
                            allEdges.set(j, allEdges.get(i));
                            allEdges.set(i, edgeTmp);
                        }
                    }
                }
            }
        }
        
        return allEdges;
    }

    void setPartOfRepeatingGroup(boolean value) {
        partOfRepeatingGroup = value;
    }

    void setPartOfPeninsula(boolean value) {
        partOfPeninsula = value;
    }

    public boolean isPartOfRepeatingGroup() {
        return partOfRepeatingGroup;
    }

    public boolean isPartOfPeninsula() {
        if (partOfPeninsula)
            return true;
        
        if (parentContent != null)
            return parentContent.isPartOfPeninsula();
        
        return false;
    }
    
    public void setParentContent(TDef_TemplateContent parent) {
        parentContent = parent;
    }
    
    public TDef_TemplateContent getParentContent() {
        return parentContent;
    }
    
    private boolean isNecessaryInHeritance(TDef_Edge edge) {
        // Super classes which belong to Java API like Frame
        // should occure in an edge.

        //TDef_Global_ClassNames singleton = TDef_Global_ClassNames.getInstance();
        // singleton --werd-- op de volgende regel gebruikt.
        ArrayList<String> classNames = TDef_Global_ClassNames.allClassNames();

        return classNames.contains(edge.getSuperClassName());
    }

    public void generateEdges() {
        /* Should be called after a template is fully read during SAX-parsing. 
         * Note: Parsed Java sources does not contain repeating groups and
         * peninsulas.
        
         * leveren een edge op.
         * Echter N.B. int, char, double ... leveren geen edge op.
         * HashTable, HashSet, Set, arrays, Arraylist leveren een 1-* op.
         *
         * returntype en parametertype die geen standaardclass zijn en 
         * ook geen class van een attribute zijn, leveren een dependency op 
         */

        HashSet<String> classNamesInSource;
        HashSet<String> standardMultipleTypes;
        TDef_Global_ClassNames classNames = TDef_Global_ClassNames.getInstance();

        classNamesInSource = new HashSet();
        classNamesInSource.addAll(TDef_Global_ClassNames.allClassNames());

        standardMultipleTypes = getMultipleTypes();

        
        Global_Debug_Info.println("TDef_TemplateContent: ONDERSTAANDE UITGECOMMENTATIEERDE STATEMENT GENEREERT MEER DEPENDENCIES DAN GAMMA ");
        Global_Debug_Info.println("TDef_TemplateContent: VAN ALLE DEPENDENCIY EDGES WAARVAN NODE2 GEEN DEEL UITMAAKT\n VAN HET SYSTEEM MOETEN NOG VERWIJDERD WORDEN.");
        Global_Debug_Info.println("TDef_TemplateContent: N.B NODE1 MOET ONGELIJK ZIJN AAN NODE2"
                + "");
        for (int i = 0; i < classes.size(); i++) {
            classes.get(i).generateAttributesEdges(this, classNamesInSource,
                    standardMultipleTypes);

             
             //classes.get(i).generateDependenciesEdges(this, classNamesInSource,
             //       standardMultipleTypes);

        }
    }

    private HashSet<String> getMultipleTypes() {
        /* Some general classes/interfaces that may contain more 
         * than one element. 
         */

        HashSet<String> names = new HashSet();

        String tmp[] = {"Array", "ArrayList", "Container", "Deque",
            "HashMap", "Hashtable", "HashSet", "Map", "Set", "SortedSet",
            "TreeMap", "TreeSet", "List", "LinkedList", "Stack",
            "Queue", "Deque", "PriorityQueue", "Vector"
        };

        for (String tmp1 : tmp) {
            names.add(tmp1);
        }

        return names;
    }
    
    public TDef_Class getClass(String className) {
        for (TDef_Class classObj: getAllClasses()) {
              if (classObj.getClassName().equals(className))  
                 return classObj;
        }  
        
        Global_Feedback.showOrWite(0, "TDef_TemplateContent: Class met class naam " + className +
                " kon niet gevonden worden."); 
        Global_Feedback.showOrWite(0, "TDef_TemplateContent: Fatale fout");
        System.exit(1);
        
        return null; // make compiler happpy
    }
    
    public boolean classInRpg(String className) {
        for (TDef_RepeatingGroup rpg: repeatingGroups) {
           for (TDef_Class classObj: rpg.getAllClasses()) {
              if (classObj.getClassName().equals(className))  
                 return true;
           }
        }
        
        return false;
    }

    @Override
    public String toString() {
        String result;

        result = "";

        for (int i = 0; i < classes.size(); i++) {
            result += classes.get(i).toString();
        }

        for (int i = 0; i < peninsulas.size(); i++) {
            result += peninsulas.get(i).toString();
        }

        for (int i = 0; i < repeatingGroups.size(); i++) {
            result += repeatingGroups.get(i).toString();
        }

        for (int i = 0; i < edges.size(); i++) {
            result += edges.get(i).toString() + "\n";
        }

        return result;
    }
}
