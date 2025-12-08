package engine;


import java.awt.event.KeyEvent;
import entities.Player;

public class PlayerControls {

    private Player player;
    private GameStateManager gsm;
    private boolean pause;
    private AudioPlayer bgMusic;

    public PlayerControls(Player player, GameStateManager gsm, AudioPlayer bgMusic) {
        this.player = player;
        this.gsm = gsm;
        this.bgMusic = bgMusic;
        this.pause = false;
    }

    public boolean isPaused() {
        return pause;
    }

    public void keyPressed(int k) {
        switch (k) {
            case KeyEvent.VK_LEFT -> player.setLeft(true);
            case KeyEvent.VK_RIGHT -> player.setRight(true);
            case KeyEvent.VK_Z -> {
                player.setUp(true);
                player.setJumping(true);
            }
            case KeyEvent.VK_DOWN -> player.setDown(true);
            case KeyEvent.VK_X -> {
                player.setGliding(true);
                player.setScratching();
                player.setFiring();
                player.setRunning(true);
            }
            case KeyEvent.VK_ESCAPE -> {
                pause = !pause;
                if (pause) bgMusic.pause();
                else bgMusic.resume();
            }
            case KeyEvent.VK_BACK_SPACE -> {
                if (pause) {
                    pause = false;
                    gsm.setState(GameStateManager.MENUSTATE);
                }
            }
        }
    }

    public void keyReleased(int k) {
        switch (k) {
            case KeyEvent.VK_LEFT -> player.setLeft(false);
            case KeyEvent.VK_RIGHT -> player.setRight(false);
            case KeyEvent.VK_Z -> {
                player.setUp(false);
                player.setJumping(false);
            }
            case KeyEvent.VK_DOWN -> player.setDown(false);
            case KeyEvent.VK_X -> {
                player.setGliding(false);
                player.setRunning(false);
            }
        }
    }
}
