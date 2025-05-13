import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level6State extends GameState
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

    public Level6State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure TileMap.png");
        tileMap.loadMap("/Livelli/Livello 6.map");
        tileMap.setPosition(0,0);

        mapVoid = 608;

        bg = new Background("/Backgrounds/Sfondo Bob Caverna.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(141,464);

        powerUps = new ArrayList<PowerUp>();

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        bgMusic = new AudioPlayer("/Music/soundtrack 3.wav");
        bgMusic.setVolume(GameStateManager.volume);
        bgMusic.play();

        tileMap.addItem(12,9,PowerUp.SPEED);
        tileMap.addItem(12, 30, PowerUp.SPEED);
        tileMap.addItem(12, 32, PowerUp.STAMINA);
        tileMap.addItem(12, 98, PowerUp.SPEED);
        tileMap.addItem(12, 100, PowerUp.STAMINA);
        tileMap.addItem(12, 160, PowerUp.SPEED);
        tileMap.addItem(12, 162, PowerUp.STAMINA);
        tileMap.addItem(12, 216, PowerUp.SPEED);
        tileMap.addItem(12, 218, PowerUp.STAMINA);
        tileMap.addItem(12, 275, PowerUp.SPEED);
        tileMap.addItem(12, 277, PowerUp.STAMINA);
        tileMap.addItem(12, 334, PowerUp.SPEED);
        tileMap.addItem(12, 336, PowerUp.STAMINA);
        tileMap.addItem(12, 393, PowerUp.SPEED);
        tileMap.addItem(12, 395, PowerUp.STAMINA);
        tileMap.addItem(12, 458, PowerUp.SPEED);
        tileMap.addItem(12, 460, PowerUp.STAMINA);
    }

    private  void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Loomby l;
        Point[] pointsL = new Point[]
                {
                        // Spazio 1 (613–2307)
                        new Point(654, 464),
                        new Point(797, 464),
                        new Point(838, 464),
                        new Point(1031, 464),
                        new Point(1162, 464),
                        new Point(1216, 464),
                        new Point(1395, 464),
                        new Point(1502, 464),
                        new Point(1583, 464),
                        new Point(1710, 464),
                        new Point(1768, 464),
                        new Point(1873, 464),
                        new Point(2079, 464),
                        new Point(2087, 464),
                        new Point(2290, 464),

                        // Spazio 2 (2439–4390)
                        new Point(2491, 464),
                        new Point(2659, 464),
                        new Point(2797, 464),
                        new Point(2942, 464),
                        new Point(3078, 464),
                        new Point(3215, 464),
                        new Point(3362, 464),
                        new Point(3501, 464),
                        new Point(3648, 464),
                        new Point(3790, 464),
                        new Point(3946, 464),
                        new Point(4099, 464),
                        new Point(4228, 464),
                        new Point(4337, 464),
                        new Point(4388, 464),

                        // Spazio 3 (4520–5984)
                        new Point(4573, 464),
                        new Point(4690, 464),
                        new Point(4815, 464),
                        new Point(4930, 464),
                        new Point(5051, 464),
                        new Point(5178, 464),
                        new Point(5302, 464),
                        new Point(5421, 464),
                        new Point(5545, 464),
                        new Point(5667, 464),
                        new Point(5782, 464),
                        new Point(5899, 464),
                        new Point(5908, 464),
                        new Point(5943, 464),
                        new Point(5984, 464),

                        // Spazio 4 (6116–8225)
                        new Point(6163, 464),
                        new Point(6391, 464),
                        new Point(6620, 464),
                        new Point(6845, 464),
                        new Point(7070, 464),
                        new Point(7295, 464),
                        new Point(7520, 464),
                        new Point(7745, 464),
                        new Point(7970, 464),
                        new Point(8195, 464),
                        new Point(8200, 464),
                        new Point(7218, 464), // bilanciamento interno
                        new Point(7350, 464),
                        new Point(7125, 464),
                        new Point(8000, 464),

                        // Spazio 5 (8359–9597)
                        new Point(8402, 464),
                        new Point(8554, 464),
                        new Point(8700, 464),
                        new Point(8845, 464),
                        new Point(8990, 464),
                        new Point(9135, 464),
                        new Point(9280, 464),
                        new Point(9425, 464),
                        new Point(9570, 464),
                        new Point(9597, 464),
                        new Point(8850, 464),
                        new Point(9000, 464),
                        new Point(9150, 464),
                        new Point(9300, 464),
                        new Point(9450, 464),

                        // Spazio 6 (9732–11525)
                        new Point(9750, 464),
                        new Point(10200, 464),
                        new Point(10500, 464),
                        new Point(10800, 464),
                        new Point(11000, 464),
                        new Point(11200, 464),
                        new Point(11400, 464),
                        new Point(11525, 464),
                        new Point(9800, 464),
                        new Point(10100, 464),
                        new Point(10300, 464),
                        new Point(10600, 464),
                        new Point(10900, 464),
                        new Point(11100, 464),
                        new Point(11300, 464),

                        // Spazio 7 (11656–13479)
                        new Point(11656, 464),
                        new Point(11920, 464),
                        new Point(12200, 464),
                        new Point(12450, 464),
                        new Point(12700, 464),
                        new Point(12950, 464),
                        new Point(13200, 464),
                        new Point(13400, 464),
                        new Point(13479, 464),
                        new Point(11850, 464),
                        new Point(12100, 464),
                        new Point(12350, 464),
                        new Point(12600, 464),
                        new Point(12850, 464),
                        new Point(13100, 464),

                        // Spazio 8 (13598–14918)
                        new Point(13600, 464),
                        new Point(13800, 464),
                        new Point(14000, 464),
                        new Point(14200, 464),
                        new Point(14400, 464),
                        new Point(14600, 464),
                        new Point(14800, 464),
                        new Point(14918, 464),
                        new Point(13750, 464),
                        new Point(13950, 464),
                        new Point(14150, 464),
                        new Point(14350, 464),
                        new Point(14550, 464),
                        new Point(14750, 464),
                        new Point(14900, 464),
                };

        for(int i = 0; i < pointsL.length; i++)
        {
            l = new Loomby(tileMap);
            l.setPosition(pointsL[i].x, pointsL[i].y);
            enemies.add(l);
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
                gsm.setState(GameStateManager.WINSTATE);
        }
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
        if(k == KeyEvent.VK_UP)
        {
            player.setUp(true);
        }
        if(k == KeyEvent.VK_DOWN)
        {
            player.setDown(true);
        }
        if(k == KeyEvent.VK_W)
        {
            player.setJumping(true);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setGliding(true);
        }
        if(k == KeyEvent.VK_R)
        {
            player.setScratching();
        }
        if(k == KeyEvent.VK_F)
        {
            player.setFiring();
        }
        if(k == KeyEvent.VK_C)
        {
            player.setRunning(true);
        }
        if(k == KeyEvent.VK_SPACE)
        {
            System.out.println("x: " + player.getX() + " y: " + player.getY());
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
        if(k == KeyEvent.VK_UP)
        {
            player.setUp(false);
        }
        if(k == KeyEvent.VK_DOWN)
        {
            player.setDown(false);
        }
        if(k == KeyEvent.VK_W)
        {
            player.setJumping(false);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setGliding(false);
        }
        if(k == KeyEvent.VK_C)
        {
            player.setRunning(false);
        }
    }
}
