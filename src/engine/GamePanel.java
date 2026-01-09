package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;

    // scala condivisa con GameStateManager
    public static int SCALE = 2; // valori >0 = scale intero, -1 = fullscreen

    private Thread thread;
    private boolean running;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private GameStateManager gsm;

    public GamePanel() {
        setFocusable(true);
        requestFocus();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
        running = true;
        gsm = new GameStateManager(this);
    }

    @Override
    public void run() {
        init();

        while (running) {
            long start = System.nanoTime();

            gsm.update();
            gsm.draw(g);
            drawToScreen();

            long elapsed = System.nanoTime() - start;
            long wait = Math.max(0, targetTime - elapsed / 1_000_000);

            try {
                Thread.sleep(wait);
            } catch (Exception ignored) {}
        }
    }

    // ridimensiona la finestra con FULLSCREEN reale
    public void resize() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (!(window instanceof JFrame frame)) return;

        if (SCALE == -1) { // FULLSCREEN
            frame.dispose();                   // serve per cambiare decorazione
            frame.setUndecorated(true);       // togli bordi e barra
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // massimizza
            frame.setVisible(true);
        } else {
            // scala intera normale
            frame.dispose();
            frame.setUndecorated(false);
            setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        revalidate();
        repaint();
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        if (g2 == null) return;

        int panelW = getWidth();
        int panelH = getHeight();

        int drawW;
        int drawH;

        if (SCALE > 0) {
            drawW = WIDTH * SCALE;
            drawH = HEIGHT * SCALE;
        } else {
            // FULLSCREEN adattivo su qualsiasi schermo
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            double scaleX = screen.width / (double) WIDTH;
            double scaleY = screen.height / (double) HEIGHT;

            // mantieni aspect ratio, prendi il più piccolo
            double scale = Math.min(scaleX, scaleY);

            drawW = (int) (WIDTH * scale);
            drawH = (int) (HEIGHT * scale);
        }

        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        // sfondo nero per bande
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, panelW, panelH);

        // disegna gioco centrato
        g2.drawImage(image, x, y, drawW, drawH, null);
        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e.getKeyCode());
    }

    @Override public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }
    @Override public void keyTyped(KeyEvent e) {}
}
