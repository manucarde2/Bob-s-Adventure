import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Flarby extends Enemy
{
    private BufferedImage[] sprites;
    private double tempRangeFly;
    private double rangeFly;
    private boolean turn;

    public Flarby(TileMap tm, boolean facingRight, double rangeFly, double moveSpeed, double maxSpeed)
    {
        super(tm);

        this.moveSpeed = moveSpeed;
        this.maxSpeed = maxSpeed;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 32;

        health = maxHealth = 2;
        damage = 1;
        this.rangeFly = rangeFly;
        tempRangeFly = rangeFly;

        loadSprite();

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(50);

        right = facingRight;
        this.facingRight = facingRight;
        left = !right;
    }

    private void getNextPosition()
    {
        if(left)
        {
            dx -= moveSpeed;
            if(dx < -maxSpeed)
            {
                dx = -maxSpeed;
            }
        }
        else if(right)
        {
            dx += moveSpeed;
            if(dx > maxSpeed)
            {
                dx = maxSpeed;
            }
        }
        else
        {
            if(dx > 0)
            {
                dx -= stopSpeed;
                if(dx < 0)
                {
                    dx = 0;
                }
            }
            else if(dx < 0)
            {
                dx += stopSpeed;
                if(dx > 0)
                {
                    dx = 0;
                }
            }
        }
        tempRangeFly -= Math.abs(dx);
    }

    public void update()
    {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400)
            {
                flinching = false;
            }
        }

        if(!turn)
        {
            if (right && (dx == 0 || tempRangeFly <= 0))
            {
                right = false;
                left = true;
                facingRight = false;
                if (tempRangeFly <= 0)
                    turn = true;
                tempRangeFly = rangeFly;
            } else if (left && (dx == 0 || tempRangeFly <= 0))
            {
                right = true;
                left = false;
                facingRight = true;
                if (tempRangeFly <= 0)
                    turn = true;
                tempRangeFly = rangeFly;
            }
        }
        else
            turn = false;

        animation.update();
    }

    public void draw(Graphics2D g)
    {
        //if(notOnScreen()) return;
        setMapPosition();
        super.draw(g);
    }

    public void loadSprite()
    {
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Flarby.png"));

            sprites = new BufferedImage[2];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
