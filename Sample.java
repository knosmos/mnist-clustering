/*
 * Class to load and represent one MNIST sample
 */

import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class Sample {
    private double[] data;
    private String filename;
    public Sample(String filename) throws IOException {
        load(filename);
        this.filename = filename;
    }
    public double distance(Sample other) {
        // squared euclidean distance
        double dist = 0;
        double[] other_data = other.getData();
        for (int i=0; i<data.length; i++) {
            dist += (data[i] - other_data[i]) * (data[i] - other_data[i]);
        }
        return dist;
    }
    public double[] getData() {
        return this.data;
    }
    private void load(String filename) throws IOException {
        // read image
        BufferedImage p = ImageIO.read(new File(filename));
        // load data
        data = new double[16];
        for (int i=0; i<p.getWidth(); i++) {
            for (int j=0; j<p.getHeight(); j++) {
                int rgb = p.getRGB(i, j);
                int gray = rgb & 0xFF;
                data[i*p.getWidth() + j] = gray;
            }
        }
    }
}