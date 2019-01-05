import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Day19 {

    private static final List<BiFunction<int[], int[], int[]>> FUNCTIONS = Arrays.asList(
            Day19::addr, Day19::addi, Day19::mulr, Day19::muli, Day19::banr, Day19::bani, Day19::borr, Day19::bori,
            Day19::setr, Day19::seti, Day19::gtir, Day19::gtri, Day19::gtrr, Day19::eqir, Day19::eqri, Day19::eqrr
    );

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        Map<String, BiFunction<int[], int[], int[]>> functions = new HashMap<>();
        functions.put("addr", Day19::addr);
        functions.put("addi", Day19::addi);
        functions.put("mulr", Day19::mulr);
        functions.put("muli", Day19::muli);
        functions.put("banr", Day19::banr);
        functions.put("bani", Day19::bani);
        functions.put("borr", Day19::borr);
        functions.put("bori", Day19::bori);
        functions.put("setr", Day19::setr);
        functions.put("seti", Day19::seti);
        functions.put("gtir", Day19::gtir);
        functions.put("gtri", Day19::gtri);
        functions.put("gtrr", Day19::gtrr);
        functions.put("eqir", Day19::eqir);
        functions.put("eqri", Day19::eqri);
        functions.put("eqrr", Day19::eqrr);

        int ipr = Integer.parseInt(input.get(0).split(" ")[1]);
        List<Instruction> program = new ArrayList<>();
        for (String row : input) {
            if (row.startsWith("#ip ")) {
                continue;
            }
            program.add(parse(row));
        }

        // Step 1
        run(functions, ipr, program, 0);

        // Step 2
        //run(functions, ipr, program, 1);
    }

    private static void run(Map<String, BiFunction<int[], int[], int[]>> functions, int ipr, List<Instruction> program, int start0) {
        int[] registers = new int[6];
        registers[0] = start0;
        int ip = 0;
        while (ip < program.size()) {
            System.out.println("ip: " + ip + " - " + toString(registers));
            registers[ipr] = ip;
            Instruction instruction = program.get(ip);
            functions.get(instruction.op).apply(registers, instruction.params);
            ip = registers[ipr];
            ++ip;
        }
        System.out.println(registers[0]);
    }

    private static String toString(int[] registers) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < registers.length; ++i) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(registers[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static int[] addr(int[] r, int[] i) {
        r[i[3]] = r[i[1]]+ r[i[2]];
        return r;
    }

    private static int[] addi(int[] r, int[] i) {
        r[i[3]] = r[i[1]]+i[2];
        return r;
    }

    private static int[] mulr(int[] r, int[] i) {
        r[i[3]] = r[i[1]]* r[i[2]];
        return r;
    }

    private static int[] muli(int[] r, int[] i) {
        r[i[3]] = r[i[1]]*i[2];
        return r;
    }

    private static int[] banr(int[] r, int[] i) {
        r[i[3]] = r[i[1]]& r[i[2]];
        return r;
    }

    private static int[] bani(int[] r, int[] i) {
        r[i[3]] = r[i[1]]&i[2];
        return r;
    }

    private static int[] borr(int[] r, int[] i) {
        r[i[3]] = r[i[1]]| r[i[2]];
        return r;
    }

    private static int[] bori(int[] r, int[] i) {
        r[i[3]] = r[i[1]]|i[2];
        return r;
    }

    private static int[] gtir(int[] r, int[] i) {
        r[i[3]] = i[1] > r[i[2]] ? 1 : 0;
        return r;
    }

    private static int[] gtri(int[] r, int[] i) {
        r[i[3]] = r[i[1]] > i[2] ? 1 : 0;
        return r;
    }

    private static int[] gtrr(int[] r, int[] i) {
        r[i[3]] = r[i[1]] > r[i[2]] ? 1 : 0;
        return r;
    }

    private static int[] setr(int[] r, int[] i) {
        r[i[3]] = r[i[1]];
        return r;
    }

    private static int[] seti(int[] r, int[] i) {
        r[i[3]] = i[1];
        return r;
    }

    private static int[] eqir(int[] r, int[] i) {
        r[i[3]] = i[1] == r[i[2]] ? 1 : 0;
        return r;
    }

    private static int[] eqri(int[] r, int[] i) {
        r[i[3]] = r[i[1]] == i[2] ? 1 : 0;
        return r;
    }

    private static int[] eqrr(int[] r, int[] i) {
        r[i[3]] = r[i[1]] == r[i[2]] ? 1 : 0;
        return r;
    }

    private static Instruction parse(String s) {
        String[] split = s.split(",? ");
        int[] data = new int[split.length];
        for (int i = 1; i < split.length; ++i) {
            data[i] = Integer.parseInt(split[i]);
        }
        return new Instruction(split[0], data);
    }

    private static class Instruction {
        private final String op;
        private final int[] params;

        public Instruction(String op, int[] params) {
            this.op = op;
            this.params = params;
        }
    }
}
