import java.util.*;

public class Train {
    private static int CLASSES = 10;
    private static int n;
    private static Sample[] samples;
    private static ArrayList<Cluster> clusters;

    public static void main(String[] args) {
        // load samples
        n = 100;
        samples = new Sample[n];
        for (int i=0; i<n; i++) {
            Sample s = new Sample();
            s.load("REPLACEME.png");
            samples[i] = s;
        }

        // run clustering
        HashMap<Integer, ArrayList<Integer>> clusterResult = runClustering();

        // clean up and generate clusters
        clusters = new ArrayList<Cluster>();
        for (ArrayList<Integer> c: clusterResult.values()) {
            Sample[] cluster_samples = new Sample[c.size()];
            for (int i=0; i<c.size(); i++) {
                cluster_samples[i] = samples[c[i]];
            }
            clusters.add(new Cluster(cluster_samples));
        }
    }
    public static HashMap<Integer, ArrayList<Integer>> runClustering() {
        // Kruskal's algorithm
        // generate edges
        Double[][] edges = new Double[n][3]; // a, b, weight
        for (int i=0; i<n; i++) {
            for (int j=0; j<i; j++) {
                edges[i*n + j] = new Double[] {(double)i, (double)j, samples[i].distance(samples[j])};
            }
        }
        // sort edges
        Comparator<Double[]> edgeComparator = Comparator.comparing(c -> -c[2]);
        Arrays.sort(edges, edgeComparator);

        // construct DSU
        DSU dsu = new DSU(n);

        // add edges one by one
        int numComponents = n;
        for (Double[] edge: edges) {
            if (!dsu.same(edge[0].intValue(), edge[1].intValue())) {
                dsu.merge(edge[0].intValue(), edge[1].intValue());
                numComponents--;
            }
            if (numComponents == CLASSES) {
                break;
            }
        }

        // generate connected components
        return dsu.readout();
    }
}
