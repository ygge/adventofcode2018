import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 {

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readStrings();

        List<Cart> carts = new ArrayList<>();
        char[][] board = new char[input.size()][];
        for (int i = 0; i < board.length; ++i) {
            board[i] = input.get(i).toCharArray();
            for (int j = 0; j < board[i].length; ++j) {
                Direction direction = Direction.fromCharacter(board[i][j]);
                if (direction != null) {
                    carts.add(new Cart(j, i, direction));
                    board[i][j] = direction == Direction.UP || direction == Direction.DOWN ? '|' : '-';
                }
            }
        }

        while (carts.size() > 1) {
            Collections.sort(carts);
            for (int i = 0; i < carts.size(); ++i) {
                Cart cart = carts.get(i);
                moveCart(board, cart);
                for (Cart otherCart : carts) {
                    if (otherCart != cart && cart.compareTo(otherCart) == 0) {
                        System.out.println("Collision: " + cart.x + "," + cart.y);
                        --i;
                        if (carts.indexOf(otherCart) <= i) {
                            --i;
                        }
                        carts.remove(cart);
                        carts.remove(otherCart);
                        break;
                    }
                }
            }
        }
        Cart lastCart = carts.get(0);
        System.out.println(lastCart.x + "," + lastCart.y);
    }

    private static void moveCart(char[][] board, Cart cart) {
        cart.x += cart.direction.dx;
        cart.y += cart.direction.dy;
        char pos = board[cart.y][cart.x];
        if (pos == '+') {
            cart.crossing();
        } else if (pos == '\\') {
            if (cart.direction == Direction.LEFT || cart.direction == Direction.RIGHT) {
                cart.direction = Move.RIGHT.changeDirection(cart.direction);
            } else {
                cart.direction = Move.LEFT.changeDirection(cart.direction);
            }
        } else if (pos == '/') {
            if (cart.direction == Direction.LEFT || cart.direction == Direction.RIGHT) {
                cart.direction = Move.LEFT.changeDirection(cart.direction);
            } else {
                cart.direction = Move.RIGHT.changeDirection(cart.direction);
            }
        }
    }

    private static class Cart implements Comparable<Cart> {
        private int x, y;
        private Direction direction;
        private Move move = Move.LEFT;

        private Cart(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        private void crossing() {
            direction = move.changeDirection(direction);
            move = move.next();
        }

        @Override
        public int compareTo(Cart o) {
            if (y != o.y) {
                return y-o.y;
            }
            return x-o.x;
        }
    }

    private enum Direction {
        UP('^', 0, -1),
        RIGHT('>', 1, 0),
        DOWN('v', 0, 1),
        LEFT('<', -1, 0);

        private final char c;
        private final int dx, dy;

        Direction(char c, int dx, int dy) {
            this.c = c;
            this.dx = dx;
            this.dy = dy;
        }

        static Direction fromCharacter(char c) {
            for (Direction value : values()) {
                if (value.c == c) {
                    return value;
                }
            }
            return null;
        }
    }

    private enum Move {
        LEFT,
        STRAIGHT,
        RIGHT;

        Direction changeDirection(Direction direction) {
            if (this == LEFT) {
                switch (direction) {
                    case UP: return Direction.LEFT;
                    case RIGHT: return Direction.UP;
                    case DOWN: return Direction.RIGHT;
                    case LEFT: return Direction.DOWN;
                }
            } else if (this == RIGHT) {
                switch (direction) {
                    case UP: return Direction.RIGHT;
                    case RIGHT: return Direction.DOWN;
                    case DOWN: return Direction.LEFT;
                    case LEFT: return Direction.UP;
                }
            }
            return direction;
        }

        Move next() {
            switch (this) {
                case LEFT: return STRAIGHT;
                case STRAIGHT: return RIGHT;
                case RIGHT: return LEFT;
            }
            throw new RuntimeException("Should never happen");
        }
    }
}
