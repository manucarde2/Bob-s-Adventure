import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Dodondo extends Enemy
{
    private BufferedImage[] sprites;
    private BufferedImage[] shootSprites;
    private BufferedImage[] closeSprites;
    private BufferedImage[] openSprites;
    private long startRun;
    private boolean isShooting;
    private ArrayList<Enemy> enemies;
    private long closeTimer;
    private boolean isOpen;
    private boolean isCloosing;
    private boolean isWalk;

    public Dodondo(TileMap tm, ArrayList<Enemy> enemy)
    {
        super(tm);

        enemies = enemy;
        moveSpeed = 0.1;
        maxSpeed = 0.1;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 64;
        height = 64;
        cwidth = 32;
        cheight = 61;

        health = maxHealth = 20;
        damage = 1;
        isShooting = false;

        startRun = System.nanoTime();


        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Dodondo.png"));

            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }

            shootSprites = new BufferedImage[4];
            for(int i = 0; i < shootSprites.length; i++)
            {
                shootSprites[i] = spritesheet.getSubimage(i*width,height,width,height);
            }

            openSprites = new BufferedImage[1];
            openSprites[0]=spritesheet.getSubimage(3*width,height,width,height);

            closeSprites = new BufferedImage[3];
            for(int i = 0; i < closeSprites.length; i++)
            {
                closeSprites[i] = spritesheet.getSubimage(i*width,height*2,width,height);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(100);
        startRun = System.nanoTime();
        isWalk = true;

        right = true;
        facingRight = true;
    }

    private void getNextPosition()
    {
        if(left)
        {
            dx -= moveSpeed;
            if(isOpen)
            {
                dx = 0;
            }
            else if(dx < -maxSpeed)
            {
                dx = -maxSpeed;
            }

        }
        else if(right)
        {
            dx += moveSpeed;
            if(isOpen)
            {
                dx = 0;
            }
            else if(dx > maxSpeed)
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

        if(isShooting && animation.getFrame()==3)
        {
            animation.setFrames(openSprites);
            closeTimer = System.nanoTime();
            isOpen = true;
            isShooting = false;
        }

        if (isOpen && (((System.nanoTime()-closeTimer)/1000000)>=1000))
        {
            closeAnimation();
            isCloosing = true;
            isOpen = false;
        }

        if(isCloosing && animation.getFrame()==2)
        {
            walkAnimation();
            isCloosing = false;
            startRun = System.nanoTime();
            isWalk = true;
        }

        if(right && dx == 0 && isWalk)
        {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0 && isWalk)
        {
            right = true;
            left = false;
            facingRight = true;
        }

        if(((System.nanoTime()-startRun)/1000000)>=3000 && isWalk) //1000 = 1 secondo
        {
            openAnimation();
            isShooting = true;
            isWalk = false;

            Loomby lb = new Loomby(tileMap, facingRight);
            lb.setPosition(x,y);

            enemies.add(lb);
        }

        animation.update();
    }

    public void draw(Graphics2D g)
    {
        //if(notOnScreen()) return;
        setMapPosition();
        super.draw(g);
    }

    public void walkAnimation()
    {
        animation.setFrames(sprites);
        animation.setDelay(100);
    }

    public void openAnimation()
    {
        animation.setFrames(shootSprites);
        animation.setDelay(100);
    }

    public void closeAnimation()
    {
        animation.setFrames(closeSprites);
        animation.setDelay(100);
    }
}
