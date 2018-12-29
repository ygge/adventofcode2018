import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream("/1.in")));
        String row;
        List<Integer> input = new ArrayList<Integer>();
        while ((row = in.readLine()) != null) {
            input.add(Integer.parseInt(row));
        }
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
