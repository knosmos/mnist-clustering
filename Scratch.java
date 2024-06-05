import java.io.IOException;
import java.util.*;

public class Scratch {
    public static void main(String args[]) throws IOException {
        Sample a = new Sample("train/img_" + args[0] + ".jpg");
        Sample b = new Sample("train/img_" + args[1] + ".jpg");
        System.out.println(a.distance(b));

    }
}
