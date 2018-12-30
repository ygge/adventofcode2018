import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day7 {

    public static void main(String[] args) throws IOException {
        List<Edge> edges = Util.readFile(Edge::new);

        Set<String> seen = new HashSet<>();
        for (Edge edge : edges) {
            seen.add(edge.from);
            seen.add(edge.to);
        }

        // step 1
        Set<String> available = new HashSet<>(seen);
        for (Edge edge : edges) {
            available.remove(edge.to);
        }
        runStep1(edges, seen, available);

        // step 2
        available = new HashSet<>(seen);
        for (Edge edge : edges) {
            available.remove(edge.to);
        }
        Elf[] elves = new Elf[5];
        for (int i = 0; i < elves.length; ++i) {
            elves[i] = new Elf();
        }
        Set<String> completed = new HashSet<>();
        Set<String> workingOn = new HashSet<>();
        int current = 0;
        while (!available.isEmpty() || isWorking(elves)) {
            while (!available.isEmpty() && notWorking(elves) != null) {
                String next = Collections.min(available);
                if (next == null) {
                    throw new RuntimeException(available.toString());
                }
                available.remove(next);
                workingOn.add(next);
                Elf elf = notWorking(elves);
                if (elf == null) {
                    throw new RuntimeException("Elf not found");
                }
                elf.task = next;
                elf.time = current + 61 + next.charAt(0)-'A';
            }
            int smallestTime = Integer.MAX_VALUE;
            for (Elf elf : elves) {
                if (elf.task != null) {
                    smallestTime = Math.min(smallestTime, elf.time);
                }
            }
            if (smallestTime == Integer.MAX_VALUE) {
                throw new RuntimeException("Max value");
            }
            current = smallestTime;
            for (Elf elf : elves) {
                if (elf.task != null && elf.time == current) {
                    completed.add(elf.task);
                    workingOn.remove(elf.task);
                    elf.task = null;
                }
            }
            for (String s : seen) {
                if (!completed.contains(s) && !available.contains(s) && !workingOn.contains(s)) {
                    boolean canAdd = true;
                    for (Edge edge : edges) {
                        if (edge.to.equals(s)
                                && !completed.contains(edge.from)
                                && !available.contains(edge.to)) {
                            canAdd = false;
                            break;
                        }
                    }
                    if (canAdd) {
                        available.add(s);
                    }
                }
            }
        }
        System.out.println(current);
    }

    private static Elf notWorking(Elf[] elves) {
        for (Elf elf : elves) {
            if (elf.task == null) {
                return elf;
            }
        }
        return null;
    }

    private static boolean isWorking(Elf[] elves) {
        for (Elf elf : elves) {
            if (elf.task != null) {
                return true;
            }
        }
        return false;
    }

    private static class Elf {
        private int time = 0;
        private String task = null;
    }

    private static void runStep1(List<Edge> edges, Set<String> seen, Set<String> available) {
        Set<String> used = new HashSet<>();
        StringBuilder step1 = new StringBuilder();
        while (!available.isEmpty()) {
            String next = Collections.min(available);
            step1.append(next);
            used.add(next);
            available.remove(next);
            for (String s : seen) {
                if (!used.contains(s) && !available.contains(s)) {
                    boolean canAdd = true;
                    for (Edge edge : edges) {
                        if (edge.to.equals(s) && !used.contains(edge.from) && !available.contains(edge.to)) {
                            canAdd = false;
                            break;
                        }
                    }
                    if (canAdd) {
                        available.add(s);
                    }
                }
            }
        }
        System.out.println(step1);
    }

    private static class Edge {
        private final String from, to;

        private Edge(String row) {
            String[] s = row.split(" ");
            from = s[1];
            to = s[7];
        }
    }
}
