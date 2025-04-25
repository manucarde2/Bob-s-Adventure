import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList<FireBall> fireBalls;

    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    private boolean gliding;

    //animazioni
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames =
            {
                    7,
                    4,
                    4,
                    7,
                    4,
                    4,
                    4,
                    7,
                    4,
                    4,
                    2,
                    4,
                    1,
                    4,
                    4,
                    1,
                    4,
                    4,
                    1,
                    4,
                    2,
                    2,
                    4,
                    1,
                    4,
                    4,
                    2,
                    1,
                    1
            };

    //azioni
    private static final int IDLE = 0;
    private static final int WALKING = 2;
    private static final int JUMPING = 2;
    private static final int FALLING = 2;
    private static final int GLIDING = 20;
    private static final int FIREBALL = 10;
    private static final int SCRATCHING = 25;

    private HashMap<String, AudioPlayer> sfx;

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
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 8;
        scratchRange = 8;

        //Sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Bob/Bob Animation.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for(int i=0; i<29; i++)
            {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for(int j=0; j<numFrames[i]; j++)
                {
                    bi[j] = spritesheet.getSubimage(j*width,i*height,width,height);
                }

                sprites.add(bi);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/SoundEffects/jump.wav"));
        sfx.put("scratch", new AudioPlayer("/SoundEffects/hitHurt.wav"));
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

    public void checkAttack(ArrayList<Enemy> enemies)
    {
        //loop trough enemies
        for (int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);

            //scratch attack
            if(scratching)
            {
                if(facingRight)
                {
                    if(
                            e.getX() > x &&
                                    e.getX() < x + scratchRange &&
                                    e.getY() > y - height / 2 &&
                                    e.getY() < y + height / 2
                    )
                    {
                        e.hit(scratchDamage);
                    }

                }else {
                    if(
                            e.getX() < x &&
                                    e.getX() > x - scratchRange &&
                                    e.getY() > y - height / 2 &&
                                    e.getY() < y + height / 2
                    )
                    {
                        e.hit(scratchDamage);
                    }
                }
            }

            //fireballs
            for(int j = 0; j < fireBalls.size(); j++)
            {
                if(fireBalls.get(j).intersects(e))
                {
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            //check enemy collision
            if(intersects(e))
            {
                hit(e.getDamage());
            }
        }
    }

    public void hit(int damage)
    {
        if(flinching) return;
        healt -= damage;
        if(healt < 0) healt = 0;
        if(healt == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();

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
            sfx.get("jump").play();
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

        if(currentAction == SCRATCHING)
        {
            if(animation.hasPlayedOnce())
            {
                scratching = false;
            }
        }
        if(currentAction == FIREBALL)
        {
            if(animation.hasPlayedOnce())
            {
                firing = false;
            }
        }

        //attacco palle di fuoco
        fire += 1;
        if(fire > maxFire)
        {
            fire = maxFire;
        }
        if(firing && currentAction != FIREBALL)
        {
            if(fire > fireCost)
            {
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        //update fireballs
        for(int i = 0; i < fireBalls.size(); i++)
        {
            fireBalls.get(i).update();
            if(fireBalls.get(i).shouldRemove())
            {
                fireBalls.remove(i);
                i--;
            }
        }

        //check done flinching
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000)
            {
                flinching = false;
            }
        }

        //animazioni
        if(scratching)
        {
            if(currentAction != SCRATCHING)
            {
                sfx.get("scratch").play();
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(100);
                width = 32;
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
                animation.setDelay(200);
                width = 32;
            }
        }
        else
        {
            if(currentAction != IDLE)
            {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(1000);
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

        for(int i = 0; i < fireBalls.size(); i++)
        {
            fireBalls.get(i).draw(g);
        }

        //disegno del player
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0)
            {
                return;
            }
        }

        super.draw(g);
    }
}
