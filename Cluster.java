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
    public Cluster() {
        centroid = new double[28*28];
        this.samples = new Sample[0];
    }
    public void calculateCentroid() {
        // calculate center of mass of samples
        for (int i=0; i<centroid.length; i++) {
            centroid[i] = 0;
        }
        for (Sample sample: samples) {
            for (int i=0; i<centroid.length; i++) {
                centroid[i] += sample.getData()[i];
            }
        }
        for (int i=0; i<centroid.length; i++) {
            centroid[i] /= samples.length;
        }
    }
    public void addSample(Sample sample) {
        Sample[] newSamples = new Sample[samples.length + 1];
        for (int i=0; i<samples.length; i++) {
            newSamples[i] = samples[i];
        }
        newSamples[samples.length] = sample;
        samples = newSamples;
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
    public String export() {
        String res = "";
        for (int i=0; i<centroid.length; i++) {
            res += String.format("%.2f ", centroid[i]);
        }
        res += "\n";
        return res;
    }
    public String toString() {
        String res = "size | " + size() + "\n";
        for (int i=0; i<28; i+=2) {
            for (int j=0; j<28; j++) {
                if (centroid[j*28 + i] > 0.75) {
                    res += "#";
                }
                else if (centroid[j*28 + i] > 0.5) {
                    res += "o";
                }
                else if (centroid[j*28 + i] > 0.25) {
                    res += "~";
                }
                else res += ".";
            }
            res += "\n";
        }
        return res;
    }
}