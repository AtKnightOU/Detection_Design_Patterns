package Matching;

import java.util.ArrayList;

/**
 *
 * @author ed
 */
public class Solutions {
    private ArrayList<Solution> solutions;
    
    Solutions() {
        solutions = new ArrayList();
    }
    
    void addSolution(Solution solution) {
        solutions.add(solution);
    }
    
    @Override 
    public String toString() {
        String result = "";
        
        for (int i = 0; i < solutions.size(); i++) {
            result += solutions.get(i).toString();
        }
        
        return result;
    } 
}
