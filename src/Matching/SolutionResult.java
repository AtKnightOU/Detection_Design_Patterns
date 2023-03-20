/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matching;

/**
 *
 * @author ed
 */
public class SolutionResult {
    private boolean solutionFound;
    private Solution solution;
    
    SolutionResult(boolean found, Solution solution) {
        this.solutionFound = found;
        this.solution = solution;
    }
    
    public boolean isMatched() {
        return solutionFound;
    }
    
    public Solution getSolution() {
        return solution;
    }
}
