import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day18 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();
        char[][] board = new char[input.size()][];
        for (int i = 0; i < input.size(); ++i) {
            board[i] = input.get(i).toCharArray();
        }
        char[][] board2 = new char[board.length][board[0].length];
        Board b = new Board(board, board2);

        // step 1
        for (int i = 0; i < 10; ++i) {
            run(b);
        }
        printAnswer(b.board);

        // step 2
        List<Count> counts = new ArrayList<>();
        for (int i = 0; i < 1000000000; ++i) {
            Count count = run(b);
            counts.add(count);
            if (counts.size() > 40) {
                boolean match = true;
                for (int j = 10; j < counts.size()-5; ++j) {
                    match = true;
                    for (int k = 0; k < 10; ++k) {
                        if (!counts.get(j-10+k).equals(counts.get(counts.size()-10+k))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        Count finalCount = counts.get(((999999999-j) % (i - j)) + j - 1);
                        System.out.println(finalCount);
                        System.out.println(finalCount.tree*finalCount.yard);
                        break;
                    }
                }
                if (match) {
                    break;
                }
            }
        }
    }

    private static void printAnswer(char[][] board) {
        for (char[] row : board) {
            System.out.println(row);
        }
        Count count = count(board);
        System.out.println(count.tree*count.yard);
    }

    private static Count count(char[][] board) {
        int open = 0;
        int tree = 0;
        int yard = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (c == '.') {
                    ++open;
                } else if (c == '|') {
                    ++tree;
                } else if (c == '#') {
                    ++yard;
                }
            }
        }
        return new Count(open, tree, yard);
    }

    private static Count run(Board board) {
        for (int y = 0; y < board.board.length; ++y) {
            for (int x = 0; x < board.board[y].length; ++x) {
                int tree = 0;
                int yard = 0;
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dx = -1; dx <= 1; ++dx) {
                        if ((dy == 0 && dx == 0)
                                || y+dy < 0 || y+dy >= board.board.length
                                || x+dx < 0 || x+dx >= board.board[y].length) {
                            continue;
                        }
                        char c = board.board[y+dy][x+dx];
                        if (c == '|') {
                            ++tree;
                        } else if (c == '#') {
                            ++yard;
                        }
                    }
                }
                char c;
                if (board.board[y][x] == '.') {
                    c = tree >= 3 ? '|' : '.';
                } else if (board.board[y][x] == '|') {
                    c = yard >= 3 ? '#' : '|';
                } else if (board.board[y][x] == '#') {
                    c = yard >= 1 && tree >= 1 ? '#' : '.';
                } else {
                    throw new RuntimeException(String.format("Char %c could not be handled", board.board[y][x]));
                }
                board.board2[y][x] = c;
            }
        }
        char[][] b = board.board;
        board.board = board.board2;
        board.board2 = b;
        return count(board.board);
    }

    private static class Count {
        private final int open, tree, yard;

        private Count(int open, int tree, int yard) {
            this.open = open;
            this.tree = tree;
            this.yard = yard;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Count count = (Count) o;
            return open == count.open &&
                    tree == count.tree &&
                    yard == count.yard;
        }

        @Override
        public int hashCode() {
            return Objects.hash(open, tree, yard);
        }

        @Override
        public String toString() {
            return "Count{" +
                    "open=" + open +
                    ", tree=" + tree +
                    ", yard=" + yard +
                    '}';
        }
    }

    private static class Board {
        private char[][] board, board2;

        private Board(char[][] board, char[][] board2) {
            this.board = board;
            this.board2 = board2;
        }
    }
}
