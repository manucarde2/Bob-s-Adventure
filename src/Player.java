import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends MapObject
{
    private int healt;
    private int maxHealt;
    private int energy;
    private int maxEnergy;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;
    private long startRun;

    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;

    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    private int dashRange;
    private int dashCost;
    private int dashDamage;

    private boolean gliding;
    private boolean flyRight;
    private boolean notGliding;
    private boolean cantGliding;

    private boolean running;
    private boolean isFast;

    public static final int PNORMAL = 0;
    public static final int PFIRE = 1;
    public static final int PFIGHT = 2;
    public static final int PSPEED = 3;
    public static final int PFLY = 4;

    private static int PAbility = PNORMAL;

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
    private static int IDLE = 0;
    private static int STAND = 1;
    private static int WALKING = 2;
    private static int JUMPING = 2;
    private static int FALLING = 2;
    private static final int GLIDING = 20;
    private static final int FIREBALL = 10;
    private static final int SCRATCHING = 25;
    private static final int SCRATCHINGPLUS = 26;
    private static final int SCRATCHINGUP = 27;
    private static final int SCRATCHINGDOWN = 28;
    private static final int RUNNING = 6;
    private static final int TURN = 21;

    private static final int normIDLE = 0;
    private static final int normSTAND = 1;
    private static final int normWALKING = 2;
    private static final int normJUMPING = 2;
    private static final int normFALLING = 2;

    private static final int speedIDLE = 3;
    private static final int speedSTAND = 4;
    private static final int speedWALKING = 5;
    private static final int speedJUMPING = 5;
    private static final int speedFALLING = 5;

    private static final int fireIDLE = 7;
    private static final int fireSTAND = 8;
    private static final int fireWALKING = 9;
    private static final int fireJUMPING = 9;
    private static final int fireFALLING = 9;

    private static final int capeIDLE = 11;
    private static final int capeSTAND = 12;
    private static final int capeWALKING = 13;
    private static final int capeJUMPING = 13;
    private static final int capeFALLING = 13;

    private static final int glassIDLE = 14;
    private static final int glassSTAND = 15;
    private static final int glassWALKING = 16;
    private static final int glassJUMPING = 16;
    private static final int glassFALLING = 16;

    private static final int flyIDLE = 17;
    private static final int flySTAND = 18;
    private static final int flyWALKING = 19;
    private static final int flyJUMPING = 19;
    private static final int flyFALLING = 19;

    private static final int fightIDLE = 22;
    private static final int fightSTAND = 23;
    private static final int fightWALKING = 24;
    private static final int fightJUMPING = 24;
    private static final int fightFALLING = 24;


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
        energy = maxEnergy = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 8;
        scratchRange = 48;

        dashRange = 16;
        dashCost = 5;
        dashDamage = 5;

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
    public int getEnergy()
    {
        return energy;
    }
    public int getMaxEnergy()
    {
        return maxEnergy;
    }

    public void setFiring()
    {
        if(PAbility == PFIRE)
            firing = true;
        else firing = false;
    }
    public void setScratching()
    {
        if(PAbility == PFIGHT)
            scratching = true;
        else if(scratching)
            scratching = false;
    }
    public void setGliding(boolean b)
    {
        if(PAbility == PFLY)
        gliding = b;
        else if (gliding)
            gliding = false;
    }
    public void setRunning(boolean b)
    {
        if(PAbility == PSPEED)
        {
            if(b && !running)
            {
                startRun = System.nanoTime();
            }
            running = b;
        }
        else if(running)
            running = false;
    }

    public void resetAbility()
    {
        scratching = false;
        firing = false;
        running = false;
        gliding = false;
    }

    public void changeAbility(int PowerUp)
    {
        switch(PowerUp)
        {
            case PFIRE:
                PAbility = PFIRE;
                IDLE = fireIDLE;
                STAND = fireSTAND;
                WALKING = fireWALKING;
                JUMPING = fireJUMPING;
                FALLING = fireFALLING;
                break;
            case PFIGHT:
                PAbility = PFIGHT;
                IDLE = fightIDLE;
                STAND = fightSTAND;
                WALKING = fightWALKING;
                JUMPING = fightJUMPING;
                FALLING = fightFALLING;
                break;
            case PSPEED:
                PAbility = PSPEED;
                IDLE = speedIDLE;
                STAND = speedSTAND;
                WALKING = speedWALKING;
                JUMPING = speedJUMPING;
                FALLING = speedFALLING;
                break;
            case PFLY:
                PAbility = PFLY;
                IDLE = glassIDLE;
                STAND = glassSTAND;
                WALKING = glassWALKING;
                JUMPING = glassJUMPING;
                FALLING = glassFALLING;
                break;
            default:
                PAbility = PNORMAL;
                IDLE = normIDLE;
                STAND = normSTAND;
                WALKING = normWALKING;
                JUMPING = normJUMPING;
                FALLING = normFALLING;
        }
        resetAbility();
    }

    public void checkPowerUps(ArrayList<PowerUp>powerUps)
    {
        //loop trough powerUps
        for (int i = 0; i < powerUps.size(); i++)
        {
            PowerUp p = powerUps.get(i);

            //check enemy collision
            if(intersects(p))
            {
                changeAbility(p.usePowerUp());
            }
        }
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

            if(running)
            {
                if(facingRight)
                {
                    if(
                            e.getX() > x &&
                                    e.getX() < x + dashRange &&
                                    e.getY() > y - height / 2 &&
                                    e.getY() < y + height / 2
                    )
                    {
                        e.hit(dashDamage);
                    }

                }else {
                    if(
                            e.getX() < x &&
                                    e.getX() > x - dashRange &&
                                    e.getY() > y - height / 2 &&
                                    e.getY() < y + height / 2
                    )
                    {
                        e.hit(dashDamage);
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
                if(e instanceof Loomby && dy>0 && y != e.y)
                {
                    if (!((Loomby)e).isSquashing)
                    dy = jumpStart * 1;
                    ((Loomby)e).squash();
                }
                else
                {
                        hit(e.getDamage());
                }
            }
        }
    }

    public void hit(int damage)
    {
        if(!running)
        {
            if(flinching) return;
            healt -= damage;
            if(healt < 0) healt = 0;
            if(healt == 0) dead = true;
            flinching = true;
            flinchTimer = System.nanoTime();
        }
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

        if(running && !falling && ((energy-dashCost)>=0))
        {
            moveSpeed = 1.6;
            maxSpeed = 6;
            isFast = true;
        }
        else
        {
            if(isFast && falling)
            {
                moveSpeed = 0.3;
                maxSpeed = 6;
            }
            else
            {
                moveSpeed = 0.3;
                maxSpeed = 1.6;
                isFast = false;
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

        if(!running)
        {
            energy += 1;
            if(energy > maxEnergy)
            {
                energy = maxEnergy;
            }
        }
        if(firing && currentAction != FIREBALL)
        {
            if(energy > fireCost)
            {
                energy -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }
        if(running)
        {
            if((energy-dashCost)>=0)
            {
                if((System.nanoTime()-startRun)>=10000000)
                {
                    startRun = System.nanoTime();
                    energy -= dashCost;
                }
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

        //prevenzione per il cambiamento dell'animazione gliding
        if(gliding && dy<=0 && PAbility == PFLY && !cantGliding)
        {
            cantGliding = true;
            IDLE = flyIDLE;
            STAND = flySTAND;
            WALKING = flyWALKING;
            JUMPING = flyJUMPING;
            FALLING = flyFALLING;
        }
        else if(cantGliding && dy>0)
        {
            IDLE = glassIDLE;
            STAND = glassSTAND;
            WALKING = glassWALKING;
            JUMPING = glassJUMPING;
            FALLING = glassFALLING;
            cantGliding = false;
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
                if(currentAction == TURN)
                {
                    if(animation.getFrame()>=1) // si riferisce all'ultimo sprite dell'animazione TURN
                    {
                        currentAction = GLIDING;
                        animation.setFrames(sprites.get(GLIDING));
                        animation.setDelay(100);
                        width = 32;
                        flyRight = right;
                    }
                }
                else if(currentAction != GLIDING)
                {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 32;
                    flyRight = right;
                }
                if((currentAction == GLIDING) && (flyRight != facingRight) && (left || right))
                {
                    currentAction = TURN;
                    animation.setFrames(sprites.get(TURN));
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
        else if((left || right) && running && isFast)
        {
            if(currentAction != RUNNING)
            {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(100);
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
            if(currentAction != STAND)
            {
                currentAction = STAND;
                animation.setFrames(sprites.get(STAND));
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
