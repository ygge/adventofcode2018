import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();
        Parser parser = new Parser(input.get(0));

        Node root = parse(parser);

        // step 1
        System.out.println(root.totalMetaData());

        // step 2
        System.out.println(root.value());
    }

    private static Node parse(Parser parser) {
        Node node = new Node();
        int children = parser.next();
        int metaData = parser.next();
        for (int i = 0; i < children; ++i) {
            node.childNodes.add(parse(parser));
        }
        for (int i = 0; i < metaData; ++i) {
            node.metaData.add(parser.next());
        }
        return node;
    }

    private static class Node {
        private final List<Node> childNodes = new ArrayList<>();
        private final List<Integer> metaData = new ArrayList<>();

        private int totalMetaData() {
            int childTotal = childNodes.stream()
                    .map(Node::totalMetaData)
                    .reduce(0, (a, b) -> a+b);
            return childTotal + metaData.stream().reduce(0, (a, b) -> a+b);
        }

        private int value() {
            if (childNodes.isEmpty()) {
                return metaData.stream().reduce(0, (a, b) -> a+b);
            }
            int v = 0;
            for (Integer index : metaData) {
                if (index > 0 && index <= childNodes.size()) {
                    v += childNodes.get(index-1).value();
                }
            }
            return v;
        }
    }

    private static class Parser {
        private final String[] data;
        private int index;

        private Parser(String row) {
            data = row.split(" ");
            index = 0;
        }

        private int next() {
            return Integer.parseInt(data[index++]);
        }
    }
}
