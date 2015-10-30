/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

import cluster.Cluster;
import java.util.*;

/**
 *
 * @author prasanna
 */
public class KDTree {
    
    private KDNode root;
    private int dimension;
    HashMap<double[],KDNode> pointNodeMap;
    
    public HashMap<double[], KDNode> getPointNodeMap() {
        return pointNodeMap;
    }
    
    private static final int TOTAL_CAP = 1000000;
    
    public KDTree(int dim)
    {
        dimension = dim;
        pointNodeMap = new HashMap<>();
    }
    
    public KDNode getRoot()
    {
        return root;
    }
    
    public void insert(double[] newPoint) throws DimensionMismatchException
    {
        if(newPoint.length != dimension)
            throw new DimensionMismatchException();
        
        KDNode newNode;
        
        if(root==null)
        {
            root = new KDNode(newPoint, 0);
            newNode = root;
        }
        else
            newNode = root.insert(newPoint);
        
        System.out.print("\nInserted: "+Arrays.toString(newNode.getPoint()));
    }
    
    @Override
    public String toString()
    {
        return root.recursivePrint();
    }
    
    public void createKDTree(List<double[]> points) throws MaxCountExceededException
    {
        pointNodeMap.clear();
        if(points.size() > TOTAL_CAP)
            throw new MaxCountExceededException();
        
        root = new KDNode(0);
        root.createSubTree(points,pointNodeMap);
        root.preprocessTree();
    }

    public Set<Cluster> getClusters() {
        Set<Cluster> resultSet = new HashSet<>();
        root.getClustersRec(resultSet);
        return resultSet;
    }
    
    public KDNode search(double[] point)
    {
        if(root==null)
            return null;
        
        return root.searchRec(point);
    }

    public int count() {
        if(root!=null)
            return root.count();
        else
            return -1;
    }
}
