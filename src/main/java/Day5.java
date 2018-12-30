import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day5 {

    public static void main(String[] args) throws IOException {
        List<String> rows = Util.readStrings();

        char[] chars = rows.get(0).toCharArray();

        // step 1
        int len = removeAll(chars, null);
        System.out.println(len);

        // step 2
        int smallest = Integer.MAX_VALUE;
        for (char i = 'a'; i <= 'z'; ++i) {
            smallest = Math.min(smallest, removeAll(chars, i));
        }
        System.out.println(smallest);
    }

    private static int removeAll(char[] chars, Character skip) {
        List<Character> data = new ArrayList<>();
        for (char c : chars) {
            if (skip == null || !(skip == c || skip+65-97 == c)) {
                data.add(c);
            }
        }
        removeAll(data);
        return data.size();
    }

    private static void removeAll(List<Character> data) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 1; i < data.size(); ++i) {
                if (destroyed(data.get(i-1), data.get(i))) {
                    data.remove(i-1);
                    data.remove(i-1);
                    changed = true;
                    i -= 1;
                }
            }
        }
    }

    private static boolean destroyed(char a, char b) {
        return a+97-65 == b || a == b+97-65;
    }
}
