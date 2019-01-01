import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day10 {

    public static void main(String[] args) throws IOException {
        List<Point> points = Util.readFile(Point::new);

        for (int i = 0; ; ++i) {
            printPoints(points, i);
        }
    }

    private static void printPoints(List<Point> points, int t) {
        Map<Integer, List<Integer>> dp = new HashMap<>();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Point point : points) {
            int y = point.getY(t);
            int x = point.getX(t);

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);

            List<Integer> xs = dp.computeIfAbsent(y, k -> new ArrayList<>());
            xs.add(x);
        }

        if (maxX-minX < 200 && maxY-minY < 200) {
            System.out.println();
            System.out.println("After time " + t);
            for (int y = minY; y <= maxY; ++y) {
                char[] c = new char[maxX-minX+1];
                for (int i = 0; i < c.length; ++i) {
                    c[i] = '.';
                }
                List<Integer> yy = dp.get(y);
                if (yy != null) {
                    for (Integer v : yy) {
                        c[v-minX] = '#';
                    }
                }
                System.out.println(new String(c));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Point {
        private final int x, y, dx, dy;

        private Point(String row) {
            x  = Integer.parseInt(row.substring(10, 16).trim());
            y  = Integer.parseInt(row.substring(17, 24).trim());
            dx = Integer.parseInt(row.substring(36, 38).trim());
            dy = Integer.parseInt(row.substring(39, 42).trim());
        }

        private int getX(int t) {
            return x + dx*t;
        }

        private int getY(int t) {
            return y + dy*t;
        }
    }
}
