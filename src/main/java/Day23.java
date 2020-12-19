import java.io.IOException;
import java.util.*;
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
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (Nanobot bot : input) {
            minX = Math.min(minX, bot.x);
            maxX = Math.max(maxX, bot.x);
            minY = Math.min(minY, bot.y);
            maxY = Math.max(maxY, bot.y);
            minZ = Math.min(minZ, bot.z);
            maxZ = Math.max(maxZ, bot.z);
        }
        int r = Math.max(maxX-minX, Math.max(maxY-minY, maxZ-minZ))+1;
        Nanobot mid = new Nanobot((minX+maxX)/2, (minY+maxY)/2, (minZ+maxZ)/2, r);
        while (mid.r != 0) {
            System.out.println(mid.dist() + " " + mid.r);
            int nr = Math.min((mid.r*2+2)/3, mid.r-1);
            int dr = mid.r-nr;
            List<Nanobot> testBots = Arrays.asList(
                    new Nanobot(mid.x, mid.y, mid.z, nr),
                    new Nanobot(mid.x-dr, mid.y, mid.z, nr),
                    new Nanobot(mid.x+dr, mid.y, mid.z, nr),
                    new Nanobot(mid.x, mid.y-dr, mid.z, nr),
                    new Nanobot(mid.x, mid.y+dr, mid.z, nr),
                    new Nanobot(mid.x, mid.y, mid.z-dr, nr),
                    new Nanobot(mid.x, mid.y, mid.z+dr, nr)
            );
            int bestCount = 0;
            for (Nanobot testBot : testBots) {
                int count = 0;
                for (Nanobot bot : input) {
                    if (Math.abs(testBot.x-bot.x) + Math.abs(testBot.y-bot.y) + Math.abs(testBot.z-bot.z) <= testBot.r+bot.r) {
                        ++count;
                    }
                }
                if (count > bestCount) {
                    bestCount = count;
                    mid = testBot;
                }
            }
        }
        System.out.println(mid.dist());
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

        public Nanobot(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            r = 0;
        }

        public Nanobot(int x, int y, int z, int r) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
        }

        private boolean inRange(Nanobot nanobot) {
            return Math.abs(nanobot.x-x) + Math.abs(nanobot.y-y) + Math.abs(nanobot.z-z) <= r;
        }

        public int dist() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        @Override
        public String toString() {
            return "Nanobot{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", r=" + r +
                    '}';
        }
    }
}
