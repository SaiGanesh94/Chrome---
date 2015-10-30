/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataminingpackage;

import cluster.ClusterCenter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prasanna
 */
public class Utilities {
    
    public static double[] vectorSum(double[] one, double[] two)
    {
        checkLengthMatch(one, two);
        
        double[] result = new double[one.length];
        for(int i=0; i < one.length; i++)
        {
            result[i] = one[i] + two[i];
        }
        
        return result;
    }
    
    public static double[] vectorDifference(double[] one, double[] two)
    {
        checkLengthMatch(one, two);
        
        double[] result = new double[one.length];
        for(int i=0; i < one.length; i++)
        {
            result[i] = one[i] - two[i];
        }
        
        return result;
    }

    private static void checkLengthMatch(double[] one, double[] two) throws RuntimeException {
        if(one.length != two.length)
            throw new RuntimeException("Vector length mismatch");
    }
    
    public static double distance(double[] one, double[] two)
    {
        checkLengthMatch(one, two);
        
        double distance = 0;
        
        for(int i=0; i<one.length; i++)
        {
            distance += Math.pow(one[i]-two[i], 2);
        }
        
        distance = Math.sqrt(distance);
        
        return distance;
    }
    
    public static double[] getMidValue(List<double[]> list)
    {
        return list.get(getMidIndex(list));
    }
    
    public static int getMidIndex(List<double[]> list)
    {
        if(list.isEmpty())
            throw new RuntimeException("List size is zero; median cannot be found");
        if(list.size()%2 == 1)
           return list.size()/2;
        else
           return list.size()/2 - 1;
    }

    public static HashSet<ClusterCenter> cloneSet(Set<ClusterCenter> set) {
        HashSet<ClusterCenter> clonedSet = new HashSet<>(set.size());
        
        Iterator<ClusterCenter> iterator = set.iterator();
        
        while(iterator.hasNext())
        {
            try {
                clonedSet.add(iterator.next().clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        return clonedSet;
    }
    
    public static boolean listContainsPoint(List<double[]> list, double[] point)
    {
        if(point==null || list==null)
            return false;
        Iterator<double[]> iterator = list.iterator();
        while(iterator.hasNext())
        {
            if(Arrays.equals(iterator.next(), point))
                return true;
        }
        
        return false;
    }
    
    public static boolean setContainsPoint(Set<double[]> set, double[] point)
    {
        if(point==null || set==null)
            return false;
        Iterator<double[]> iterator = set.iterator();
        while(iterator.hasNext())
        {
            if(Arrays.equals(point, iterator.next()))
                return true;
        }
        
        return false;
    }
    
    public static String getPointsAsString(List<double[]> list)
    {
        String result = "";
        Iterator<double[]> iterator = list.iterator();
        
        while(iterator.hasNext())
        {
            if(!result.isEmpty())
                result = result.concat(",");
            result = result.concat(Arrays.toString(iterator.next()));
        }
        
        return result;
    }

    public static boolean equalSet(HashSet<ClusterCenter> oldSet, HashSet<ClusterCenter> newSet) {
        return oldSet.equals(newSet);
    }

    public static double[] vectorDivide(double[] vector, double dividend) {
        if(dividend==0)
            throw new ArithmeticException("Dividing vector by zero: "+Arrays.toString(vector)+" / "+dividend);
        double[] result = new double[vector.length];
        for(int i=0; i<vector.length; i++)
        {
            result[i] = vector[i] / dividend;
        }
        return result;
    }
}
