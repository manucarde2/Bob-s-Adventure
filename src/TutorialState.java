import java.awt.*;
import java.awt.event.KeyEvent;

public class TutorialState extends GameState {
    private Background bg;
    private int currentChoice = 0;
    private String[] options = {
            "Press Arrow Right to Move Right",
            "Press Arrow Left to Move Left",
            "Press Space to Jump",
            "Press E to Use Ability",
            "Press Space to Start Level 1"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public TutorialState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/Sfondo Bob Menu.png", 1);
            bg.setVector(-0.1, 0);
            titleColor = new Color(255, 0, 0); // Colore rosso per il titolo
            titleFont = new Font("Century Gothic", Font.BOLD, 32); // Titolo
            font = new Font("Arial", Font.PLAIN, 14); // Testo delle istruzioni
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        // Inizializzazione, se necessaria
    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        // Sfondo semitrasparente dietro il testo
        g.setColor(new Color(0, 0, 0, 150)); // Nero con alpha 150 su 255
        g.fillRoundRect(20, 60, 280, 160, 20, 20);

        // Titolo centrato
        g.setFont(titleFont);
        String title = "TUTORIAL";
        FontMetrics titleMetrics = g.getFontMetrics();
        int titleX = (320 - titleMetrics.stringWidth(title)) / 2;
        g.setColor(titleColor);
        g.drawString(title, titleX, 40);

        // Istruzioni con ombra
        g.setFont(font);
        int startY = 80;
        for (int i = 0; i < options.length; i++) {
            int textWidth = g.getFontMetrics().stringWidth(options[i]);
            int textX = (320 - textWidth) / 2;

            g.setColor(Color.BLACK);
            g.drawString(options[i], textX + 1, startY + i * 22 + 1); // Ombra

            g.setColor(Color.WHITE);
            g.drawString(options[i], textX, startY + i * 22); // Testo
        }

        // Messaggio finale
        String finalMessage = "Press Space to Start Level 1";
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int finalMessageWidth = g.getFontMetrics().stringWidth(finalMessage);
        int finalMessageX = (320 - finalMessageWidth) / 2;

        g.setColor(Color.BLACK);
        g.drawString(finalMessage, finalMessageX + 1, 211); // Ombra

        g.setColor(Color.YELLOW);
        g.drawString(finalMessage, finalMessageX, 210); // Testo

        // Cornice gialla con margini aumentati
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(20, 60, 280, 160, 20, 20);
    }


    private void select() {
        gsm.setState(GameStateManager.LEVEL1STATE);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_SPACE) {
            select();  // Passa al livello 1 premendo spazio
        }
    }

    @Override
    public void keyReleased(int k) {
    }
}
