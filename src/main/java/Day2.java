import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(Day2.class.getResourceAsStream("/2.in")));
        String row;
        int two = 0;
        int three = 0;
        List<String> values = new ArrayList<String>();
        while ((row = in.readLine()) != null) {
            Map<Character, Integer> seen = new HashMap<Character, Integer>();
            for (int i = 0; i < row.length(); ++i) {
                char c = row.charAt(i);
                Integer prev = seen.get(c);
                seen.put(c, (prev == null ? 0 : prev) + 1);
            }
            for (Integer value : seen.values()) {
                if (value == 2) {
                    ++two;
                    break;
                }
            }
            for (Integer value : seen.values()) {
                if (value == 3) {
                    ++three;
                    break;
                }
            }
            values.add(row);
        }
        System.out.println(two*three);
        for (String value : values) {
            for (String s : values) {
                int diff = -1;
                for (int i = 0; i < s.length(); ++i) {
                    if (value.charAt(i) != s.charAt(i)) {
                        if (diff != -1) {
                            diff = -2;
                            break;
                        }
                        diff = i;
                    }
                }
                if (diff >= 0) {
                    System.out.println(value.substring(0, diff) + value.substring(diff+1));
                }
            }
        }
    }
}
