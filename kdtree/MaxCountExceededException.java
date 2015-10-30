/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

/**
 *
 * @author prasanna
 */
public class MaxCountExceededException extends Exception{
    
    public MaxCountExceededException()
    {
        super("Maximum count exceeded for KD Tree");
    }
}
