import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class HUD
{
    private Player player;
    private BufferedImage image;
    private Font font;

    public HUD(Player p)
    {
        player = p;
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Bob/HUD.png"));
            font = new Font("Arial", Font.PLAIN, 14);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(image, 0, 10, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealt() + "/" + player.getMaxHealt(), 30, 25);
        g.drawString(player.getEnergy() / 100 + "/" + player.getMaxEnergy() / 100, 30, 45);
    }
}
