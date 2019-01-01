import java.util.ArrayList;
import java.util.List;

public class Day14 {

    public static void main(String[] args) {
        int total = 327901;

        // step 1
        runStep1(total);

        // step 2
        runStep2(total);

    }

    private static void runStep2(int total) {
        List<Integer> values = new ArrayList<>();
        while (total > 0) {
            values.add(total % 10);
            total /= 10;
        }
        Recipe root = new Recipe(3);
        Recipe elf0 = root;
        Recipe elf1 = new Recipe(7);
        elf1.next = elf0;
        elf1.prev = elf0;
        elf0.next = elf1;
        elf0.prev = elf1;
        int num = 2;
        while (true) {
            int v = elf0.value + elf1.value;
            if (v > 9) {
                add(root, new Recipe(v / 10));
                ++num;
                if (matches(root, values)) {
                    break;
                }
            }
            add(root, new Recipe(v % 10));
            ++num;
            if (matches(root, values)) {
                break;
            }
            elf0 = move(elf0);
            elf1 = move(elf1);
        }
        System.out.println(num-values.size());
    }

    private static boolean matches(Recipe root, List<Integer> values) {
        Recipe n = root.prev;
        for (Integer value : values) {
            if (value != n.value) {
                return false;
            }
            n = n.prev;
        }
        return true;
    }

    private static void runStep1(int total) {
        Recipe root = new Recipe(3);
        Recipe elf0 = root;
        Recipe elf1 = new Recipe(7);
        elf1.next = elf0;
        elf1.prev = elf0;
        elf0.next = elf1;
        elf0.prev = elf1;
        int num = 2;
        while (num < total+10) {
            int v = elf0.value + elf1.value;
            if (v > 9) {
                add(root, new Recipe(v / 10));
                ++num;
            }
            add(root, new Recipe(v % 10));
            ++num;
            elf0 = move(elf0);
            elf1 = move(elf1);
        }

        Recipe first = root.prev;
        if (num > total+10) {
            first = first.prev;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            sb.append(first.value);
            first = first.prev;
        }
        System.out.println(sb.reverse().toString());
    }

    private static Recipe move(Recipe elf) {
        int v = elf.value;
        while (v-- >= 0) {
            elf = elf.next;
        }
        return elf;
    }

    private static void add(Recipe root, Recipe newRecipe) {
        root.prev.next = newRecipe;
        newRecipe.prev = root.prev;
        root.prev = newRecipe;
        newRecipe.next = root;
    }

    private static class Recipe {
        private int value;
        private Recipe next, prev;

        private Recipe(int value) {
            this.value = value;
        }
    }
}
