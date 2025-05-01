import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Cannon extends Enemy
{
    private BufferedImage[] sprites;
    private BufferedImage[] shootSprites;
    private long startRun;
    private Player player;
    private ArrayList<Cannonball> cannonball;
    private int cannonballDamage;
    private boolean isShooting;
    private boolean isWalk;

    public Cannon(TileMap tm, int mapVoid, Player player)
    {
        super(tm, mapVoid);

        this.player = player;
        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 32;
        height = 32;
        cwidth = 32;
        cheight = 32;


        health = maxHealth = 8;
        damage = 1;
        cannonball = new ArrayList<Cannonball>();
        cannonballDamage = 2;
        isShooting = false;

        startRun = System.nanoTime();


        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Cannone.png"));

            sprites = new BufferedImage[6];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }

            shootSprites = new BufferedImage[2];
            for(int i = 0; i < shootSprites.length; i++)
            {
                shootSprites[i] = spritesheet.getSubimage(i*width,height,width,height);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(100);
        isShooting = false;


        right = true;
        facingRight = true;
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

        if(isShooting && animation.hasPlayedOnce())
        {
            isShooting = false;
        }

        if(!isShooting && !isWalk)
        {
            animation.setFrames(sprites);
            animation.setDelay(100);
            isShooting = false;
            isWalk = true;
        }

        if(right && dx == 0)
        {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0)
        {
            right = true;
            left = false;
            facingRight = true;
        }

        if(((System.nanoTime()-startRun)/1000000)>=3000) //1000 = 1 secondo
        {
            if ((facingRight && (player.x - x) <= 350) || (!facingRight && (player.x - x) >= -350))
            {
                animation.setFrames(shootSprites);
                animation.setDelay(100);
                isShooting = true;
                isWalk = false;
                shootCannonBall(facingRight);
                startRun = System.nanoTime();
            }
        }

        for(int i = 0; i < cannonball.size(); i++)
        {
            cannonball.get(i).update();
            if(cannonball.get(i).shouldRemove())
            {
                cannonball.remove(i);
                i--;
            }
        }
        checkAttack();

        animation.update();
    }

    public void draw(Graphics2D g)
    {
        //if(notOnScreen()) return;
        setMapPosition();
        super.draw(g);

        for(int i = 0; i < cannonball.size(); i++)
        {
            cannonball.get(i).draw(g);
        }
    }

    public void shootCannonBall(boolean facingRight)
    {
        Cannonball cb = new Cannonball(tileMap, facingRight);
        cb.setPosition(x, y);
        cannonball.add(cb);
    }

    public void checkAttack()
    {
        for(int j = 0; j < cannonball.size(); j++)
        {
            if(cannonball.get(j).intersects(player))
            {
                player.hit(cannonballDamage);
                cannonball.get(j).setHit();
                break;
            }
        }
    }
}
