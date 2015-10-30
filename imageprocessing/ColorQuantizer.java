/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import cluster.Cluster;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import kmeansfilter.KMeansFilterAlgorithm;

/**
 *
 * @author prasanna
 */
public class ColorQuantizer {
    
    private static double[] getPixelARGB(int pixel) {
        double[] argbValues = new double[4];
        argbValues[0] = (pixel >> 24) & 0xff;
        argbValues[1]= (pixel >> 16) & 0xff;
        argbValues[2] = (pixel >> 8) & 0xff;
        argbValues[3] = (pixel) & 0xff;
        return argbValues;
  }
    
    public static BufferedImage colorQuantize(String imagePath, int palleteSize, String outputFilePath) throws Exception
    {
        System.out.print("\n\nReading image...");
        File imageFile = new File(imagePath);
        if(!imageFile.exists())
        {
            throw new FileNotFoundException("Image file "+imagePath+" does not exist.");
        }
        
        BufferedImage image = ImageIO.read(new File(imagePath));
        
        System.out.print("\n\nGenerating data set for clustering...");
        List<double[]> rgbData = generateRGBDataSet(image);
        System.out.print("\n\nData set ready. Number of points to cluster: "+rgbData.size());
        
        KMeansFilterAlgorithm algorithmInstance = new KMeansFilterAlgorithm();
        Set<Cluster> clusters = algorithmInstance.cluster(rgbData, 4, palleteSize);
        int count = algorithmInstance.getNumberOfPoints();
        
        System.out.print("\n\nCreating quantized image...");
        BufferedImage quantizedImage = createQuantizedImage(algorithmInstance,rgbData,image);
        
        System.out.print("\n\nWriting image to disk...");
        writeImage(quantizedImage,outputFilePath);
        
        return quantizedImage;
    }

    private static List<double[]> generateRGBDataSet(BufferedImage image) {
        List<double[]> rgbDataSet = new ArrayList<>();
        
        for(int j=image.getMinY(); j<image.getHeight(); j++)
        {
            for(int i=image.getMinX(); i<image.getWidth(); i++)
            {
                rgbDataSet.add(getPixelARGB(image.getRGB(i, j)));
            }
        }
        
        return rgbDataSet;
    }

    private static BufferedImage createQuantizedImage(KMeansFilterAlgorithm algorithmInstance, List<double[]> rgbData, BufferedImage image) {
        
        BufferedImage quantizedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Iterator<double[]> rgbIterator = rgbData.iterator();
        
        for(int j=image.getMinY(); j<image.getHeight(); j++)
        {
            for(int i=image.getMinX(); i<image.getWidth(); i++)
            {
                double[] originalValue = rgbIterator.next();
                try {
                    double[] centerValue = algorithmInstance.getCenterValue(originalValue);
                    quantizedImage.setRGB(i, j, rgbToInt(centerValue));
                } catch (Exception ex) {
                    Logger.getLogger(ColorQuantizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return quantizedImage;
        
    }

    private static int rgbToInt(double[] centerValue) {
        int color = 0;
        
        color |= ((int)centerValue[0] & 255) << 24;
        color |= ((int)centerValue[1] & 255) << 16;
        color |= ((int)centerValue[2] & 255) << 8;
        color |= ((int)centerValue[3] & 255);
        
        return color;
    }

    private static void writeImage(BufferedImage quantizedImage, String outputFilePath) {
        String format = outputFilePath.substring(outputFilePath.length()-3);
        File outputFile = new File(outputFilePath);
        try {
            ImageIO.write(quantizedImage, format, outputFile);
        } catch (IOException ex) {
            Logger.getLogger(ColorQuantizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
