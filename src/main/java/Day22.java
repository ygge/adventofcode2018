import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class Day22 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        int depth = Integer.parseInt(input.get(0).substring(input.get(0).indexOf(' ')+1));
        String[] xy = input.get(1).substring(input.get(1).indexOf(' ')+1).split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);

        int[][] cave = new int[10*y+1][10*x+1];
        int sum = 0;
        for (int i = 0; i < cave.length; ++i) {
            for (int j = 0; j < cave[i].length; ++j) {
                int geo;
                if ((i == 0 && j == 0) || (i == y && j == x)) {
                    geo = 0;
                } else if (i == 0) {
                    geo = j*16807;
                } else if (j == 0) {
                    geo = i*48271;
                } else {
                    geo = cave[i-1][j]*cave[i][j-1];
                }
                cave[i][j] = (geo+depth)%20183;
                if (i <= y && j <= x) {
                    int type = cave[i][j]%3;
                    sum += type;
                }
            }
        }

        // Step 1
        System.out.println(sum);

        // Step 2
        Map<Node, Integer> seen = new HashMap<>();
        Queue<Node> que = new PriorityQueue<>(Comparator.comparingInt(a -> a.time));
        que.add(new Node(0, 0, 0, Equipment.TORCH));
        while (!que.isEmpty()) {
            Node node = que.poll();
            if (node.x == x && node.y == y && node.equip == Equipment.TORCH) {
                System.out.println(node.time);
                break;
            }
            Integer value = seen.get(node);
            if (value != null && value <= node.time) {
                continue;
            }
            seen.put(node, node.time);
            if (node.equip != Equipment.TORCH && allowed(cave, node.x, node.y, Equipment.TORCH)) {
                que.add(new Node(node.x, node.y, node.time + 7, Equipment.TORCH));
            }
            if (node.equip != Equipment.CLIMBING_GEAR && allowed(cave, node.x, node.y, Equipment.CLIMBING_GEAR)) {
                que.add(new Node(node.x, node.y, node.time + 7, Equipment.CLIMBING_GEAR));
            }
            if (node.equip != null && allowed(cave, node.x, node.y, null)) {
                que.add(new Node(node.x, node.y, node.time + 7, null));
            }
            if (allowed(cave, node.x, node.y-1, node.equip)) {
                que.add(new Node(node.x, node.y-1, node.time + 1, node.equip));
            }
            if (allowed(cave, node.x, node.y+1, node.equip)) {
                que.add(new Node(node.x, node.y+1, node.time + 1, node.equip));
            }
            if (allowed(cave, node.x-1, node.y, node.equip)) {
                que.add(new Node(node.x-1, node.y, node.time + 1, node.equip));
            }
            if (allowed(cave, node.x+1, node.y, node.equip)) {
                que.add(new Node(node.x+1, node.y, node.time + 1, node.equip));
            }
        }

        //printCave(cave);
    }

    private static boolean allowed(int[][] cave, int x, int y, Equipment equipment) {
        if (x < 0 || y < 0 || y >= cave.length || x >= cave[0].length) {
            return false;
        }
        int type = cave[y][x]%3;
        if (type == 0) {
            return equipment == Equipment.CLIMBING_GEAR || equipment == Equipment.TORCH;
        } else if (type == 1) {
            return equipment == Equipment.CLIMBING_GEAR || equipment == null;
        }
        return equipment == Equipment.TORCH || equipment == null;
    }

    private static void printCave(int[][] cave) {
        for (int[] ints : cave) {
            for (int anInt : ints) {
                int a = anInt%3;
                char c;
                if (a == 0) {
                    c = '.';
                } else if (a == 1) {
                    c = '=';
                } else {
                    c = '|';
                }
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static class Node {
        private final int x, y, time;
        private final Equipment equip;

        private Node(int x, int y, int time, Equipment equip) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.equip = equip;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x &&
                    y == node.y &&
                    equip == node.equip;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, equip);
        }
    }

    private enum Equipment {
        TORCH,
        CLIMBING_GEAR
    }
}
