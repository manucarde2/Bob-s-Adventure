import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Loomby extends Enemy
{
    private BufferedImage[] sprites;
    private BufferedImage[] squashSprites;
    public boolean isSquashing;

    public Loomby(TileMap tm)
    {
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 32;

        health = maxHealth = 2;
        damage = 1;

        loadSprite();

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    public Loomby(TileMap tm, boolean facingRight)
    {
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 32;

        health = maxHealth = 2;
        damage = 1;

        loadSprite();

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = facingRight;
        this.facingRight = facingRight;
        left = !right;
    }

    private void getNextPosition()
    {
        if(isSquashing)
        {
            return;
        }

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

        if(falling)
        {
            dy += fallSpeed;
        }
    }

    public void squash()
    {
        if(!isSquashing)
        {
            isSquashing = true;
            animation.setFrames(squashSprites);
            animation.setDelay(500);

            dx = 0;
            dy = 0;
        }
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

        if(isSquashing && animation.hasPlayedOnce())
        {
            hit(maxHealth);
        }

        if(right && dx == 0 && !isSquashing)
        {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0 && !isSquashing)
        {
            right = true;
            left = false;
            facingRight = true;
        }

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
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Loomby.png"));

            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }

            squashSprites = new BufferedImage[1];
            squashSprites[0] = spritesheet.getSubimage(2*width,height,width,height);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
