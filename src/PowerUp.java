import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends MapObject
{
    private int type;
    private BufferedImage[] sprites;
    public boolean isCreated;
    public boolean needUpgrade;
    public boolean isUsed;

    public static final int FIRE = 1;
    public static final int FIGHT = 2;
    public static final int SPEED = 3;
    public static final int FLY = 4;
    public static final int CURE = 5;
    public static final int STAMINA = 6;

    public PowerUp(TileMap tm, int type, int x, int y)
    {
        super(tm);

        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        this.y = y;
        this.x = x;

        width = 32;
        height = 32;
        cwidth = 32;
        cheight = 32;

        this.type = type;
        needUpgrade = true;

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/RisorseTexture/Nemici/Esplosione Animazione.png"));          //qui ci va l'animazione dell'esplosione del nemico

            BufferedImage[] temp = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                temp[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }

            sprites = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                sprites[i] = temp[7 - i];
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

    private void getNextPosition()
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

        if(falling)
        {
            dy += fallSpeed;
        }
    }

    public void loadAnimation()
    {
        String texturePowerUp = "";
        int spriteNumber = 0;

        switch(type) {
            case FIRE:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/FirePowerUp.png";
                spriteNumber = 9;
                break;
            case FIGHT:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/FightPowerUp.png";
                spriteNumber = 21;
                break;
            case SPEED:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/SpeedPowerUp.png";
                spriteNumber = 8;
                break;
            case FLY:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/FlyPowerUp.png";
                spriteNumber = 8;
                break;
            case CURE:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/Cuore Animation.png";
                spriteNumber = 8;
                break;
            case STAMINA:
                texturePowerUp = "/RisorseTexture/Bob/PowerUp/Stella Animation.png";
                spriteNumber = 8;
                break;
            default:
                return;
        }

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(texturePowerUp));

            sprites = new BufferedImage[spriteNumber];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        needUpgrade = false;

        animation.setFrames(sprites);
        animation.setDelay(50);
    }

    public void update()
    {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        if(!isCreated)
        {
            if(animation.hasPlayedOnce()) isCreated = true;
        }

        if(isCreated && needUpgrade)
        {
            loadAnimation();
        }
        animation.update();
    }

    public void draw(Graphics2D g)
    {
        //if(notOnScreen()) return;
        setMapPosition();
        super.draw(g);
    }

    public int usePowerUp()
    {
        isUsed = true;
        return type;
    }
}
