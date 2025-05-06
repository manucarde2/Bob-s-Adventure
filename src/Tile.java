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

}
