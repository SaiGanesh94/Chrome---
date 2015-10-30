/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluster;

import dataminingpackage.Utilities;
import java.util.Arrays;
import kdtree.Cell;

/**
 *
 * @author prasanna
 */
public class ClusterCenter implements Cloneable{
    private double[] point;
    private double[] weightedCentroid;
    private double numberOfNeighbours;
    private Cluster cluster;

    public Cluster getCluster() {
        return cluster;
    }

    public ClusterCenter(double[] point, double[] weightedCentroid, double numberOfNeighbours) {
        this.point = Arrays.copyOf(point,point.length);
        this.weightedCentroid = weightedCentroid;
        this.numberOfNeighbours = numberOfNeighbours;
        cluster = new Cluster(this);
    }
    
    public ClusterCenter(double[] point)
    {
        this.point = Arrays.copyOf(point, point.length);
        this.weightedCentroid = new double[point.length];
        this.numberOfNeighbours = 0;
        cluster = new Cluster(this);
    }

    public double getNumberOfNeighbours() {
        return numberOfNeighbours;
    }

    public void setNumberOfNeighbours(double numberOfNeighbours) {
        this.numberOfNeighbours = numberOfNeighbours;
    }

    public double[] getPoint() {
        return point;
    }

    public void setPoint(double[] point) {
        this.point = point;
    }

    public double[] getWeightedCentroid() {
        return weightedCentroid;
    }

    public void setWeightedCentroid(double[] weightedCentroid) {
        this.weightedCentroid = weightedCentroid;
    }

    @Override
    public ClusterCenter clone() throws CloneNotSupportedException {
        return new ClusterCenter(point, weightedCentroid, numberOfNeighbours);
    }

    @Override
    public boolean equals(Object newCenter) {
        
        if(newCenter instanceof ClusterCenter)
        {
            ClusterCenter center = (ClusterCenter)newCenter;
            if(Arrays.equals(this.point, center.point))
                return true;
            else
                return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }
    
    public boolean isFarther(ClusterCenter zStar, Cell c)
    {
        double[] v = new double[zStar.point.length];
        
        double[] u = Utilities.vectorDifference(this.point, zStar.point);
        
        for(int i=0; i<u.length; i++)
        {
            if(u[i]<0)
                v[i] = c.getMin()[i];
            else
                v[i] = c.getMax()[i];
        }
        //System.out.print("\nv(H): "+Arrays.toString(v));
        if(Utilities.distance(this.point, v) >= Utilities.distance(zStar.point,v))
            return true;
        else
            return false;
    }

    public void update() {
        this.point = Utilities.vectorDivide(weightedCentroid,numberOfNeighbours);
    }

    @Override
    public String toString() {
        return Arrays.toString(point)+", WC:"+Arrays.toString(weightedCentroid)+", c: "+numberOfNeighbours;
    }

    
    
}
