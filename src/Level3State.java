import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level3State extends GameState
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

    public Level3State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure TileMap.png");
        tileMap.loadMap("/Livelli/Livello 3.map");
        tileMap.setPosition(0,0);

        mapVoid = 608;

        bg = new Background("/Backgrounds/Sfondo Bob Deserto.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(80,560);

        powerUps = new ArrayList<PowerUp>();

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        bgMusic = new AudioPlayer("/Music/Bob-nel-Deserto-1.wav");
        bgMusic.setVolume(GameStateManager.volume);
        bgMusic.playLoop();

        tileMap.addItem(13,10,PowerUp.SPEED);
        tileMap.addItem(17,61,PowerUp.FIGHT);
        tileMap.addItem(9,73,PowerUp.STAMINA);
        tileMap.addItem(5,24,PowerUp.FIRE);
    }

    private  void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Loomby l;
        Cannon c;
        Point[] pointsL = new Point[]
                {
                        new Point(343, 496),
                        new Point(589 , 464),
                        new Point(660, 464),
                        new Point(929, 464),
                        new Point(1191, 464),
                        new Point(1693, 464),
                        new Point(1481, 464),
                        new Point(1584, 464),
                        new Point(1392, 240),
                        new Point(1297, 240),
                        new Point(1205, 240),
                        new Point(851, 240),
                        new Point(741, 240),
                        new Point(371, 240),
                };

        Point[] pointsC = new Point[]
                {
                        new Point(2381, 528),
                        new Point(369, 240),
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

        Dodondo dodondo = new Dodondo(tileMap, enemies);
        dodondo.setPosition(1966,608);
        enemies.add(dodondo);

        Flarby flarby = new Flarby(tileMap, true, 128, 0.4, 0.6);
        flarby.setPosition(749, 350);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, true, 128, 0.4, 0.6);
        flarby.setPosition(1008, 350);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, true, 128, 0.4, 0.6);
        flarby.setPosition(1268, 350);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(1909, 324);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 128, 0.4, 0.6);
        flarby.setPosition(1998, 140);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 128, 0.4, 0.6);
        flarby.setPosition(1837, 140);
        enemies.add(flarby);

        flarby = new Flarby(tileMap, false, 128, 0.4, 0.6);
        flarby.setPosition(1837, 140);
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
                gsm.setState(GameStateManager.LEVEL4STATE);
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
