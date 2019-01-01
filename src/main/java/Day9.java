import java.util.Arrays;

public class Day9 {

    public static void main(String[] args) {
        long[] playerScore = new long[405];
        int lastMarble = 7170000;

        Marble current = new Marble(0);
        current.prev = current;
        current.next = current;

        for (int i = 1; i <= lastMarble; ++i) {
            if (i%23 == 0) {
                playerScore[i%playerScore.length] += i;
                current = current.prev.prev.prev.prev.prev.prev.prev;
                playerScore[i%playerScore.length] += current.value;
                current.next.prev = current.prev;
                current.prev.next = current.next;
                current = current.next;
            } else {
                current = current.next;
                Marble next = new Marble(i);
                next.next = current.next;
                next.prev = current;
                next.next.prev = next;
                next.prev.next = next;
                current = next;
            }
        }
        Arrays.stream(playerScore).max()
                .ifPresent(System.out::println);
    }

    private static class Marble {
        private int value;
        private Marble next, prev;

        private Marble(int value) {
            this.value = value;
        }
    }
}
