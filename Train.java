/*
 * Kruskal/K-means Clustering
 * 
 * Jieruei Chang
 * Java 11
 * 4/6/2024
 */

import java.io.*;
import java.util.*;

public class Train {
    private static int CLASSES = 10;
    private static int n;
    private static Sample[] samples;
    private static ArrayList<Cluster> clusters;

    public static void main(String[] args) throws IOException {
        // load samples
        File folder = new File("archive/trainingSet/trainingSet");
        n = 5000;
        ArrayList<String> filenames = listFiles(folder);
        // n = filenames.size();
        samples = new Sample[n];
        for (int i=0; i<n; i++) {
            Sample s = new Sample(
                filenames.get(
                    (int)(Math.random() * filenames.size())
                )
            );
            samples[i] = s;
        }
        System.out.println(n + " samples loaded");

        // run clustering
        clusters = runClustering();

        // Generate visualizations of clusters and ask for labeling
        for (Cluster cluster: clusters) {
            cluster.calculateCentroid();
            System.out.println(cluster.toString());
        }

        // Store clusters
        FileWriter writer = new FileWriter("clusters.txt");
        for (Cluster cluster: clusters) {
            writer.write(cluster.export());
        }
        writer.close();
    }
    public static ArrayList<Cluster> runClustering() {
        // Kruskal's algorithm
        // generate edges
        Double[][] edges = new Double[n * (n-1) / 2][3]; // a, b, weight
        int ctr = 0;
        for (int i=0; i<n; i++) {
            for (int j=0; j<i; j++) {
                edges[ctr] = new Double[] {(double)i, (double)j, samples[i].distance(samples[j])};
                ctr++;
            }
        }
        // sort edges
        Comparator<Double[]> edgeComparator = Comparator.comparing(c -> c[2]);
        Arrays.sort(edges, edgeComparator);
        
        // print out smallest edges (sanity check)
        /* 
        for (int i=0; i<20; i++) {
            System.out.printf("[%d %d, wt: %2f]\n", edges[i][0].intValue(), edges[i][1].intValue(), edges[i][2]);
        }
        */

        // construct DSU
        DSU dsu = new DSU(n);

        // add edges one by one
        int numComponents = n;
        for (Double[] edge: edges) {
            if (!dsu.same(edge[0].intValue(), edge[1].intValue())) {
                dsu.merge(edge[0].intValue(), edge[1].intValue());
                System.out.printf("merging %d %d\n", edge[0].intValue(), edge[1].intValue());
                numComponents--;
            }
            if (numComponents == CLASSES) {
                break;
            }
        }

        // generate connected components
        HashMap<Integer, ArrayList<Integer>> clusterResult = dsu.readout();

        // clean up and generate clusters
        clusters = new ArrayList<Cluster>();
        for (ArrayList<Integer> c: clusterResult.values()) {
            Sample[] cluster_samples = new Sample[c.size()];
            for (int i=0; i<c.size(); i++) {
                cluster_samples[i] = samples[c.get(i)];
            }
            clusters.add(new Cluster(cluster_samples));
        }

        // Experiment: start k-means from random samples rather than Kruskal result
        /*
        clusters = new ArrayList<Cluster>();
        for (int i=0; i<CLASSES; i++) {
            Cluster c = new Cluster();
            c.addSample(samples[(int)(Math.random() * n)]);
            clusters.add(c);
        }*/

        // Run second-stage k-means afterwards
        for (int it=0; it<100; it++) {
            ArrayList<double[]> centroids = new ArrayList<double[]>();

            // get centroids
            for (Cluster cluster: clusters) {
                cluster.calculateCentroid();
                centroids.add(cluster.getCentroid());
            }

            // build new clusters: iterate through each sample
            // and assign to the nearest centroid
            ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
            for (int i=0; i<centroids.size(); i++) {
                newClusters.add(new Cluster());
            }
            for (Sample s: samples) {
                double best_dist = 1000000000;
                int best_idx = 0;
                for (int i=0; i<centroids.size(); i++) {
                    double dist = s.distance(centroids.get(i));
                    if (dist < best_dist) {
                        best_dist = dist;
                        best_idx = i;
                    }
                }
                newClusters.get(best_idx).addSample(s);
            }

            // update clusters
            clusters = newClusters;
        }
        return clusters;
    }

    /* Detect training files */
    public static ArrayList<String> listFiles(File folder) {
        ArrayList<String> files = new ArrayList<String>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                for (String f: listFiles(fileEntry)) files.add(f);
            } else {
                String path = fileEntry.getPath();
                if (path.endsWith(".jpg")) files.add(path);
            }
        }
        return files;
    }
}
