import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState
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

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
        pause = false;
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure TileMap.png");
        tileMap.loadMap("/Livelli/Livello 1.map");
        tileMap.setPosition(0,0);

        mapVoid = 608;

        bg = new Background("/Backgrounds/Sfondo Bob Normale.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);

        powerUps = new ArrayList<PowerUp>();

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        bgMusic = new AudioPlayer("/Music/L_avventura-di-Bob-1.wav");
        bgMusic.setVolume(GameStateManager.musicVolume);
        bgMusic.playLoop();

        tileMap.addItem(19,6,PowerUp.FIRE);
        tileMap.addItem(12,26,PowerUp.CURE);
        tileMap.addItem(14,45,PowerUp.FLY);
    }

    private  void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Loomby l;
        Cannon c;
        Point[] pointsL = new Point[]
                {
                        new Point(623, 592),
                        new Point(860, 304),
                        new Point(2038, 304),
                        new Point(2119, 304),
                        new Point(2166, 720),
                        new Point(2406, 720),
                        new Point(2193, 720),
                        new Point(2038, 720),
                };

        Point[] pointsC = new Point[]
                {
                        new Point(2253, 720),
                        new Point(2110, 720),
                };

        for(int i = 0; i < pointsL.length; i++)
        {
            l = new Loomby(tileMap);
            l.setPosition(pointsL[i].x, pointsL[i].y);
            enemies.add(l);
        }

        for(int i = 0; i < pointsC.length; i++)
        {
            c = new Cannon(tileMap, player);
            c.setPosition(pointsC[i].x, pointsC[i].y);
            enemies.add(c);
        }

        Flarby flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(847, 303);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, true, 300, 0.4, 0.6);
        flarby.setPosition(1236, 454);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(2587, 182);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(2618, 304);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(2669, 432);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(2670, 624);
        enemies.add(flarby);

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
                gsm.setState(GameStateManager.LEVEL2STATE);
        }
    }

    public void drawPauseMenu(Graphics2D g) {
        String[] options = {
                "Press Arrow Right to Move Right",
                "Press Arrow Left to Move Left",
                "Press Z to Jump",
                "Press X to Use Ability"
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
