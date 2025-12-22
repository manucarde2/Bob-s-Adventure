package states;

import engine.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TutorialState extends GameState {
    private Background bg;
    private int currentChoice = 0;
    private String[] options = {
            "Press Arrow Right to Move Right",
            "Press Arrow Left to Move Left",
            "Press Z to Jump",
            "Press X to Use Ability",
            "Press Escape to pause the game"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public TutorialState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/Sfondo Bob Menu.png", 1);
            bg.setVector(-0.1, 0, 0, 0);
            titleColor = new Color(255, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 32);
            font = new Font("Arial", Font.PLAIN, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        // Parametri per il layout
        int startY = 80;
        int lineHeight = 18; // distanza verticale tra le opzioni
        int paddingX = 20;   // margine orizzontale del riquadro
        int topY = 60;       // posizione Y del bordo superiore del riquadro
        int bottomPadding = lineHeight + 15; // spazio extra tra ultima scritta e bordo inferiore
        int messageOffset = 10; // spostamento verticale delle scritte gialle

        // Calcolo altezza riquadro in base al contenuto
        int contentHeight = options.length * lineHeight + 30 + bottomPadding + messageOffset; // opzioni + messaggi gialli + margine
        int boxWidth = 320 - 2 * paddingX;
        int boxHeight = contentHeight;

        // Sfondo trasparente del riquadro
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(paddingX, topY, boxWidth, boxHeight, 20, 20);

        // Titolo
        g.setFont(titleFont);
        String title = "TUTORIAL";
        FontMetrics titleMetrics = g.getFontMetrics();
        int titleX = (320 - titleMetrics.stringWidth(title)) / 2;
        g.setColor(titleColor);
        g.drawString(title, titleX, 40);

        // Opzioni tutorial
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            int textWidth = g.getFontMetrics().stringWidth(options[i]);
            int textX = (320 - textWidth) / 2;

            // Ombra nera
            g.setColor(Color.BLACK);
            g.drawString(options[i], textX + 1, startY + i * lineHeight + 1);

            // Testo principale bianco
            g.setColor(Color.WHITE);
            g.drawString(options[i], textX, startY + i * lineHeight);
        }

        // Messaggi finali gialli
        String finalMessage = "Press Space to Start Level 1";
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int finalMessageWidth = g.getFontMetrics().stringWidth(finalMessage);
        int finalMessageX = (320 - finalMessageWidth) / 2;

        g.setColor(Color.BLACK);
        g.drawString(finalMessage, finalMessageX + 1, startY + options.length * lineHeight + 10 + messageOffset);
        g.setColor(Color.YELLOW);
        g.drawString(finalMessage, finalMessageX, startY + options.length * lineHeight + 9 + messageOffset);

        String escMessage = "Press Escape to return to Menu";
        int escMessageWidth = g.getFontMetrics().stringWidth(escMessage);
        int escMessageX = (320 - escMessageWidth) / 2;

        g.setColor(Color.BLACK);
        g.drawString(escMessage, escMessageX + 1, startY + options.length * lineHeight + 30 + messageOffset);
        g.setColor(Color.YELLOW);
        g.drawString(escMessage, escMessageX, startY + options.length * lineHeight + 29 + messageOffset);

        // Bordo giallo adattato al contenuto con padding inferiore proporzionato
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(paddingX, topY, boxWidth, boxHeight, 20, 20);
    }

    private void select() {
        gsm.setState(GameStateManager.LEVEL1STATE);
        GameStateManager.MAXREACHEDLEVEL = GameStateManager.LEVEL1STATE;
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_SPACE) {
            select();
        }
        if (k == KeyEvent.VK_ESCAPE) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {
    }
}
