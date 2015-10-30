/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataminingpackage;

import cluster.Cluster;
import cluster.ClusterCenter;
import imageprocessing.ColorQuantizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import kdtree.DimensionMismatchException;
import kdtree.KDTree;
import kdtree.MaxCountExceededException;
import kmeansfilter.KMeansFilterAlgorithm;


/**
 *
 * @author prasanna
 */
public class DataMiningPackage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DimensionMismatchException, MaxCountExceededException, Exception {
        
        /*
        Set<ClusterCenter> testCenters = new HashSet<>();
        KDTree testingTree = new KDTree(2);
        
        ArrayList<double[]> points = new ArrayList<>();
        
        points.add(new double[]{3,5});
        points.add(new double[]{5,7});
        points.add(new double[]{6,8});
        points.add(new double[]{9,10});
        points.add(new double[]{10,11});
        points.add(new double[]{11,12});
        points.add(new double[]{12,13});
        points.add(new double[]{13,14});
        points.add(new double[]{14,15});
        points.add(new double[]{15,16});
        
        //testingTree.insert(new double[]{3,4,5});
        //testingTree.insert(new double[]{5,7,1});
        //testingTree.insert(new double[]{2,6,1});
        //testingTree.insert(new double[]{1,6,4});
        //testingTree.insert(new double[]{4,6,1});
        //testingTree.insert(new double[]{2,6,3});
        
        testingTree.createKDTree(points);
        
        System.out.print(testingTree);
        
        System.out.print(testingTree.search(points.get(9)));
        */
        
        //KMeansFilterAlgorithm instance = new KMeansFilterAlgorithm();
        //Set<Cluster> clusters = instance.cluster(points, 2, 3);
        
        
        //Iterator<Cluster> iterator = clusters.iterator();
        //while(iterator.hasNext())
        //{
        //    Cluster cluster = iterator.next();
        //   System.out.print("\n\nCluster: \n"+cluster.toString());
        //}
        
        //System.out.print("\n\nCluster center of (6,6): "+Arrays.toString(instance.getCenterValue((points.get(8)))));
        
        //System.out.print(testingTree.toString());
        
       ColorQuantizer.colorQuantize(args[0], Integer.parseInt(args[1]), args[2]); 

    }
}
