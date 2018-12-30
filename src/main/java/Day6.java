import java.io.IOException;
import java.util.List;

public class Day6 {

    public static void main(String[] args) throws IOException {
        List<Coor> coords = Util.readFile(Coor::new);

        // step 1
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Coor coord : coords) {
            minX = Math.min(minX, coord.x);
            minY = Math.min(minY, coord.y);
            maxX = Math.max(maxX, coord.x);
            maxY = Math.max(maxY, coord.y);
        }
        int[] count = new int[coords.size()];
        for (int i = minX-2; i <= maxX+2; ++i) {
            for (int j = minY-2; j <= maxY+2; ++j) {
                int index = closest(coords, i, j);
                if (index != -1) {
                    ++count[index];
                }
            }
        }
        int max = 0;
        int k = 0;
        for (Coor coord : coords) {
            if (closest(coords, minX-10000, coord.y) != k
                    && closest(coords, maxX+10000, coord.y) != k
                    && closest(coords, coord.x, minY-10000) != k
                    && closest(coords, coord.x, maxY+10000) != k) {
                max = Math.max(max, count[k]);
            }
            ++k;
        }
        System.out.println(max);

        // step 2
        int total = 0;
        for (int i = minX; i <= maxX; ++i) {
            for (int j = minY; j <= maxY; ++j) {
                int len = 0;
                for (Coor coord : coords) {
                    len += Math.abs(i-coord.x)+Math.abs(j-coord.y);
                }
                if (len < 10000) {
                    ++total;
                }
            }
        }
        System.out.println(total);
    }

    private static int closest(List<Coor> coords, int x, int y) {
        int len = Integer.MAX_VALUE;
        int index = -1;
        int k = 0;
        boolean multiple = false;
        for (Coor coord : coords) {
            int dist = Math.abs(x-coord.x)+Math.abs(y-coord.y);
            if (dist < len) {
                len = dist;
                index = k;
                multiple = false;
            } else if (dist == len) {
                multiple = true;
            }
            ++k;
        }
        return multiple ? -1 : index;
    }

    private static class Coor {
        private final int x, y;

        private Coor(String row) {
            String[] split = row.split(", ");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }
    }
}
