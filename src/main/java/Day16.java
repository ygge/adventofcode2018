import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Day16 {

    private static final List<BiFunction<int[], int[], int[]>> FUNCTIONS = Arrays.asList(
            Day16::addr, Day16::addi, Day16::mulr, Day16::muli, Day16::banr, Day16::bani, Day16::borr, Day16::bori,
            Day16::setr, Day16::seti, Day16::gtir, Day16::gtri, Day16::gtrr, Day16::eqir, Day16::eqri, Day16::eqrr
    );

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        List<int[][]> data = new ArrayList<>();
        int i = 0;
        for (; i < input.size(); ++i) {
            String b = input.get(i);
            if (!b.startsWith("Before: ")) {
                break;
            }
            int[] before = parse(b.substring(9, b.length()-1));
            int[] instructions = parse(input.get(++i));
            String a = input.get(++i);
            int[] after = parse(a.substring(9, a.length()-1));
            ++i;
            int[][] d = new int[3][];
            d[0] = before;
            d[1] = instructions;
            d[2] = after;
            data.add(d);
        }

        List<int[]> program = new ArrayList<>();
        while (i < input.size()) {
            String s = input.get(i++);
            if (s.length() > 0) {
                program.add(parse(s));
            }
        }

        // step 1
        step1(data);

        // step 2
        step2(data, program);
    }

    private static void step2(List<int[][]> data, List<int[]> program) {
        int[] mapping = new int[FUNCTIONS.size()];
        for (int i = 0; i < mapping.length; ++i) {
            mapping[i] = -1;
        }
        int set = 0;
        while (set < FUNCTIONS.size()) {
            for (int[][] d : data) {
                int matches = -1;
                for (int i = 0; i < FUNCTIONS.size(); ++i) {
                    if (mapping[i] != -1) {
                        continue;
                    }
                    int[] res = FUNCTIONS.get(i).apply(d[0], d[1]);
                    if (eq(res, d[2])) {
                        if (matches != -1) {
                            matches = -1;
                            break;
                        }
                        matches = i;
                    }
                }
                if (matches != -1) {
                    mapping[matches] = d[1][0];
                    ++set;
                }
            }
        }
        Map<Integer, BiFunction<int[], int[], int[]>> instructions = new HashMap<>();
        for (int i = 0; i < mapping.length; ++i) {
            instructions.put(mapping[i], FUNCTIONS.get(i));
        }

        int[] state = new int[]{ 0, 0, 0, 0 };
        for (int[] ins : program) {
            state = instructions.get(ins[0]).apply(state, ins);
        }
        System.out.println(state[0]);
    }

    private static void step1(List<int[][]> data) {
        int moreThan3 = 0;
        for (int[][] d : data) {
            int matches = (int) FUNCTIONS.stream()
                    .filter(f -> eq(f.apply(d[0], d[1]), d[2]))
                    .count();
            moreThan3 += matches >= 3 ? 1 : 0;
        }
        System.out.println(moreThan3);
    }

    private static boolean eq(int[] a, int[] b) {
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static int[] addr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]+res[i[2]];
        return res;
    }

    private static int[] addi(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]+i[2];
        return res;
    }

    private static int[] mulr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]*res[i[2]];
        return res;
    }

    private static int[] muli(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]*i[2];
        return res;
    }

    private static int[] banr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]&res[i[2]];
        return res;
    }

    private static int[] bani(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]&i[2];
        return res;
    }

    private static int[] borr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]|res[i[2]];
        return res;
    }

    private static int[] bori(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]]|i[2];
        return res;
    }

    private static int[] gtir(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = i[1] > res[i[2]] ? 1 : 0;
        return res;
    }

    private static int[] gtri(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]] > i[2] ? 1 : 0;
        return res;
    }

    private static int[] gtrr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]] > res[i[2]] ? 1 : 0;
        return res;
    }

    private static int[] setr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]];
        return res;
    }

    private static int[] seti(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = i[1];
        return res;
    }

    private static int[] eqir(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = i[1] == res[i[2]] ? 1 : 0;
        return res;
    }

    private static int[] eqri(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]] == i[2] ? 1 : 0;
        return res;
    }

    private static int[] eqrr(int[] r, int[] i) {
        int[] res = copy(r);
        res[i[3]] = res[i[1]] == res[i[2]] ? 1 : 0;
        return res;
    }

    private static int[] copy(int[] r) {
        int[] res = new int[r.length];
        System.arraycopy(r, 0, res, 0, r.length);
        return res;
    }

    private static int[] parse(String s) {
        String[] split = s.split(",? ");
        int[] data = new int[split.length];
        for (int i = 0; i < split.length; ++i) {
            data[i] = Integer.parseInt(split[i]);
        }
        return data;
    }
}
