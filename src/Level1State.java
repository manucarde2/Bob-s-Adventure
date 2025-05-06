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
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure TileMap.png");
        tileMap.loadMap("/Livelli/Livello 1.map");
        tileMap.setPosition(0,0);

        mapVoid = 608;

        bg = new Background("/Backgrounds/Livello1Sfondo.jpg", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);

        powerUps = new ArrayList<PowerUp>();

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        bgMusic = new AudioPlayer("/Music/soundtrack 1.wav");
        bgMusic.setVolume(GameStateManager.volume);
        bgMusic.play();

        tileMap.setPowerUp(19,6,Player.PSPEED);
    }

    private  void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Loomby l;
        Cannon c;
        Point[] pointsL = new Point[]
                {
                        new Point(200, 100),
                        new Point(860, 200),
                        new Point(1525, 200),
                        new Point(1680, 200),
                        new Point(1800, 200)
                };

        Point[] pointsC = new Point[]
                {
                        new Point(1500, 200),
                        new Point(1700, 200),
                };

        /*for(int i = 0; i < pointsL.length; i++)
        {
            l = new Loomby(tileMap);
            l.setPosition(pointsL[i].x, pointsL[i].y);
            enemies.add(l);
        }*/

        /*for(int i = 0; i < pointsC.length; i++)
        {
            c = new Cannon(tileMap, player);
            c.setPosition(pointsC[i].x, pointsC[i].y);
            enemies.add(c);
        }*/

        /*Dodondo d;
        d = new Dodondo(tileMap,enemies);
        d.setPosition(1500,100);
        enemies.add(d);*/

        /*Flarby flarby = new Flarby(tileMap, false, 300, 0.4, 0.6);
        flarby.setPosition(100, 300);
        enemies.add(flarby);*/

        l = new Loomby(tileMap);
        l.setPosition(200, 100);
        enemies.add(l);

        PowerUp pf = new PowerUp(tileMap, PowerUp.FIRE, 100, 100);
        PowerUp pff = new PowerUp(tileMap, PowerUp.FIGHT, 132, 100);
        PowerUp ps = new PowerUp(tileMap, PowerUp.FLY, 164, 100);
        PowerUp pfff = new PowerUp(tileMap, PowerUp.SPEED, 196, 100);
        powerUps.add(pf);
        powerUps.add(pff);
        powerUps.add(ps);
        powerUps.add(pfff);
    }

    public void checkCollision()
    {

    }

    public void update()
    {
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
            System.out.println("PowerUp in creazione");
            PowerUp powerUp = new PowerUp(tileMap, player.getSavedPowerUp(), player.tx, player.ty);
            powerUps.add(powerUp);
            System.out.println("PowerUp creato");

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
