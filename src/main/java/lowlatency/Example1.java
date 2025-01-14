package lowlatency;

import java.util.ArrayList;
import java.util.List;

public class Example1 {
    public static void main(String[] args) {

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1_00_000; i++) {
            list.add(new byte[1_024]);  // Allocate 1 KB objects
            if (i % 100_000 == 0) {
                System.out.println("Created " + i + " objects");
            }
        }
        list = null;
        System.out.println("All objects are eligible for GC");
        System.gc();
    }
}
