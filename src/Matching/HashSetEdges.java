/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matching;

import DP_Templates_Def.TDef_Edge;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author ed
 */
public class HashSetEdges {
    // Made for fast en easy search.
    
    private HashSet <String> hashSetEdges;
    
    HashSetEdges(ArrayList <TDef_Edge> edges) {
       hashSetEdges = new HashSet(); 
       
       for (TDef_Edge edge: edges) {
           hashSetEdges.add(makeKey(edge.getNode1Name(),
                   edge.getNode2Name(), 
                   edge.getEdgeTypeInt()));
       }
    }
    
    private String makeKey(String name1, String name2, int typeEdge) {        
        return name1 + " " + name2 + " " + typeEdge;
    }
    
    public boolean contains(String name1, String name2, int typeEdge) {
        return hashSetEdges.contains(makeKey(name1, name2, typeEdge));
    }
    
}
