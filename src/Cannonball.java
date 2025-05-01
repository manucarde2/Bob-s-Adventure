import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Cannonball extends MapObject
{
    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;

    public Cannonball(TileMap tm, boolean right)
    {
        super(tm);

        facingRight = right;

        moveSpeed = 3.8;
        if(right)
        {
            dx = moveSpeed;
        }
        else
        {
            dx = -moveSpeed;
        }

        width = 12;
        height = 12;
        cwidth = 12;
        cheight = 12;

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Palla di cannone.png"));

            sprites = new BufferedImage[1];
            for (int i = 0; i<sprites.length;i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setHit()
    {
        if(hit) return;;
        hit = true;
        dx = 0;
    }

    public boolean shouldRemove()
    {
        return remove;
    }

    public void update()
    {
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        if(dx == 0 && !hit)
        {
            setHit();
        }

        animation.update();
        if(hit && animation.hasPlayedOnce())
        {
            remove = true;
        }
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();

        super.draw(g);
    }
}
