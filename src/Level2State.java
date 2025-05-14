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
        bgMusic.setVolume(GameStateManager.volume);
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
        if(k == KeyEvent.VK_SPACE)
        {
            player.setJumping(true);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setGliding(true);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setScratching();
        }
        if(k == KeyEvent.VK_E)
        {
            player.setFiring();
        }
        if(k == KeyEvent.VK_E)
        {
            player.setRunning(true);
        }
        if(k == KeyEvent.VK_O)
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
        if(k == KeyEvent.VK_SPACE)
        {
            player.setJumping(false);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setGliding(false);
        }
        if(k == KeyEvent.VK_E)
        {
            player.setRunning(false);
        }
    }
}
