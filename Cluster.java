/*
 * Class to represent one detected cluster
 *
 * Jieruei Chang
 * Java 11
 * 4/6/2024
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

    /* Calculate center of mass of samples */
    public void calculateCentroid() {
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

    /* Add new sample */
    public void addSample(Sample sample) {
        Sample[] newSamples = new Sample[samples.length + 1];
        for (int i=0; i<samples.length; i++) {
            newSamples[i] = samples[i];
        }
        newSamples[samples.length] = sample;
        samples = newSamples;
    }

    /* Data for writing to file storage */
    public String export() {
        String res = "";
        for (int i=0; i<centroid.length; i++) {
            res += String.format("%.2f ", centroid[i]);
        }
        res += "\n";
        return res;
    }

    /* Pretty-print ASCII visualization */
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