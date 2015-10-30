/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

/**
 *
 * @author prasanna
 */
public class DimensionMismatchException extends Exception {
    
    public DimensionMismatchException()
    {
        super("Dimension mismatch in KDTree");
    }
    
}
