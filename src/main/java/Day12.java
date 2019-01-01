import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        String state = input.get(0).substring(15);
        Set<String> plant = new HashSet<>();
        for (int i = 2; i < input.size(); ++i) {
            String v = input.get(i);
            if (v.substring(v.length() - 1).equals("#")) {
                plant.add(v.substring(0, 5));
            }
        }

        // part 1
        long score = runSimulation(state, plant, 20);
        System.out.println(score);

        // part 2
        System.out.println(runSimulation(state, plant, 50000000000L));
    }

    private static long runSimulation(String state, Set<String> plant, long turns) {
        List<String> seen = new ArrayList<>();
        long added = 0;
        for (long i = 0; i < turns; ++i) {
            state = "...." + state + "....";
            added += 4;
            StringBuilder sb = new StringBuilder();
            for (int p = 2; p < state.length()-3; ++p) {
                if (plant.contains(state.substring(p-2, p+3))) {
                    sb.append('#');
                } else {
                    sb.append('.');
                }
            }
            added -= 2;
            state = sb.toString();
            int pos = state.indexOf('#');
            if (pos > 4) {
                state = state.substring(pos-4);
                added -= pos-4;
            }
            pos = seen.indexOf(state);
            if (pos != -1) {
                int index = (int)((turns-i)%(seen.size()-pos));
                state = seen.get(pos+index);
                added -= turns-i-1;
                break;
            }
            seen.add(state);
        }
        return score(state, added);
    }

    private static long score(String state, long added) {
        long score = 0;
        for (int i = 0; i < state.length(); ++i) {
            if (state.charAt(i) == '#') {
                score += i-added;
            }
        }
        return score;
    }
}
