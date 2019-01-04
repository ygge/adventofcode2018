import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Day16 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        // step 1
        List<BiFunction<int[], int[], int[]>> functions = Arrays.asList(
                Day16::addr, Day16::addi, Day16::mulr, Day16::muli, Day16::banr, Day16::bani, Day16::borr, Day16::bori,
                Day16::setr, Day16::seti, Day16::gtir, Day16::gtri, Day16::gtrr, Day16::eqir, Day16::eqri, Day16::eqrr
        );

        int moreThan3 = 0;
        for (int i = 0; i < input.size(); ++i) {
            String b = input.get(i);
            if (!b.startsWith("Before: ")) {
                break;
            }
            int[] before = parse(b.substring(9, b.length()-1));
            int[] instructions = parse(input.get(++i));
            String a = input.get(++i);
            int[] after = parse(a.substring(9, a.length()-1));
            ++i;

            int matches = (int)functions.stream()
                    .filter(f -> eq(f.apply(before, instructions), after))
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
