import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level2State extends GameState
{
    private TileMap tileMap;
    private int mapVoid;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<PowerUp> powerUps;
    private HUD hud;
    private AudioPlayer bgMusic;

    public Level2State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure TileMap.png");
        tileMap.loadMap("/Livelli/Livello 2.map");
        tileMap.setPosition(0,0);

        mapVoid = 608;

        bg = new Background("/Backgrounds/Sfondo Bob Normale.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);

        powerUps = new ArrayList<PowerUp>();

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        bgMusic = new AudioPlayer("/Music/L_avventura-di-Bob-2.wav");
        bgMusic.setVolume(GameStateManager.musicVolume);
        bgMusic.playLoop();

        tileMap.addItem(8,22,PowerUp.FLY);
        tileMap.addItem(18,3,PowerUp.FIRE);
        tileMap.addItem(27,27,PowerUp.FLY);
        tileMap.addItem(27,6,PowerUp.CURE);
        tileMap.addItem(46,16,PowerUp.FLY);
        tileMap.addItem(72,7,PowerUp.FLY);
        tileMap.addItem(85,27,PowerUp.FIRE);
        tileMap.addItem(109,25,PowerUp.FLY);
        tileMap.addItem(128,12,PowerUp.FLY);
    }

    private  void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Loomby l;
        Flarby f;
        Point[] pointsL = new Point[]
                {
                        new Point(831, 336),
                        new Point(597, 336),
                        new Point(197, 688),
                        new Point(369, 720),
                        new Point(541, 720),
                        new Point(687, 720),
                        new Point(824, 2800),
                        new Point(586, 2864),
                        new Point(418, 2864),
                };

        Point[] pointsF = new Point[]
                {
                        new Point(554, 1027),
                        new Point(407, 1128),
                        new Point(190, 1328),
                        new Point(320, 1454),
                        new Point(169, 1751),
                        new Point(835, 1236),
                        new Point(521, 1399),
                        new Point(626, 1798),
                        new Point(813, 2231),
                        new Point(611, 2192),
                        new Point(340, 3177),
                        new Point(639, 3338),
                        new Point(869, 3401),
                        new Point(606, 3924),
                        new Point(849, 4081),
                        new Point(603, 4330),
                        new Point(749, 4296),
                        new Point(801, 4637),
                        new Point(111, 4612),
                        new Point(316, 2232),
                        new Point(422, 2492),
                        new Point(191, 3428),
                        new Point(421, 3566),
                        new Point(150, 3902),
                        new Point(209, 4316),
                };
        for(int i = 0; i < pointsL.length; i++)
        {
            l = new Loomby(tileMap);
            l.setPosition(pointsL[i].x, pointsL[i].y);
            enemies.add(l);
        }

        for(int i = 0; i < pointsF.length; i++)
        {
            f = new Flarby(tileMap, false, 320, 0.4, 0.6);
            f.setPosition(pointsF[i].x, pointsF[i].y);
            enemies.add(f);
        }
    }

    public void checkCollision()
    {

    }

    public void update()
    {
        if(!player.fineLivello)
            player.update();

        tileMap.setPosition(GamePanel.WIDTH/2 - player.getX(),GamePanel.WIDTH/2 - player.getY());

        bg.setPosition(tileMap.getx(), tileMap.gety());

        //attack enemies
        player.checkAttack(enemies);
        player.checkPowerUps(powerUps);

        if(player.dead)
        {
            bgMusic.close();
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        }

        //update all enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            e.update();
            if(e.bloccoDanno)
            {
                e.dead = true;
            }
            if(e.isDead())
            {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getX(), e.getY()));
            }

        }

        //update explosions
        for(int i = 0; i < explosions.size(); i++)
        {
            explosions.get(i).update();

            if(explosions.get(i).shouldRemove())
            {
                explosions.remove(i);
                i--;
            }
        }

        //update all PowerUps
        for(int i = 0; i < powerUps.size(); i++)
        {
            PowerUp p = powerUps.get(i);
            p.update();

            if(p.isUsed)
            {
                powerUps.remove(i);
                i--;
            }
        }

        if(player.havePowerUp)
        {
            player.havePowerUp = false;
            if(player.getSavedPowerUp() != Player.PNORMAL)
            {
                PowerUp powerUp = new PowerUp(tileMap, player.getSavedPowerUp(), player.tx, player.ty);
                powerUps.add(powerUp);
            }
        }

        if(player.fineLivello)
        {
            bgMusic.close();
            player.animation.update();
            if(player.winAnimation())
                gsm.setState(GameStateManager.LEVEL3STATE);
        }
    }

    public void drawPauseMenu(Graphics2D g) {
        String[] options = {
                "Press Arrow Right to Move Right",
                "Press Arrow Left to Move Left",
                "Press Space to Jump",
                "Press E to Use Ability"
        };

        Color titleColor = new Color(255, 0, 0); // Colore rosso per il titolo
        Font titleFont = new Font("Century Gothic", Font.BOLD, 32); // Titolo
        Font font = new Font("Arial", Font.PLAIN, 14); // Testo delle istruzioni

        g.setColor(new Color(0, 0, 0, 150)); // Nero con alpha 150 su 255
        g.fillRoundRect(20, 60, 280, 160, 20, 20);

        // Titolo centrato
        g.setFont(titleFont);
        String title = "PAUSE";
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

        String continueMessage = "Press Escape to continue";
        String menuMessage = "Press Cancel to turn to the Menu";

        g.setFont(new Font("Arial", Font.BOLD, 16));
        int finalMessageWidth = g.getFontMetrics().stringWidth(menuMessage);
        int finalMessageX = (320 - finalMessageWidth) / 2;

        g.setColor(Color.BLACK);
        g.drawString(menuMessage, finalMessageX + 1, 211); // Ombra

        g.setColor(Color.YELLOW);
        g.drawString(menuMessage, finalMessageX, 210); // Testo

        g.setColor(Color.BLACK);
        g.drawString(continueMessage, finalMessageX + 25 + 1, 181); // Ombra

        g.setColor(Color.YELLOW);
        g.drawString(continueMessage, finalMessageX + 25, 180); // Testo

        // Cornice gialla con margini aumentati
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(20, 60, 280, 160, 20, 20);
    }

    public void draw(Graphics2D g)
    {
        //sfondo
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        player.draw(g);

        for(int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).draw(g);
        }

        //draw explosions
        for(int i = 0; i < explosions.size(); i++)
        {
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        for(int i = 0; i < powerUps.size(); i++)
        {
            powerUps.get(i).draw(g);
        }

        hud.draw(g);

        if (pause)
        {
            drawPauseMenu(g);
        }
    }
    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_LEFT)
        {
            player.setLeft(true);
        }
        if(k == KeyEvent.VK_RIGHT)
        {
            player.setRight(true);
        }
        if(k == KeyEvent.VK_Z)
        {
            player.setUp(true);
            player.setJumping(true);
        }
        if(k == KeyEvent.VK_DOWN)
        {
            player.setDown(true);
        }
        if(k == KeyEvent.VK_X)
        {
            player.setGliding(true);
        }
        if(k == KeyEvent.VK_X)
        {
            player.setScratching();
        }
        if(k == KeyEvent.VK_X)
        {
            player.setFiring();
        }
        if(k == KeyEvent.VK_X)
        {
            player.setRunning(true);
        }
        if(k == KeyEvent.VK_SPACE)
        {
            System.out.println("x: " + player.getX() + " y: " + player.getY());
        }
        if(k == KeyEvent.VK_ESCAPE) {
            pause = !pause;
            if(pause)
            {
                bgMusic.pause();
            }

            else
                bgMusic.resume();
        }
        if(k == KeyEvent.VK_BACK_SPACE && pause)
        {
            pause = false;
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
    public void keyReleased(int k)
    {
        if(k == KeyEvent.VK_LEFT)
        {
            player.setLeft(false);
        }
        if(k == KeyEvent.VK_RIGHT)
        {
            player.setRight(false);
        }
        if(k == KeyEvent.VK_Z)
        {
            player.setUp(false);
            player.setJumping(false);
        }
        if(k == KeyEvent.VK_DOWN)
        {
            player.setDown(false);
        }
        if(k == KeyEvent.VK_X)
        {
            player.setGliding(false);
        }
        if(k == KeyEvent.VK_X)
        {
            player.setRunning(false);
        }
    }
}
