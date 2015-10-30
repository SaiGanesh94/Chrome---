/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

import java.util.Comparator;

/**
 *
 * @author prasanna
 */
public class ArrayComparator implements Comparator<double[]>{
    
    int index;
    
    public ArrayComparator(int index)
    {
        this.index = index;
    }

    @Override
    public int compare(double[] o1, double[] o2) {
        if(o1[index] < o2[index])
            return -1;
        if(o1[index] > o2[index])
            return 1;
        
        return 0;
    }
    
}
