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
    private boolean isGameOver = false; // 新增游戏结束状态

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
        } while (snake.contains(food));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("游戏结束", getWidth() / 2 - 80, getHeight() / 2);
        }
    }

    private void move() {
        if (isGameOver) return; // 修复：游戏结束后不再移动

        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (dir) {
            case 'U': newHead.x--; break;
            case 'D': newHead.x++; break;
            case 'L': newHead.y--; break;
            case 'R': newHead.y++; break;
        }

        // 修复：添加了碰撞检测
        if (newHead.x <= 0 || newHead.x >= ROWS || newHead.y <= 0 || newHead.y >= COLS) {
            isGameOver = true;
            return;
        }
        if (snake.contains(newHead)) {
            isGameOver = true;
            return;
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            createFood();
        } else {
            snake.removeLast();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        // 错误1：游戏结束后，定时器没有停止，还在不断触发repaint
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_UP && dir != 'D') dir = 'U';
        if (k == KeyEvent.VK_DOWN && dir != 'U') dir = 'D';
        if (k == KeyEvent.VK_LEFT && dir != 'R') dir = 'L';
        if (k == KeyEvent.VK_RIGHT && dir != 'L') dir = 'R';
        // 错误2：没有暂停和重新开始功能
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
