import java.awt.*;

public class Level1State extends GameState
{
    private TileMap tileMap;

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
        public void init()
        {
            tileMap = new TileMap(30);
            tileMap.loadTiles("/Risorse/mappa");
            tileMap.loadMap("/Risorse/mappa1");
            tileMap.setPosition(0,0);
        }
        public void update()
        {

        }
        public void draw(Graphics2D g)
        {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,GamePanel.WIDTH, GamePanel.HEIGHT);
        }
        public void keyPressed(int k)
        {

        }
        public void keyReleased(int k)
        {

        }
    }
}
