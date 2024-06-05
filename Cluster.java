/*
 * Class to represent one detected cluster
 */

import java.util.*;

public class Cluster {
    private Sample[] samples;
    private double[] centroid;
    public Cluster(Sample[] samples) {
        this.samples = samples;
        centroid = new double[28*28];
    }
    public void calculateCentroid() {
        // calculate center of mass of samples
        for (int i=0; i<centroid.length; i++) {
            centroid[i] = 0;
        }
        for (Sample sample: samples) {
            for (int i=0; i<centroid.length; i++) {
                centroid[i] += sample[i];
            }
        }
        for (int i=0; i<centroid.length; i++) {
            centroid[i] /= samples.length;
        }
    }
    public double[] getCentroid() {
        return centroid;
    }
    public Sample[] getSamples() {
        return samples;
    }
    public int size() {
        return samples.length;
    }
}