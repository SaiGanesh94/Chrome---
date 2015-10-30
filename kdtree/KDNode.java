/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kdtree;

import cluster.Cluster;
import cluster.ClusterCenter;
import dataminingpackage.Utilities;
import java.util.*;

/**
 *
 * @author prasanna
 */
public class KDNode {
    
    private double[] point;
    private ClusterCenter clusterCenter;
    
    private KDNode next;
    private KDNode prev;
    private Cell cell;
    //private KDNode parent;
    
    private int axisIndex;
    
    private int count;
    private double[] weightedCentroid;
    
    public KDNode(double[] newPoint, int axisIndex)
    {
        this(axisIndex);
        point = Arrays.copyOf(newPoint, newPoint.length);
    }
    
    public KDNode(int axisIndex)
    {
        this.axisIndex = axisIndex;
        count = 0;
    }

    public Cell getCell() {
        return cell;
    }
    
    public KDNode insert(double[] newPoint)
    {
        if(Arrays.equals(this.point, newPoint))
            return null;
        
        KDNode newNode;
        
        if(newPoint[axisIndex] < point[axisIndex])
        {
            if(prev == null)
            {
                newNode = new KDNode(newPoint, (axisIndex+1)%point.length);
                prev = newNode;
            }
            else
                newNode = prev.insert(newPoint);
        }
        else
        {
            if(next==null)
            {
                newNode = new KDNode(newPoint, (axisIndex+1)%point.length);
                next = newNode;
            }
            else
                newNode = next.insert(newPoint);
        }
        
        return newNode;
    }

    public double[] getPoint() {
        return point;
    }
    
    @Override
    public String toString()
    {
        return (Arrays.toString(point));
    }
    
    public String recursivePrint()
    {
        String result = ("\n\n"+this.toString()+"\tChildren: ");
        String prevResult = "";
        String nextResult = "";
        if(prev!=null)
        {
            result = result.concat("p = "+prev.toString()+" ");
            prevResult = prev.recursivePrint();
        }
        if(next!=null)
        {
            result = result.concat("n = "+next.toString());
            nextResult = next.recursivePrint();
        }
        result = result.concat("\nCount: "+count+"\t Weighted centroid: "+Arrays.toString(weightedCentroid));
        result = result.concat("\nCell - Min: "+Arrays.toString(cell.getMin())+"\tMax: "+Arrays.toString(cell.getMax()));
        
        result = result.concat(prevResult);
        result = result.concat(nextResult);
        
        
        return result;
    }
    
    public void createSubTree(List<double[]> points, HashMap<double[],KDNode> pointNodeMap)
    {
        Collections.sort(points,new ArrayComparator(axisIndex));
        int medianIndex = Utilities.getMidIndex(points);
        double[] median = Utilities.getMidValue(points);
        point = median;
        pointNodeMap.put(point, this);
        if(medianIndex!=0)
        {
            prev = new KDNode((axisIndex+1)%median.length);
            prev.createSubTree(new ArrayList(points.subList(0, medianIndex)),pointNodeMap);
        }
        if(medianIndex!=points.size()-1)
        {
            next = new KDNode((axisIndex+1)%median.length);
            next.createSubTree(new ArrayList(points.subList(medianIndex+1, points.size())),pointNodeMap);
        }
    }
    
    public void preprocessTree()
    {
        if(weightedCentroid == null)
            weightedCentroid = new double[point.length];
        if(cell==null)
            cell = new Cell(point, point);
        if(this.prev == null && this.next == null)
        {
            this.count = 1;
            this.weightedCentroid = Arrays.copyOf(point, point.length);
        }
        else
        {
            if(prev!=null)
            {
                prev.preprocessTree();
                this.count += prev.count;
                this.weightedCentroid = Utilities.vectorSum(weightedCentroid, prev.weightedCentroid);
                this.cell.considerPoint(prev.cell.getMin());
                this.cell.considerPoint(prev.cell.getMax());
            }
            if(next!=null)
            {
                next.preprocessTree();
                this.count += next.count;
                this.weightedCentroid = Utilities.vectorSum(weightedCentroid, next.weightedCentroid);
                this.cell.considerPoint(next.cell.getMin());
                this.cell.considerPoint(next.cell.getMax());
            }
            this.count+=1;
            this.weightedCentroid = Utilities.vectorSum(weightedCentroid, point);
        }
    }

    public int getCount() {
        return count;
    }

    public void setClusterCenter(ClusterCenter clusterCenter) {
        this.clusterCenter = clusterCenter;
    }

    public ClusterCenter getClusterCenter() {
        return clusterCenter;
    }

    public KDNode getNext() {
        return next;
    }

    public KDNode getPrev() {
        return prev;
    }

    public double[] getWeightedCentroid() {
        return weightedCentroid;
    }

    public void getClustersRec(Set<Cluster> clusterSet) {
        if(prev!=null)
            prev.getClustersRec(clusterSet);
        if(next!=null)
            next.getClustersRec(clusterSet);
        if(this.clusterCenter==null)
            throw new RuntimeException("Clustering has to be done before clusters can be retrived");

        this.clusterCenter.getCluster().addPoint(this.point);
        clusterSet.add(this.clusterCenter.getCluster());
    }

    public boolean isLeaf() {
        return prev==null && next==null;
    }

    public void setCenterForSubtree(ClusterCenter zStar) {
        if(prev!=null)
        {
            prev.setCenterForSubtree(zStar);
        }
        if(next!=null)
        {
            next.setCenterForSubtree(zStar);
        }
        this.clusterCenter = zStar;
    }

    public KDNode searchRec(double[] searchPoint) {
        if(Arrays.equals(this.point, searchPoint))
        {
            return this;
        }
        if(searchPoint[axisIndex] < this.point[axisIndex])
        {
            if(prev!=null)
                return prev.searchRec(searchPoint);
            else
                return null;
        }
        else if(searchPoint[axisIndex] < this.point[axisIndex])
        {
            if(next!=null)
                return next.searchRec(searchPoint);
            else
                return null;
        }
        else
        {
            KDNode result=null;
            if(next!=null)
            {
                result = next.searchRec(searchPoint);
            }
            if(result==null && prev!=null)
            {
                result = prev.searchRec(searchPoint);
            }
            return result;
        }
    }

    int count() {
        return (prev!=null?prev.count():0)+(next!=null?next.count():0)+1;
    }
    

}
