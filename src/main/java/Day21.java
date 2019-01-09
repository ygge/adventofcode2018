import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class Day21 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        Map<String, BiFunction<int[], int[], int[]>> functions = new HashMap<>();
        functions.put("addr", Day21::addr);
        functions.put("addi", Day21::addi);
        functions.put("mulr", Day21::mulr);
        functions.put("muli", Day21::muli);
        functions.put("banr", Day21::banr);
        functions.put("bani", Day21::bani);
        functions.put("borr", Day21::borr);
        functions.put("bori", Day21::bori);
        functions.put("setr", Day21::setr);
        functions.put("seti", Day21::seti);
        functions.put("gtir", Day21::gtir);
        functions.put("gtri", Day21::gtri);
        functions.put("gtrr", Day21::gtrr);
        functions.put("eqir", Day21::eqir);
        functions.put("eqri", Day21::eqri);
        functions.put("eqrr", Day21::eqrr);

        int ipr = Integer.parseInt(input.get(0).split(" ")[1]);
        List<Instruction> program = new ArrayList<>();
        for (String row : input) {
            if (row.startsWith("#ip ")) {
                continue;
            }
            if (row.trim().length() == 0) {
                break;
            }
            program.add(parse(row));
        }

        // Step 1
        run(functions, ipr, program, true);

        // Step 2
        run(functions, ipr, program, false);
    }

    private static void run(Map<String, BiFunction<int[], int[], int[]>> functions, int ipr, List<Instruction> program, boolean breakOnFirst) {
        int[] registers = new int[6];
        int ip = 0;
        registers[0] = 0;
        int count = 0;
        int last = 0;
        Set<Integer> seen = new HashSet<>();
        while (ip < program.size()) {
            registers[ipr] = ip;
            ++count;
            if (ip == 28) {
                if (breakOnFirst) {
                    System.out.println(registers[1]);
                    break;
                }
                if (!seen.add(registers[1])) {
                    System.out.println(last);
                    break;
                }
                last = registers[1];
            }
            Instruction instruction = program.get(ip);
            functions.get(instruction.op).apply(registers, instruction.params);
            ip = registers[ipr];
            ++ip;
        }
        System.out.println("Done in " + count);
    }

    private static String registers(int[] registers) {
        StringBuilder sb = new StringBuilder("[");
        for (int register : registers) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(String.format("%9d", register));
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

        private Instruction(String op, int[] params) {
            this.op = op;
            this.params = params;
        }
    }
}
