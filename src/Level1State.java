import java.awt.*;

public class Level1State extends GameState
{
    private TileMap tileMap;
    private Background bg;
    private Player player;

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
    }
    public void update()
    {

    }
    public void draw(Graphics2D g)
    {
        //sfondo
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);
    }
    public void keyPressed(int k)
    {

    }
    public void keyReleased(int k)
    {

    }
}
