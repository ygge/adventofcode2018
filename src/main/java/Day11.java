public class Day11 {

    public static void main(String[] args) {
        int serial = 7511;
        //int serial = 42;
        int[][] grid = new int[300][300];
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                int rackId = (i + 1) + 10;
                int level = (rackId * (j + 1) + serial) * rackId;
                grid[i][j] = (level/100)%10-5;
            }
        }

        // part 1
        int max = Integer.MIN_VALUE;
        int bestX = 0, bestY = 0;
        for (int i = 0; i < grid.length-2; ++i) {
            for (int j = 0; j < grid[i].length-2; ++j) {
                int v = 0;
                for (int di = 0; di < 3; ++di) {
                    for (int dj = 0; dj < 3; ++dj) {
                        v += grid[i+di][j+dj];
                    }
                }
                if (v > max) {
                    max = v;
                    bestX = i+1;
                    bestY = j+1;
                }
            }
        }
        System.out.println(bestX + "," + bestY + ": " + max);

        // part 2
        max = Integer.MIN_VALUE;
        bestX = 0;
        bestY = 0;
        int bestSide = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                for (int side = 1; side < Math.min(grid.length-i+1, grid[i].length-j+1); ++side) {
                    int v = 0;
                    for (int di = 0; di < side; ++di) {
                        for (int dj = 0; dj < side; ++dj) {
                            v += grid[i+di][j+dj];
                        }
                    }
                    if (v > max) {
                        max = v;
                        bestX = i+1;
                        bestY = j+1;
                        bestSide = side;
                    }
                }
            }
        }
        System.out.println(bestX + "," + bestY + "," + bestSide + ": " + max);
    }
}
