import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Day23 {

    public static void main(String[] args) throws IOException {
        List<Nanobot> input = Util.readFile(Nanobot::new);

        input.sort((a, b) -> b.r - a.r);

        Nanobot nanobot = input.get(0);

        // Step 1
        long inRange = input.stream()
                .filter(nanobot::inRange)
                .count();
        System.out.println(inRange);

        // Step 2
        Box initial = new Box(
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
                input
        );
        input.forEach(bot -> {
            initial.minX = Math.min(initial.minX, bot.x);
            initial.minY = Math.min(initial.minY, bot.y);
            initial.minZ = Math.min(initial.minZ, bot.z);
            initial.maxX = Math.max(initial.maxX, bot.x);
            initial.maxY = Math.max(initial.maxY, bot.y);
            initial.maxZ = Math.max(initial.maxZ, bot.z);
        });

        BestPos best = new BestPos();
        Set<Box> seen = new HashSet<>();
        calc(initial, best, input, seen);
        System.out.println(best.dist);
    }

    private static void calc(Box box, BestPos best, List<Nanobot> input, Set<Box> seen) {
        //System.out.println(box + " " + box.num + " " + box.dist() + " " + best.num + " " + best.dist);
        if (!seen.add(box)) {
            return;
        }
        if (box.isSingle()) {
            if (box.num > best.num || (box.num == best.num && box.dist() < best.dist)) {
                best.num = box.num;
                System.out.println(box + " " + box.num + " " + box.dist());
                best.dist = box.dist();
            }
        } else {
            box.split(input).forEach(newBox -> {
                if (newBox.num > best.num || (newBox.num == best.num && newBox.dist() < best.dist)) {
                    calc(newBox, best, input, seen);
                }
            });
        }
    }

    private static class Nanobot {
        private final int x, y, z, r;

        private Nanobot(String data) {
            String[] split = data.substring(5).split(">, r=");
            String[] coords = split[0].split(",");
            x = Integer.parseInt(coords[0]);
            y = Integer.parseInt(coords[1]);
            z = Integer.parseInt(coords[2]);
            r = Integer.parseInt(split[1]);
        }

        private boolean inRange(Nanobot nanobot) {
            return Math.abs(nanobot.x-x) + Math.abs(nanobot.y-y) + Math.abs(nanobot.z-z) <= r;
        }
    }

    private static class Box {
        private int minX, minY, minZ, maxX, maxY, maxZ, num;

        private Box(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, List<Nanobot> bots) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.num = count(bots);
        }

        private boolean isSingle() {
            return minX == maxX && minY == maxY && minZ == maxZ;
        }

        private int dist() {
            int xDist = minX <= 0 && maxX >= 0 ? 0 : Math.min(Math.abs(minX), Math.abs(maxX));
            int yDist = minY <= 0 && maxY >= 0 ? 0 : Math.min(Math.abs(minY), Math.abs(maxY));
            int zDist = minZ <= 0 && maxZ >= 0 ? 0 : Math.min(Math.abs(minZ), Math.abs(maxZ));
            return xDist + yDist + zDist;
        }

        private Stream<Box> split(List<Nanobot> bots) {
            if (isSingle()) {
                return Stream.of(this);
            }
            int divX = (minX+maxX)/2;
            int divY = (minY+maxY)/2;
            int divZ = (minZ+maxZ)/2;
            return Stream.of(
                    new Box(minX, minY, minZ, divX, divY, divZ, bots),
                    new Box(minX, divY+1, minZ, divX, maxY, divZ, bots),
                    new Box(divX+1, minY, minZ, maxX, divY, divZ, bots),
                    new Box(divX+1, divY+1, minZ, maxX, maxY, divZ, bots),
                    new Box(minX, minY, divZ+1, divX, divY, maxZ, bots),
                    new Box(minX, divY+1, divZ+1, divX, maxY, maxZ, bots),
                    new Box(divX+1, minY, divZ+1, maxX, divY, maxZ, bots),
                    new Box(divX+1, divY+1, divZ+1, maxX, maxY, maxZ, bots)
            )
                    .filter(Box::isValid)
                    .filter(box -> !box.equals(this))
                    .sorted((a, b) -> {
                        if (a.num != b.num) {
                            return b.num - a.num;
                        }
                        return a.dist() - b.dist();
                    });
        }

        private static boolean isValid(Box box) {
            return box.minX <= box.maxX && box.minY <= box.maxY && box.minZ <= box.maxZ;
        }

        private int count(List<Nanobot> input) {
            return (int)input.stream()
                    .filter(this::inRange)
                    .count();
        }

        private boolean inRange(Nanobot bot) {
            return dist(minX, maxX, bot.x)
                    + dist(minY, maxY, bot.y)
                    + dist(minZ, maxZ, bot.z)
                    <= bot.r;
        }

        private static int dist(int min, int max, int p) {
            if (p >= min && p <= max) {
                return 0;
            }
            return p < min ? min-p : p-max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Box box = (Box) o;
            return minX == box.minX &&
                    minY == box.minY &&
                    minZ == box.minZ &&
                    maxX == box.maxX &&
                    maxY == box.maxY &&
                    maxZ == box.maxZ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
        }

        @Override
        public String toString() {
            return "Box{" +
                    "minX=" + minX +
                    ", minY=" + minY +
                    ", minZ=" + minZ +
                    ", maxX=" + maxX +
                    ", maxY=" + maxY +
                    ", maxZ=" + maxZ +
                    '}';
        }
    }

    private static class BestPos {
        private int num, dist;

        private BestPos() {
            this.num = Integer.MIN_VALUE;
            this.dist = Integer.MAX_VALUE;
        }
    }
}
