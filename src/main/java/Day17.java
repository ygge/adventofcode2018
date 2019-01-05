import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day17 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();
        Set<Pos> clayTiles = new HashSet<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (String s : input) {
            String[] xy = s.split(", ");
            String x = xy[0].charAt(0) == 'x' ? xy[0] : xy[1];
            String y = xy[0].charAt(0) == 'y' ? xy[0] : xy[1];
            for (Integer xx : toNums(x)) {
                minX = Math.min(minX, xx);
                maxX = Math.max(maxX, xx);
                for (Integer yy : toNums(y)) {
                    minY = Math.min(minY, yy);
                    maxY = Math.max(maxY, yy);
                    clayTiles.add(new Pos(xx, yy));
                }
            }
        }
        System.out.println(minX + "," + minY + " - " + maxX + "," + maxY);
        //printBoard(clayTiles, minX, maxX, minY, maxY, new HashMap<>());

        // step 1
        Map<Pos, WaterType> waterTiles = new HashMap<>();
        int startY = Math.max(0, minY);
        watersFlowing(new Pos(500, startY), waterTiles, clayTiles, maxY);
        waterTiles.remove(new Pos(500, 0));
        System.out.println(waterTiles.size());
        printBoard(clayTiles, minX, maxX, minY, maxY, waterTiles);

        // step 2
        long left = waterTiles.values().stream()
                .filter(type -> type == WaterType.NOT_FLOWING)
                .count();
        System.out.println(left);
    }

    private static void printBoard(Set<Pos> clayTiles, int minX, int maxX, int minY, int maxY, Map<Pos, WaterType> waterTiles) {
        for (int y = Math.min(minY, 0); y <= maxY; ++y) {
            System.out.println(row(y, minX, maxX, clayTiles, waterTiles));
        }
    }

    private static String row(int y, int minX, int maxX, Set<Pos> clayTiles, Map<Pos, WaterType> waterTiles) {
        StringBuilder sb = new StringBuilder();
        for (int x = minX; x <= maxX; ++x) {
            char c = '.';
            Pos pos = new Pos(x, y);
            if (clayTiles.contains(pos)) {
                c = '#';
            }
            WaterType waterType = waterTiles.get(pos);
            if (waterType != null) {
                c = waterType.c;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static boolean watersFlowing(Pos pos, Map<Pos, WaterType> waterTiles, Set<Pos> clayTiles, int maxY) {
        if (pos.y > maxY) {
            return true;
        }
        WaterType waterType = waterTiles.get(pos);
        if (waterType != null) {
            return waterType == WaterType.FLOWING;
        }
        if (clayTiles.contains(pos)) {
            return false;
        }
        waterTiles.put(pos, WaterType.UNDETERMINED);
        if (!watersFlowing(new Pos(pos.x, pos.y+1), waterTiles, clayTiles, maxY)) {
            boolean left = watersFlowing(new Pos(pos.x-1, pos.y), waterTiles, clayTiles, maxY);
            boolean right = watersFlowing(new Pos(pos.x+1, pos.y), waterTiles, clayTiles, maxY);
            boolean flowing = left || right;
            waterTiles.put(pos, flowing ? WaterType.FLOWING : WaterType.NOT_FLOWING);
            if (flowing) {
                if (!left) {
                    setFlowing(new Pos(pos.x, pos.y), -1, waterTiles);
                } else if (!right) {
                    setFlowing(new Pos(pos.x, pos.y), 1, waterTiles);
                }
            }
            return flowing;
        }
        waterTiles.put(pos, WaterType.FLOWING);
        return true;
    }

    private static void setFlowing(Pos pos, int dx, Map<Pos, WaterType> waterTiles) {
        Pos loopPos = pos;
        while (true) {
            loopPos = new Pos(loopPos.x + dx, loopPos.y);
            WaterType waterType = waterTiles.get(loopPos);
            if (waterType != WaterType.NOT_FLOWING) {
                return;
            }
            waterTiles.put(loopPos, WaterType.FLOWING);
        }
    }

    private static List<Integer> toNums(String s) {
        String[] ss = s.substring(2).split("\\.\\.");
        int start = Integer.parseInt(ss[0]);
        if (ss.length == 1) {
            return Collections.singletonList(start);
        }
        int end = Integer.parseInt(ss[1]);
        List<Integer> i = new ArrayList<>();
        for (int j = start; j <= end; ++j) {
            i.add(j);
        }
        return i;
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

    private enum WaterType {
        FLOWING('|'),
        UNDETERMINED('X'),
        NOT_FLOWING('~');

        private final char c;

        WaterType(char c) {
            this.c = c;
        }
    }
}
