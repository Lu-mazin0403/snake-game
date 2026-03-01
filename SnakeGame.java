import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int CELL_SIZE = 25;
    private static final int INIT_SPEED = 200;

    private LinkedList<Point> snake;
    private Point food;
    private char dir = 'R';
    private Timer timer;
    private Random random;

    public SnakeGame() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        initSnake();
        createFood();
        timer = new Timer(INIT_SPEED, this);
        timer.start();
    }

    private void initSnake() {
        snake = new LinkedList<>();
        snake.add(new Point(10, 16));
        snake.add(new Point(10, 15));
        snake.add(new Point(10, 14));
    }

    private void createFood() {
        int r, c;
        do {
            r = random.nextInt(ROWS) + 1;
            c = random.nextInt(COLS) + 1;
            food = new Point(r, c);
        } while (snake.contains(food)); // 修复：食物不会生成在蛇身上
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 修复：绘制了边界墙
        g.setColor(Color.DARK_GRAY);
        for (int r = 0; r <= ROWS; r++) {
            for (int c = 0; c <= COLS; c++) {
                if (r == 0 || r == ROWS || c == 0 || c == COLS) {
                    g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.y * CELL_SIZE, p.x * CELL_SIZE, CELL_SIZE - 2, CELL_SIZE - 2);
        }
        g.setColor(Color.RED);
        g.fillOval(food.y * CELL_SIZE, food.x * CELL_SIZE, CELL_SIZE - 2, CELL_SIZE - 2);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (dir) {
            case 'U': newHead.x--; break;
            case 'D': newHead.x++; break;
            case 'L': newHead.y--; break;
            case 'R': newHead.y++; break;
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            createFood();
        } else {
            snake.removeLast();
        }
        // 错误1：没有任何碰撞检测，蛇可以穿墙和穿身
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        // 修复：添加了180度转向保护
        if (k == KeyEvent.VK_UP && dir != 'D') dir = 'U';
        if (k == KeyEvent.VK_DOWN && dir != 'U') dir = 'D';
        if (k == KeyEvent.VK_LEFT && dir != 'R') dir = 'L';
        if (k == KeyEvent.VK_RIGHT && dir != 'L') dir = 'R';
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("贪吃蛇");
            SnakeGame game = new SnakeGame();
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            game.requestFocus();
        });
    }
}
