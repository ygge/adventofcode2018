import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day3 {

    public static void main(String[] args) throws IOException {
        List<Claim> claims = Util.readFile(Claim::new);
        Map<Node, Integer> squares = new HashMap<>();
        for (Claim claim : claims) {
            for (int i = 0; i < claim.w; ++i) {
                for (int j = 0; j < claim.h; ++j) {
                    Node node = new Node(claim.x + i, claim.y + j);
                    Integer v = squares.get(node);
                    squares.put(node, (v == null ? 0 : v) + 1);
                }
            }
        }

        // part 1
        long count = squares.values().stream()
                .filter(v -> v > 1)
                .count();
        System.out.println("Count: " + count);

        // part 2
        for (Claim claim : claims) {
            boolean overlap = false;
            for (int i = 0; !overlap && i < claim.w; ++i) {
                for (int j = 0; j < claim.h; ++j) {
                    Node node = new Node(claim.x + i, claim.y + j);
                    if (squares.get(node) != 1) {
                        overlap = true;
                        break;
                    }
                }
            }
            if (!overlap) {
                System.out.println("id: " + claim.id);
            }
        }
    }

    private static class Claim {
        private final int id, x, y, w, h;

        private Claim(String row) {
            String[] s = row.split(" ");
            id = Integer.parseInt(s[0].substring(1));
            String pos = s[2].substring(0, s[2].length()-1);
            String[] poss = pos.split(",");
            x = Integer.parseInt(poss[0]);
            y = Integer.parseInt(poss[1]);
            String size = s[3];
            String[] ss = size.split("x");
            w = Integer.parseInt(ss[0]);
            h = Integer.parseInt(ss[1]);
        }
    }

    private static class Node {
        private final int x, y;

        private Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x &&
                    y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
