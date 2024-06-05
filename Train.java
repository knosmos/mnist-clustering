import java.io.IOException;
import java.util.*;

public class Train {
    private static int CLASSES = 10;
    private static int n;
    private static Sample[] samples;
    private static ArrayList<Cluster> clusters;

    public static void main(String[] args) throws IOException {
        // load samples
        n = 300;
        samples = new Sample[n];
        for (int i=0; i<n; i++) {
            Sample s = new Sample("train/img_" + (i+1) + ".jpg");
            samples[i] = s;
        }

        // run clustering
        //HashMap<Integer, ArrayList<Integer>> clusterResult = runClustering();
        

        // Generate visualizations of clusters
        for (Cluster cluster: clusters) {
            cluster.calculateCentroid();
            System.out.println(cluster.toString());
        }
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
        
        for (int i=0; i<20; i++) {
            System.out.printf("[%d %d, wt: %2f]\n", edges[i][0].intValue(), edges[i][1].intValue(), edges[i][2]);
        }

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

        // because kruskal sucks too much, we run k-means afterwards
        
    }
}
