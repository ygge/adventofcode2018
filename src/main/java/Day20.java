import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day20 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();
        String route = input.get(0);
        Map<Pos, Node> nodes = new HashMap<>();

        Node node = addNode(nodes, new Pos(0, 0), null);
        parse(nodes, node, route, 1);

        for (Node value : nodes.values()) {
            System.out.println(value.toPrint());
        }

        Set<Pos> seen = new HashSet<>();
        Deque<Room> que = new LinkedList<>();
        que.add(new Room(node, 0));
        int maxSteps = 0;
        int num1000 = 0;
        while (!que.isEmpty()) {
            Room room = que.poll();
            if (!seen.add(room.node.pos)) {
                continue;
            }
            maxSteps = Math.max(maxSteps, room.steps);
            if (room.steps >= 1000) {
                ++num1000;
            }
            for (Node otherNode : room.node.doors) {
                que.add(new Room(otherNode, room.steps+1));
            }
        }

        // step 1
        System.out.println(maxSteps);

        // step 2
        System.out.println(num1000);
    }

    private static int parse(Map<Pos, Node> nodes, Node node, String route, int index) {
        int i = index;
        Node loopNode = node;
        while (true) {
            char c = route.charAt(i);
            if (c == '|' || c == ')') {
                return i;
            } else if (c == '(') {
                while (true) {
                    i = parse(nodes, loopNode, route, i+1);
                    char cc = route.charAt(i);
                    if (cc == ')') {
                        ++i;
                        break;
                    } else if (cc != '|') {
                        throw new RuntimeException("index: " + i);
                    }
                }
            } else {
                Pos newPos = toPos(loopNode.pos, c);
                if (newPos != null) {
                    loopNode = addNode(nodes, newPos, loopNode);
                    ++i;
                } else {
                    break;
                }
            }
        }
        return i;
    }

    /*
#############
#.|.|.|.|.|.#
#-#####-###-#
#.#.|.#.#.#.#
#-#-###-#-#-#
#.#.#.|.#.|.#
#-#-#-#####-#
#.#.#.#X|.#.#
#-#-#-###-#-#
#.|.#.|.#.#.#
###-#-###-#-#
#.|.#.|.|.#.#
#############
     */

    private static Node addNode(Map<Pos, Node> nodes, Pos pos, Node previous) {
        Node node = nodes.get(pos);
        if (node == null) {
            node = new Node(pos);
            nodes.put(pos, node);
        }
        if (previous != null) {
            previous.doors.add(node);
            node.doors.add(previous);
        }
        return node;
    }

    private static Pos toPos(Pos pos, char c) {
        if (c == 'N') {
            return new Pos(pos.x, pos.y-1);
        } else if (c == 'E') {
            return new Pos(pos.x+1, pos.y);
        } else if (c == 'S') {
            return new Pos(pos.x, pos.y+1);
        } else if (c == 'W') {
            return new Pos(pos.x-1, pos.y);
        }
        return null;
    }

    private static class Node {
        private final Pos pos;
        private final List<Node> doors = new ArrayList<>();

        private Node(Pos pos) {
            this.pos = pos;
        }

        private String toPrint() {
            return "Node{" +
                    "pos=" + pos +
                    ", doors=" + doors +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(pos, node.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "pos=" + pos +
                    '}';
        }
    }

    private static class Pos {
        private final int x, y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return x == pos.x &&
                    y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class Room {
        private final Node node;
        private final int steps;

        private Room(Node node, int steps) {
            this.node = node;
            this.steps = steps;
        }
    }
}
