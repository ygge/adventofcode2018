import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Util {

    private Util() {}

    static List<String> readStrings() throws IOException {
        return readFile(Function.identity());
    }

    static List<Integer> readInts() throws IOException {
        return readFile(Integer::parseInt);
    }

    static <T> List<T> readFile(Function<String, T> converter) throws IOException {
        String className = "";
        for (StackTraceElement stackTraceElement : new RuntimeException().getStackTrace()) {
            if (!stackTraceElement.getClassName().equals("Util")) {
                className = stackTraceElement.getClassName();
            }
        }
        String fileName = String.format("/%s.in", className.substring(3));
        BufferedReader in = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream(fileName)));
        String row;
        List<T> input = new ArrayList<>();
        while ((row = in.readLine()) != null) {
            input.add(converter.apply(row));
        }
        return input;
    }
}
