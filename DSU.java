/*
 * Disjoint-set Union / Union-Find implementation
 */

import java.util.*;

public class DSU {
    private int[] par;
    private int[] sz;
    private int n;
    public DSU(int n) {
        // initialize such that all nodes are
        // initially separate
        this.par = new int[n];
        this.sz = new int[n];
        this.n = n;
        for (int i=0; i<n; i++) {
            this.par[i] = i;
            this.sz[i] = 1;
        }
    }
    public void merge(int u, int v) {
        // merge two connected components,
        // making the smaller component a subtree
        // of the larger one
        int root_u = root(u);
        int root_v = root(v);
        if (root_u == root_v) {
            return;
        }
        if (sz[root_u] > sz[root_v]) {
            // merge v into u
            par[root_v] = root_u;
            sz[root_u] += sz[root_v];
        }
        else {
            // merge u into v
            par[root_u] = root_v;
            sz[root_v] += sz[root_u];
        }
    }
    public boolean same(int u, int v) {
        // test connectedness between two vertices
        return root(u) == root(v);
    }
    public HashMap<Integer, ArrayList<Integer>> readout() {
        // calculate and determine all connected components
        HashMap<Integer, ArrayList<Integer>> mapping = new HashMap<Integer, ArrayList<Integer>>();
        for (int i=0; i<this.n; i++) {
            int root_i = root(i);
            if (!mapping.containsKey(root_i)) {
                mapping.put(root_i, new ArrayList<Integer>());
            }
            mapping.get(root_i).add(i);
        }
        return mapping;
    }
    private int root(int v) {
        // internal method to determine the root
        // of a vertex, reducing the depth of the
        // tree in the process
        int x = par[v];
        while (par[x] != x) {
            x = par[x];
        }
        par[v] = x;
        return x;
    }
}