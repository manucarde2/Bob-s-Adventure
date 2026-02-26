package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;

    // >0 = scala fissa, -1 = fullscreen
    public static int SCALE = 2;

    private Thread thread;
    private boolean running;

    private static final int FPS = 60;
    private static final long TARGET_TIME = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private GameStateManager gsm;

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            addKeyListener(this);
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
        gsm = new GameStateManager(this);
        running = true;
    }

    @Override
    public void run() {
        init();

        while (running) {
            long start = System.nanoTime();

            gsm.update();
            gsm.draw(g);
            repaint();

            long elapsed = (System.nanoTime() - start) / 1_000_000;
            long wait = TARGET_TIME - elapsed;
            if (wait < 1) wait = 1;

            try {
                Thread.sleep(wait);
            } catch (InterruptedException ignored) {}
        }
    }

    // ===== RENDERING CORRETTO =====
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        int panelW = getWidth();
        int panelH = getHeight();

        int drawW, drawH;

        if (SCALE > 0) {
            drawW = WIDTH * SCALE;
            drawH = HEIGHT * SCALE;
        } else {
            double scale = Math.min(
                    panelW / (double) WIDTH,
                    panelH / (double) HEIGHT
            );
            drawW = (int) (WIDTH * scale);
            drawH = (int) (HEIGHT * scale);
        }

        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, panelW, panelH);
        g2.drawImage(image, x, y, drawW, drawH, null);
    }

    // ===== FULLSCREEN VERO =====
    public void resize() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (!(w instanceof JFrame frame)) return;

        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        frame.dispose();
        frame.setIgnoreRepaint(true);

        if (SCALE == -1) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            gd.setFullScreenWindow(null);
            frame.setUndecorated(false);
            setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
            frame.pack();
            frame.setLocationRelativeTo(null);
        }

        frame.setVisible(true);
        requestFocusInWindow();
    }

    // ===== INPUT =====
    @Override
    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
