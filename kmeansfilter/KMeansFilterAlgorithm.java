/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeansfilter;

import cluster.Cluster;
import cluster.ClusterCenter;
import dataminingpackage.Utilities;
import java.util.*;
import kdtree.Cell;
import kdtree.KDNode;
import kdtree.KDTree;
import kdtree.MaxCountExceededException;

/**
 *
 * @author prasanna
 */
public class KMeansFilterAlgorithm {
    
    private KDTree kdTree;
    
    
    public double[] getCenterValue(double[] point) throws Exception
    {
        if(kdTree==null)
            throw new RuntimeException("Clustering must be done before center value can be found");
        KDNode node = kdTree.getPointNodeMap().get(point);
        if(node==null)
            throw new Exception("Point not present in the data set: "+Arrays.toString(point));
        
        return node.getClusterCenter().getPoint();
        
    }
    
    public Set<Cluster> cluster(List<double[]> points, int dimension, int k)
    {
        System.out.print("\n\nBuilding KD Tree...");
        try {
            kdTree = new KDTree(dimension);
            kdTree.createKDTree(points);
            
            //System.out.print("\n\nKDTree: "+kdTree.toString()+"\n\n");
            
            List<double[]> initialCenters = pickRandom(points,k);
            
            //System.out.print("\n\nInitial centers picked: "+Utilities.getPointsAsString(initialCenters));
            
            System.out.print("\n\nClustering based on k-means filter algorithm, for k = "+k+" clusters...");
            Set<ClusterCenter> clusterCenters = runFilterAlgorithm(kdTree,initialCenters);
            
            Set<Cluster> clusters = kdTree.getClusters();
            
            System.out.print("\n\nClustering done.");
            return clusters;
        } catch (MaxCountExceededException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void assignCenterToNode(ClusterCenter zStar, KDNode node) {
        zStar.setWeightedCentroid(Utilities.vectorSum(zStar.getWeightedCentroid(),node.getPoint()));
        zStar.setNumberOfNeighbours(zStar.getNumberOfNeighbours()+1);
        node.setClusterCenter(zStar);
    }

    private void assignCenterToSubtree(ClusterCenter zStar, KDNode node) {
        zStar.setWeightedCentroid(Utilities.vectorSum(zStar.getWeightedCentroid(),node.getWeightedCentroid()));
        zStar.setNumberOfNeighbours(zStar.getNumberOfNeighbours()+node.getCount());
        node.setCenterForSubtree(zStar);
    }

    private Set<ClusterCenter> runFilterAlgorithm(KDTree kdTree, List<double[]> initialCenters) {
        
        Set<ClusterCenter> centerSet = new HashSet<>();
        
        for(Iterator<double[]> iter = initialCenters.iterator(); iter.hasNext();)
        {
            centerSet.add(new ClusterCenter(iter.next()));
        }
        HashSet<ClusterCenter> oldCenterSet;
        int i=0;
        System.out.print("\n");
        do
        {
            filter(kdTree.getRoot(),centerSet);
            //System.out.print("\n\n\nCluster centers: "+centerSet);
            oldCenterSet = Utilities.cloneSet(centerSet);
            centerSet = getUpdatedCenterSet(centerSet);
            if(oldCenterSet.equals(centerSet))
                break;
            i++;
            //System.out.print("\n\n\nUpdated centers: ");
            //System.out.print(centerSet);
            System.out.print("\nIteration "+i+"...");
            
        }while(true);
        
        return centerSet;
    }

    private Set<ClusterCenter> filterCenters(Set<ClusterCenter> centerSet, ClusterCenter zStar, Cell c) {
        Iterator<ClusterCenter> iterator = centerSet.iterator();
        Set<ClusterCenter> removedCenters = new HashSet();
        while(iterator.hasNext())
        {
            ClusterCenter thisCenter = iterator.next();
            if(!thisCenter.equals(zStar))
            {
                if(thisCenter.isFarther(zStar, c))
                {
                    //System.out.print("\nRemoving center... zStar: "+zStar+" z: "+thisCenter);
                    removedCenters.add(thisCenter);
                    //System.out.print("\nCenter removed for next level: "+thisCenter);
                }
            }
        }
        iterator = removedCenters.iterator();
        while(iterator.hasNext())
        {
            ClusterCenter removedCenter = iterator.next();
            centerSet.remove(removedCenter);
        }
        
        return removedCenters;
    }

    private List<double[]> pickRandom(List<double[]> points, int k) {
        Random random = new Random();
        ArrayList<double[]> result = new ArrayList<>();
        int index;
        for(int i=0; i < k; i++)
        {
            do
            {
                index = random.nextInt(points.size());
            }while(Utilities.listContainsPoint(result,points.get(index)));
            
            result.add(points.get(index));
        }
        return result;
    }

    private void filter(KDNode node, Set<ClusterCenter> centerSet) {
        Cell c = node.getCell();
        
        if(node.isLeaf())
        {
            ClusterCenter zStar = findClosestCenter(node.getPoint(), centerSet);
            assignCenterToNode(zStar, node);
            //System.out.print("\n\nLeaf node: "+Arrays.toString(node.getPoint()));
            //System.out.print("\nCenter set: "+centerSet);
            //System.out.print("Cluster center set: "+zStar);
        }
        else
        {
            //System.out.print("\n\nNode: "+Arrays.toString(node.getPoint()));
            //System.out.print("\nCenter set: "+centerSet);
            double[] midPoint = c.getMidPoint();
            ClusterCenter zStar = findClosestCenter(midPoint, centerSet);
            //System.out.print("\nClosest center to mid point: "+zStar);
            
            Set<ClusterCenter> removedCenters = filterCenters(centerSet, zStar, c);
            
            if(centerSet.size()==1)
            {
                assignCenterToSubtree(zStar, node);
                //System.out.print("\n\nNode with single candidate: "+Arrays.toString(node.getPoint()));
                //System.out.print("\nCluster center set for subtree: "+zStar);
            }
            else
            {
                final ClusterCenter closestCenter = findClosestCenter(node.getPoint(), centerSet);
                assignCenterToNode(closestCenter, node);
                //System.out.print("\nCluster center set: "+node.getClusterCenter());
                if(node.getPrev()!=null)
                    filter(node.getPrev(),centerSet);
                if(node.getNext()!=null)
                    filter(node.getNext(), centerSet);
            }
            centerSet.addAll(removedCenters);
        }
    }
    
    private ClusterCenter findClosestCenter(double[] point, Set<ClusterCenter> centerSet)
    {
        if(centerSet.isEmpty())
            return null;
        double shortestDistance = Double.MAX_VALUE;
        ClusterCenter closestCenter = null;
        
        Iterator<ClusterCenter> iterator = centerSet.iterator();
        while(iterator.hasNext())
        {
            ClusterCenter thisCenter = iterator.next();
            double distance = Utilities.distance(point, thisCenter.getPoint());
            if(distance < shortestDistance)
            {
                shortestDistance = distance;
                closestCenter = thisCenter;
            }
        }
        
        return closestCenter;
    }

    private Set<ClusterCenter> getUpdatedCenterSet(Set<ClusterCenter> centerSet) {
        Iterator<ClusterCenter> iterator = centerSet.iterator();
        HashSet<ClusterCenter> newCenterSet = new HashSet<>();
        while(iterator.hasNext())
        {
            ClusterCenter thisCenter = iterator.next();
            //System.out.print("\n\nUpdating center..."+thisCenter);
            
            thisCenter.update();
            thisCenter.setNumberOfNeighbours(0);
            thisCenter.setWeightedCentroid(new double[thisCenter.getPoint().length]);
            newCenterSet.add(thisCenter);
        }
        centerSet.clear();
        
        return newCenterSet;
    }

    public int getNumberOfPoints() {
        return kdTree.count();
    }

}
