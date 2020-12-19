import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day25 {

    public static void main(String[] args) throws IOException {
        List<Point> input = Util.readFile(Point::new);

        List<List<Point>> constellations = new ArrayList<>();
        for (Point point : input) {
            List<List<Point>> matching = new ArrayList<>();
            for (List<Point> constellation : constellations) {
                if (matching(constellation, point)) {
                    matching.add(constellation);
                }
            }
            if (matching.isEmpty()) {
                List<Point> nc = new ArrayList<>();
                nc.add(point);
                constellations.add(nc);
            } else {
                constellations.removeAll(matching);
                List<Point> nc = new ArrayList<>();
                for (List<Point> points : matching) {
                    nc.addAll(points);
                }
                nc.add(point);
                constellations.add(nc);
            }
        }
        System.out.println(constellations.size());
    }

    private static boolean matching(List<Point> constellation, Point point) {
        for (Point p : constellation) {
            if (p.closeTo(point)) {
                return true;
            }
        }
        return false;
    }

    public static class Point {
        int[] coords = new int[4];

        public Point(String row) {
            String[] split = row.split(",");
            for (int i = 0; i < coords.length; ++i) {
                coords[i] = Integer.parseInt(split[i]);
            }
        }

        public boolean closeTo(Point point) {
            int diff = 0;
            for (int i = 0; i < coords.length; ++i) {
                diff += Math.abs(coords[i] - point.coords[i]);
            }
            return diff <= 3;
        }
    }
}
