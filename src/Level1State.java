import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState
{
    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private HUD hud;

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }
    public void init()
    {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/RisorseTexture/Bob's Adventure Tile overworld.png");
        tileMap.loadMap("/Livelli/Mondo prova.map");
        tileMap.setPosition(0,0);

        bg = new Background("/Backgrounds/Livello1Sfondo.jpg", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);

        enemies = new ArrayList<Enemy>();
        Loomby l;
        l = new Loomby(tileMap);
        l.setPosition(100,100);
        enemies.add(l);

        hud = new HUD(player);
    }
    public void update()
    {
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getX(),GamePanel.WIDTH/2 - player.getY());

        bg.setPosition(tileMap.getx(), tileMap.gety());

        for(int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).update();
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
    }
}
