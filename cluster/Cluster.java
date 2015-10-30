/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluster;

import dataminingpackage.Utilities;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prasanna
 */
public class Cluster {
    
    private ClusterCenter center;
    private List<double[]> points;
    
    public String toString()
    {
        String result = "";
        result = result.concat("Center: "+center);
        result = result.concat("\nPoints: "+Utilities.getPointsAsString(points));
        return result;
    }
    
    public Cluster(double[] centerPoint)
    {
        center.setPoint(centerPoint);
        points = new ArrayList<>();
    }
    
    public Cluster(ClusterCenter center)
    {
        this.center = center;
        points = new ArrayList<>();
    }
    
    public void addPoint(double[] point)
    {
        points.add(point);
    }
    
    public boolean contains(double[] point)
    {
        return Utilities.listContainsPoint(points, point);
    }
}
