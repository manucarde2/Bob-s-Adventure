import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background
{
    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double xi;
    private double yi;

    private double moveScale;

    public Background(String s, double ms)
    {
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            moveScale = ms;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        xi = x;
        yi = y;
    }

    public void setPosition(double x, double y)
    {
        this.x = (x*moveScale)%GamePanel.WIDTH;
        //this.y = (y*moveScale)%GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy, double xi, double yi)
    {
        this.dx = dx;
        this.dy = dy;
        this.xi = yi;
        this.yi = yi;
    }

    public void update()
    {
        x += dx;
        y += dy;
        if ((xi+x)<=-GamePanel.WIDTH || (xi+x)>= GamePanel.WIDTH)
        {
            x=xi;
        }
        if ((yi+y)>=-GamePanel.WIDTH || (yi+y)>= GamePanel.WIDTH)
        {
            y=yi;
        }
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(image, (int)x, (int) y, null);
        if(x<0)
        {
            g.drawImage(image,(int)x + GamePanel.WIDTH, (int)y,null);
        }
        if(x > 0)
        {
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
    }
}
