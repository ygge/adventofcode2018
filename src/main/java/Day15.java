import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day15 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        // step 1
        runSimulation(input, null);

        // step 2
        for (int i = 4; ; ++i) {
            if (runSimulation(input, i)) {
                break;
            }
        }
    }

    private static boolean runSimulation(List<String> input, Integer elfPower) {
        char[][] board = new char[input.size()][];
        List<Unit> units = new ArrayList<>();
        int numE = 0;
        int numG = 0;
        for (int i = 0; i < input.size(); ++i) {
            board[i] = input.get(i).toCharArray();
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == 'E') {
                    board[i][j] = '.';
                    units.add(new Unit(UnitType.Elf, j, i, elfPower == null ? 3 : elfPower));
                    ++numE;
                } else if (board[i][j] == 'G') {
                    board[i][j] = '.';
                    units.add(new Unit(UnitType.Goblin, j, i, 3));
                    ++numG;
                }
            }
        }

        Map<Pos, Unit> occupied = new HashMap<>();
        for (Unit unit : units) {
            occupied.put(unit.pos, unit);
        }

        int turns = 0;
        while (numE > 0 && numG > 0) {
            ++turns;
            Collections.sort(units);
            for (int i = 0; i < units.size(); ++i) {
                if (numE == 0 || numG == 0) {
                    --turns;
                    break;
                }
                Unit current = units.get(i);
                Node node = closestEnemy(occupied, board, current);
                if (node != null) {
                    if (node.unit.pos.dist(current.pos) > 1 && node.firstMove != null) {
                        occupied.remove(current.pos);
                        current.pos = node.firstMove;
                        occupied.put(current.pos, current);
                    }
                    if (node.unit.pos.dist(current.pos) == 1) {
                        Unit unit = getBestUnitToAttack(current, occupied);
                        unit.hp -= current.attack;
                        if (unit.hp <= 0) {
                            if (unit.type == UnitType.Elf && elfPower != null) {
                                return false;
                            }
                            occupied.remove(unit.pos);
                            if (units.indexOf(unit) < i) {
                                --i;
                            }
                            units.remove(unit);
                            if (unit.type == UnitType.Elf) {
                                --numE;
                            } else {
                                --numG;
                            }
                        }
                    }
                }
            }
        }
        int totalHp = units.stream()
                .map((unit) -> unit.hp)
                .reduce(0, (a, b) -> a+b);
        System.out.println(String.format("Done in %d turns, total hp=%d, points=%d with elfPower=%d", turns, totalHp, turns*totalHp, elfPower));
        return true;
    }

    private static Unit getBestUnitToAttack(Unit current, Map<Pos, Unit> occupied) {
        Unit best = null;
        List<Pos> targets = Arrays.asList(
                current.pos.up(),
                current.pos.left(),
                current.pos.right(),
                current.pos.down()
        );
        for (Pos target : targets) {
            Unit unit = occupied.get(target);
            if (unit != null && unit.type != current.type && (best == null || unit.hp < best.hp)) {
                best = unit;
            }
        }
        return best;
    }

    private static Node closestEnemy(Map<Pos, Unit> occupied, char[][] board, Unit current) {
        Deque<Node> que = new LinkedList<>();
        que.add(new Node(current.pos));
        Set<Pos> seen = new HashSet<>();
        while (!que.isEmpty()) {
            Node node = que.poll();
            if (!seen.add(node.pos)) {
                continue;
            }
            Unit unit = occupied.get(node.pos);
            if (!node.pos.equals(current.pos) && unit != null) {
                if (unit.type != current.type) {
                    return new Node(unit, node.firstMove);
                }
                continue;
            }
            if (board[node.pos.y][node.pos.x] != '#') {
                que.add(new Node(node.pos.up(), node.firstMove));
                que.add(new Node(node.pos.left(), node.firstMove));
                que.add(new Node(node.pos.right(), node.firstMove));
                que.add(new Node(node.pos.down(), node.firstMove));
            }
        }
        return null;
    }

    private static class Unit implements Comparable<Unit> {
        private final UnitType type;
        private final int attack;
        private int hp;
        private Pos pos;

        private Unit(UnitType type, int x, int y, int attack) {
            this.type = type;
            this.attack = attack;
            this.pos = new Pos(x, y);
            this.hp = 200;
        }

        @Override
        public int compareTo(Unit o) {
            return pos.compareTo(o.pos);
        }
    }

    private static class Node {
        private final Unit unit;
        private final Pos pos, firstMove;

        private Node(Pos pos) {
            this.pos = pos;
            this.firstMove = null;
            this.unit = null;
        }

        private Node(Pos pos, Pos firstMove) {
            this.pos = pos;
            this.firstMove = firstMove == null ? pos : firstMove;
            this.unit = null;
        }

        private Node(Unit unit, Pos firstMove) {
            this.pos = unit.pos;
            this.firstMove = firstMove;
            this.unit = unit;
        }
    }

    private static class Pos implements Comparable<Pos> {
        private final int x, y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private Pos up() {
            return new Pos(x, y-1);
        }

        private Pos down() {
            return new Pos(x, y+1);
        }

        private Pos left() {
            return new Pos(x-1, y);
        }

        private Pos right() {
            return new Pos(x+1, y);
        }

        private int dist(Pos pos) {
            return Math.abs(x-pos.x) + Math.abs(y-pos.y);
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
        public int compareTo(Pos o) {
            if (y != o.y) {
                return y-o.y;
            }
            return x-o.x;
        }
    }

    private enum UnitType {
        Elf,
        Goblin
    }
}
