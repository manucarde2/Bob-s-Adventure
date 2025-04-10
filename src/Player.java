import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends MapObject
{
    private int healt;
    private int maxHealt;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    //private ArrayList<FireBal> fireBalls;

    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    private boolean gliding;

    //animazioni
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames =
            {
                    1
                    //frame per riga (quindi bho)
            };

    //azioni
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;

    public Player(TileMap tm)
    {
        super(tm);

        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 32;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        healt = maxHealt = 5;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        //fireBalls = new ArrayList<FireBalls>();

        scratchDamage = 8;
        scratchRange = 8;

        //Sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Bob/BobNormale.png"));
            for(int i=0; i<7; i++)
            {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for(int j=0; j<numFrames[i]; j++)
                {
                    if(i != 999) //999 è da sostituire con il numero in cui iniziano le texture più larghe, cosa che non non abbiamo
                    {
                        bi[j] = spritesheet.getSubimage(j*width,i*height,width,height);
                    }
                    else
                    {
                        bi[j] = spritesheet.getSubimage(j*width * 2,i*height,width,height); //2 è un esempio probabile
                    }
                }

                sprites.add(bi);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getHealt()
    {
        return healt;
    }
    public int getMaxHealt()
    {
        return maxHealt;
    }
    public int getFire()
    {
        return fire;
    }
    public int getMaxFire()
    {
        return maxFire;
    }

    public void setFiring()
    {
        firing = true;
    }
    public void setScratching()
    {
        scratching = true;
    }
    public void setGliding(boolean b)
    {
        gliding = b;
    }

    private void getNextPosition()
    {
        //movimenti
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

        //fermarsi quando attacca
        if((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling))
        {
            dx = 0;
        }

        //salto
        if(jumping && !falling)
        {
            dy = jumpStart;
            falling = true;
        }

        //caduta
        if(falling)
        {
            if(dy>0 && gliding)
            {
                dy += fallSpeed*0.1;
            }
            else
            {
                dy += fallSpeed;
            }

            if(dy > 0)
            {
                jumping = false;
            }
            if(dy < 0 && !jumping)
            {
                dy += stopJumpSpeed;
            }
            if(dy > maxFallSpeed)
            {
                dy = maxFallSpeed;
            }
        }
    }

    public void update()
    {
        //aggiorna la posizione
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //animazioni
        if(scratching)
        {
            if(currentAction != SCRATCHING)
            {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 64;
            }
        }
        else if (firing)
        {
            if(currentAction != FIREBALL)
            {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 32;
            }
        }
        else if(dy > 0)
        {
            if(gliding)
            {
                if(currentAction != GLIDING)
                {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 32;
                }
            }
            else if(currentAction != FALLING)
            {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 32;
            }
        }
        else if(dy<0)
        {
            if(currentAction != JUMPING)
            {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 32;
            }
        }
        else if(left || right)
        {
            if(currentAction != WALKING)
            {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 32;
            }
        }
        else
        {
            if(currentAction != IDLE)
            {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(40);
                width = 32;
            }
        }

        animation.update();

        //direzione
        if(currentAction != SCRATCHING && currentAction != FIREBALL)
        {
            if(right) facingRight = true;
            if(left) facingRight = false;
        }
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();

        //disegno del player
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0)
            {
                return;
            }
        }

        if(facingRight)
        {
            g.drawImage(animation.getImage(),(int) (x + xmap - width / 2),(int) (y + ymap - height / 2),null); //disegno del personaggio in base alla mappa (sperimentale) (dubito che funziona)
        }
        else
        {
            g.drawImage(animation.getImage(),(int) (x + xmap - width / 2 + width),(int) (y + ymap - height / 2),-width,height,null);
        }
    }
}
