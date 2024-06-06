/*
 * Interactive visualizer of cluster centroids
 * 
 * Jieruei Chang
 * Java 11
 * 4/6/2024
 */

import java.util.*;
import java.io.*;

public class Interactive {
    private static double[] current;
    private static ArrayList<Sample> centroids;

    public static void main(String args[]) throws IOException {
        // load clusters
        Scanner sc = new Scanner(new File("clusters.txt"));
        centroids = new ArrayList<Sample>();
        for (int i=0; i<10; i++) {
            double[] vals = new double[28*28];
            for (int j=0; j<28*28; j++) {
                vals[j] = sc.nextDouble();
            }
            centroids.add(new Sample(vals));
        }
        
        // initialize
        current = new double[28*28];
        for (int i=0; i<28*28; i++) current[i]=0;

        // set up window
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(800, 400);
        StdDraw.setXscale(-1, 28 + 2 + 28 + 0.5);
        StdDraw.setYscale(30 + 0.5, -1);

        // main loop
        while (true) {
            run();
        }
    }

    public static void run() {
        StdDraw.clear();
        
        // draw grid
        for (int i=0; i<28; i++) {
            for (int j=0; j<28; j++) {
                int r = (int)(current[i*28 + j] * (255 - 82)) + 82;
                int g = (int)(current[i*28 + j] * (255 - 18)) + 18;
                int b = (int)(current[i*28 + j] * (255 - 79)) + 79;

                StdDraw.setPenColor(r, g, b);
                StdDraw.filledRectangle(i, j, 0.5, 0.5);
                StdDraw.setPenColor(105, 22, 101);
                StdDraw.rectangle(i, j, 0.5, 0.5);
            }
        }

        // find closest centroid
        double best_dist = 1000000000;
        int best_idx = 0;
        for (int i=0; i<centroids.size(); i++) {
            double dist = centroids.get(i).distance(current);
            if (dist < best_dist) {
                best_dist = dist;
                best_idx = i;
            }
        }

        // draw closest centroid
        for (int i=0; i<28; i++) {
            for (int j=0; j<28; j++) {
                int g = (int) (centroids.get(best_idx).getData()[i*28+j] * 255);
                StdDraw.setPenColor(g, g, g);
                StdDraw.filledRectangle(i + 30, j, 0.5, 0.5);
                StdDraw.setPenColor(0, 0, 0);
                StdDraw.rectangle(i + 30, j, 0.5, 0.5);
            }
        }

        // user input
        if (StdDraw.isMousePressed()) {
            int c = (int)StdDraw.mouseX();
            int r = (int)StdDraw.mouseY();
            if (0 <= r && r < 28 && 0 <= c && c < 28) {
                // paint grid
                for (int i=0; i<28; i++) {
                    for (int j=0; j<28; j++) {
                        double delta = Math.max(0, (double)(5-(r-i)*(r-i)-(c-j)*(c-j))/10);
                        current[j*28 + i] = Math.min(current[j*28 + i] + delta, 1);
                    }
                }
            }
            else {
                // clear
                for (int i=0; i<28*28; i++) current[i]=0;
            }
        }

        StdDraw.show();
        StdDraw.pause(5);
    }
}