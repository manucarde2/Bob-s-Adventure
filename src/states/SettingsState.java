package states;

import engine.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SettingsState extends GameState {

    private Background bg;

    private int currentChoice = 0;
    private final String[] options = {
            "Music",
            "Sound Effects",
            "Screen Scale",
            "Return to menu"
    };

    private int screenScale;

    private final Font titleFont = new Font("Century Gothic", Font.PLAIN, 28);
    private final Font font = new Font("Arial", Font.PLAIN, 12);

    private final int verticalOffset = 30;

    public SettingsState(GameStateManager gsm) {
        this.gsm = gsm;

        try {
            bg = new Background("/Backgrounds/Sfondo Bob GameOver.png", 1);
            bg.setVector(-0.1, 0, 0, 0);
        } catch (Exception e) {
            bg = null;
        }

        screenScale = GameStateManager.scale;
    }

    @Override
    public void init()
    {

    }

    @Override
    public void update() {
        if (bg != null) bg.update();
    }

    // calcola il massimo scale per l’altezza dello schermo
    private int computeMaxScale() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return screen.height / GamePanel.HEIGHT;
    }

    @Override
    public void draw(Graphics2D g) {
        if (bg != null) bg.draw(g);

        g.setFont(titleFont);
        g.setColor(Color.RED);
        g.drawString("SETTINGS", 40, 100 - 30);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == currentChoice ? Color.YELLOW : Color.WHITE);
            g.drawString(options[i], 50, 100 + i * 30 + verticalOffset);
        }

        // barre volume
        int barX = 180;
        int musicY = 90 + verticalOffset;
        int effectsY = 130 + verticalOffset;
        int scaleY = 170 + verticalOffset;
        int barWidth = 100;
        int barHeight = 10;

        // Music
        g.setColor(new Color(80, 80, 80));
        g.fillRect(barX, musicY, barWidth, barHeight);
        g.setColor(Color.BLUE);
        g.fillRect(barX, musicY, GameStateManager.musicVolume * barWidth / 100, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(barX, musicY, barWidth, barHeight);

        // Effects
        g.setColor(new Color(80, 80, 80));
        g.fillRect(barX, effectsY, barWidth, barHeight);
        g.setColor(Color.BLUE);
        g.fillRect(barX, effectsY, GameStateManager.effectVolume * barWidth / 100, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(barX, effectsY, barWidth, barHeight);

        // Screen Scale (barra proporzionale)
        int maxScale = computeMaxScale();
        String scaleText = (screenScale == -1) ? "FULL" : String.valueOf(screenScale);

        g.setColor(Color.WHITE);
        g.drawString("Screen Scale: " + scaleText, barX, scaleY - 10);
        g.drawRect(barX, scaleY, barWidth, barHeight);

        // calcola percentuale barra verde
        double percent;
        if (screenScale == -1) {
            percent = 1.0; // FULLSCREEN → barra piena
        } else {
            percent = screenScale / (double) maxScale;
        }

        int fillWidth = (int) (barWidth * percent);
        g.setColor(Color.GREEN);
        g.fillRect(barX, scaleY, fillWidth, barHeight);
    }

    @Override
    public void keyPressed(int k) {
        int maxScale = computeMaxScale();

        if (k == KeyEvent.VK_UP)
            currentChoice = (currentChoice - 1 + options.length) % options.length;

        if (k == KeyEvent.VK_DOWN)
            currentChoice = (currentChoice + 1) % options.length;

        // modifiche volume / scale
        if (currentChoice == 0) {
            if (k == KeyEvent.VK_LEFT && GameStateManager.musicVolume > 0)
                GameStateManager.musicVolume--;
            if (k == KeyEvent.VK_RIGHT && GameStateManager.musicVolume < 100)
                GameStateManager.musicVolume++;
        } else if (currentChoice == 1) {
            if (k == KeyEvent.VK_LEFT && GameStateManager.effectVolume > 0)
                GameStateManager.effectVolume--;
            if (k == KeyEvent.VK_RIGHT && GameStateManager.effectVolume < 100)
                GameStateManager.effectVolume++;
        } else if (currentChoice == 2) {
            if (k == KeyEvent.VK_RIGHT) {
                if (screenScale == -1) screenScale = 1;
                else screenScale++;
                if (screenScale > maxScale) screenScale = -1; // FULLSCREEN
            }
            if (k == KeyEvent.VK_LEFT) {
                if (screenScale == -1) screenScale = maxScale;
                else if (screenScale > 1) screenScale--;
            }
        }

        // RETURN
        if (currentChoice == 3 && k == KeyEvent.VK_ENTER)
            gsm.setState(GameStateManager.MENUSTATE);

        // aggiorna scala effettiva
        if (screenScale != GamePanel.SCALE) {
            GamePanel.SCALE = screenScale;
            gsm.resizeScale(screenScale);
        }
    }

    @Override
    public void keyReleased(int k) {}
}
