import java.awt.image.BufferedImage;

public class Tile
{
    private BufferedImage image;
    private int type;

    //tile types
    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;
    public static final int ITEM = 2;
    public static final int BREAK = 3;
    public static final int DANNO = 4;
    public static final int ENDLEVEL = 5;

    public int powerUpType;


    public Tile(BufferedImage image, int type)
    {
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public int getType()
    {
        return type;
    }

    public void setPowerUpType(int setPowerUp)
    {
        if(type == ITEM)
        {
            powerUpType = setPowerUp;
            System.out.println("Power up impostato");
        }
    }

    public int getPowerUpType()
    {
        if(type == ITEM)
        {
            type = BLOCKED;
            System.out.println("raccolta avvenuta");
            return powerUpType;
        }
        else
        {
            System.out.println("raccolta fallita");
            return Player.PNORMAL;
        }

    }
}
