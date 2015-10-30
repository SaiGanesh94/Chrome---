/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

import java.util.Arrays;

/**
 *
 * @author prasanna
 */
public class Cell {
    private double[] min;
    private double[] max;
    public Cell(double[] min, double[] max)
    {
        if(min.length != max.length)
            throw new RuntimeException("Cannot create cell: min max dimensions do not match");
        this.min = Arrays.copyOf(min, min.length);
        this.max = Arrays.copyOf(max, max.length);
    }

    public double[] getMax() {
        return max;
    }

    public double[] getMin() {
        return min;
    }
    
    public double[] getMidPoint()
    {
        double[] midPoint = new double[min.length];
        for(int i=0; i<min.length; i++)
        {
            midPoint[i] = (max[i]+min[i])/2.0;
        }
        
        return midPoint;
    }
    
    public void considerPoint(double[] point)
    {
        if(point.length != min.length)
            throw new RuntimeException("Cannot create cell: min max dimensions do not match");
        
        for(int i=0; i<point.length; i++)
        {
            if(min[i] > point[i])
                min[i] = point[i];
            if(max[i] < point[i])
                max[i] = point[i];
        }
    }
}
