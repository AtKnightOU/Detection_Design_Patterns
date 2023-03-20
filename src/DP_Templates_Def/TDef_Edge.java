package DP_Templates_Def;

import General.Constants;
import General.Global_Feedback;
import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */

public class TDef_Edge {
    private String node1Name, node2Name;
    private int edgeTypeInt;
    private TDef_TemplateContent parentContent;
    private boolean matched;
    // used during matching

    public TDef_Edge(String node1Name, String node2Name, String edgeTypeStr) {
        this.node1Name = node1Name;
        this.node2Name = node2Name;
        
        parentContent = null;
        matched = false;
        
        if (Constants.edgeTypeStrOk(edgeTypeStr)) {
            this.edgeTypeInt = Constants.getInt(edgeTypeStr);
        }
        else {
            errorMessage("edgeType " + edgeTypeStr + " is not correct.");
            
            System.exit(1);
        }
    }
    
    public String getNode1Name() {
        return node1Name;
    }
    
    public String getNode2Name() {
        return node2Name;
    }
    
    public void setParentContent(TDef_TemplateContent parent) {
        parentContent = parent;
    }
    
    public boolean isInheritance() {
        return Constants.isInheritanceInt(edgeTypeInt);
    }
    
    public boolean isDependency() {
        return Constants.isDependencyInt(edgeTypeInt);
    }

    private void errorMessage(String msg) {
        Global_Feedback.showOrWite(1, msg + " in Java class " + this.getClass().getName());
        System.exit(1);
    }
    
    @Override
    public boolean equals(Object obj) {
        // necessary to prevent multiple occurrences of one edges
        
        TDef_Edge tmp = (TDef_Edge)obj;
        
        return node1Name.equals(tmp.node1Name) &&  
                node2Name.equals(tmp.node2Name) &&
                edgeTypeInt == tmp.edgeTypeInt;
    }
    
    public String getSuperClassName() {
        return node2Name;
    }
    
    public boolean isPartOfrepeatingGroup() {
        return parentContent.getParentContent() != null;
        
        // This may be wrong The structure of the code 
        // may be have to resemble the code of the next method.
    }
    
    public boolean isPartOfPeninsula() {
        
        Global_Feedback.showOrWite(0, "isPartOfPeninsula TIJDELIJK FALSE");
        return false; /*
        
        if (parentContent == null)
            return false;
        
        if (parentContent.hasPeninsula())
            return true;
        
        return parentContent.isPartOfPeninsula(); */
        
        // return parentContent.getParentContent().hasPeninsula();
    }
    
    public TDef_TemplateContent getParentTemplateContent() {
        return parentContent;
    }
    
    public ArrayList<TDef_Edge> getRpgDp_Edges(ArrayList<TDef_Edge> allDP_Edges) {
        TDef_TemplateContent local_parentContent;
        ArrayList <TDef_Edge> result;
        
        local_parentContent = getParentTemplateContent();
        // WAAROM IS DEZE VAARIABLE NODIG??
        result = new ArrayList();
        
        // return this.parentContent.getLocalEdgesOrdered();
        
               
        ArrayList <TDef_Edge> tmp = this.parentContent.getLocalEdges();  
                                    // local_ (zie boven) is later toegevoegd.
        for (TDef_Edge edge: allDP_Edges) {
            //if (tmp.contains(edge))
                //result.add(edge);
            for (TDef_Edge e: tmp){
                if (e.equals(edge)) {
                    result.add(edge);
                    break;
                } 
            }
        }
        
        return result;              
    }
    
    public String getEdgeTypeStr() {
        return Constants.getStr(edgeTypeInt);
    }
    
    public int getEdgeTypeInt() {
        return edgeTypeInt;
    }
    
    
    public boolean isMatched() {
        return matched;
    }
    
    public void setMatched(boolean matched) {
        this.matched = matched;
    }
    
    @Override
    public String toString() {
        return "<" + node1Name + ", " + node2Name + ", " + getEdgeTypeStr() + ">";
    }
}