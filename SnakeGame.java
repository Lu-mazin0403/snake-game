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
    private char dir = 'R'; // 新增方向变量
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
        int r = random.nextInt(ROWS) + 1;
        int c = random.nextInt(COLS) + 1;
        food = new Point(r, c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        // 修复1：根据方向计算新头位置
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 修复2：添加了键盘控制
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_UP) dir = 'U';
        if (k == KeyEvent.VK_DOWN) dir = 'D';
        if (k == KeyEvent.VK_LEFT) dir = 'L';
        if (k == KeyEvent.VK_RIGHT) dir = 'R';
        // 错误1：缺少180度转向保护，比如向右时按左，直接掉头
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
