package states;

import engine.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SelectLevelState extends GameState {

    // ===== COSTANTI =====
    private static final int NUM_WORLDS = 3;
    private static final int LEVELS_PER_WORLD = 2;

    // ===== SELEZIONE =====
    private int currentChoice;   // 0=World, 1=Level, 2=Start, 3=Back
    private int currentWorld;    // 0-based
    private int currentLevel;    // 0-based

    private Background bg;

    private Font titleFont;
    private Font font;
    private Color titleColor;
    private Color menuColor;

    private String[] options = {
            "World",
            "Level",
            "Start",
            "Back"
    };

    public SelectLevelState(GameStateManager gsm) {
        this.gsm = gsm;

        try {
            bg = new Background("/Backgrounds/Sfondo Bob Menu.png", 1);
            bg.setVector(-0.1, 0, 0, 0);

            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);

            titleColor = new Color(255, 0, 0);
            menuColor = new Color(255,165,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        currentWorld = 0;
        currentLevel = 0;
        currentChoice = 0;
    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {

        // ===== BACKGROUND =====
        bg.draw(g);

        // ===== TITOLO =====
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Select Level", 70, 70);

        // ===== OPZIONI =====
        g.setFont(font);

        int maxLevelReached =
                GameStateManager.MAXREACHEDLEVEL - GameStateManager.LEVEL1STATE;
        int maxWorldReached = maxLevelReached / LEVELS_PER_WORLD;

        for (int i = 0; i < options.length; i++) {

            if (i == currentChoice) {
                g.setColor(menuColor);
            } else {
                g.setColor(Color.BLACK);
            }

            int y = 130 + i * 15;

            // ===== WORLD =====
            if (i == 0) {
                String left = (currentWorld > 0) ? "< " : "  ";
                String right = (currentWorld < maxWorldReached) ? " >" : "  ";

                g.drawString("World: " + left + (currentWorld + 1) + right, 130, y);
            }

            // ===== LEVEL =====
            else if (i == 1) {
                int absoluteLevel = currentWorld * LEVELS_PER_WORLD + currentLevel;

                String left = (absoluteLevel == 0) ? "  " : "< ";
                String right = (absoluteLevel >= maxLevelReached) ? "  " : " >";

                g.drawString("Level: " + left + (currentLevel + 1) + right, 130, y);
            }

            // ===== START / BACK =====
            else {
                g.drawString(options[i], 145, y);
            }
        }
    }

    // ===== INPUT =====
    @Override
    public void keyPressed(int k) {

        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice < 0)
                currentChoice = options.length - 1;
        }

        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice >= options.length)
                currentChoice = 0;
        }

        if (k == KeyEvent.VK_LEFT) {
            changeValue(-1);
        }

        if (k == KeyEvent.VK_RIGHT) {
            changeValue(1);
        }

        if (k == KeyEvent.VK_ENTER) {
            select();
        }

        if (k == KeyEvent.VK_ESCAPE) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    private void changeValue(int dir) {

        int maxLevelReached =
                GameStateManager.MAXREACHEDLEVEL - GameStateManager.LEVEL1STATE;
        int maxWorldReached = maxLevelReached / LEVELS_PER_WORLD;


        // ===== WORLD =====
        if (currentChoice == 0) {
            currentWorld += dir;

            if (currentWorld < 0)
                currentWorld = 0;
            if (currentWorld > maxWorldReached)
                currentWorld = maxWorldReached;

            currentLevel = 0;
        }

        // ===== LEVEL =====
        if (currentChoice == 1) {

            int absoluteLevel = currentWorld * LEVELS_PER_WORLD + currentLevel + dir;

            if (absoluteLevel < 0)
                return;

            if (absoluteLevel > maxLevelReached)
                return;

            currentLevel += dir;

            if (currentLevel < 0) {
                if (currentWorld > 0) {
                    currentWorld--;
                    currentLevel = LEVELS_PER_WORLD - 1;
                } else {
                    currentLevel = 0;
                }
            }

            if (currentLevel >= LEVELS_PER_WORLD) {
                if (currentWorld < maxWorldReached) {
                    currentWorld++;
                    currentLevel = 0;
                } else {
                    currentLevel = LEVELS_PER_WORLD - 1;
                }
            }
        }
    }

    private void select() {

        // ===== START =====
        if (currentChoice == 2) {
            int absoluteLevel =
                    currentWorld * LEVELS_PER_WORLD + currentLevel;
            int state =
                    GameStateManager.LEVEL1STATE + absoluteLevel;

            gsm.setState(state);
        }

        // ===== BACK =====
        if (currentChoice == 3) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {}
}
