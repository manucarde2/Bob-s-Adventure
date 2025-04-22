import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion
{
    private int x;
    private int y;
    private int Xmap;
    private int Ymap;

    private int width;
    private int height;

    private Animation animation;
    private BufferedImage[] sprites;

    private boolean remove;

    public Explosion(int x, int y)
    {
        this.x = x;
        this.y = y;

        width = 30;
        height = 30;

        try
        {
            BufferedImage spritesheet = ImageIO.read(
              getClass().getResourceAsStream(
                      "/Sprites/Enemies/explosion.gif"          //qui ci va l'animazione dell'esplosione del nemico
              )
            );

            sprites = new BufferedImage[6];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);
    }

    public void update()
    {
        animation.update();

        if(animation.hasPlayedOnce())
        {
            remove = true;
        }
    }

    public boolean shouldRemove()
    {
        return remove;
    }

    public void setMapPosition(int x, int y)
    {
        Xmap = x;
        Ymap = y;
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(
                animation.getImage(),
                x + Xmap - width / 2,
                y + Ymap - height / 2,
                null
        );
    }
}
