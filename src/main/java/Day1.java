import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

    public static void main(String[] args) throws IOException {
        List<Integer> input = Util.readInts();
        Set<Integer> seen = new HashSet<Integer>();
        int sum = 0;
        seen.add(sum);
        while (seen != null) {
            for (Integer n : input) {
                sum += n;
                if (!seen.add(sum)) {
                    System.out.println("seen: " + sum);
                    seen = null;
                    break;
                }
            }
        }
    }
}
