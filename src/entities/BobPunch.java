package entities;

import engine.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BobPunch extends MapObject
{
    private boolean remove;
    private boolean hit;

    private BufferedImage[] sprites;

    // movimento
    private double deceleration = 0.30;
    private double lastDx;

    // fade / tempo vita
    private float alpha = 1.0f;
    private double lifeTime = 0;
    private final double TOTAL_LIFE_TIME = 0.5;
    private final double FULL_VISIBLE_TIME = TOTAL_LIFE_TIME / 3.0;

    public BobPunch(TileMap tm, boolean right)
    {
        super(tm);

        facingRight = right;

        moveSpeed = 6.5;
        dx = right ? moveSpeed : -moveSpeed;

        width = 32;
        height = 32;
        cwidth = 15;
        cheight = 15;

        try
        {
            BufferedImage spritesheet =
                    ImageIO.read(getClass().getResourceAsStream(
                            "/RisorseTexture/Bob/PugnoDiBob.png"));

            sprites = new BufferedImage[1];
            sprites[0] = spritesheet.getSubimage(0, 0, width, height);

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(-1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean shouldRemove()
    {
        return remove;
    }

    // chiamato quando colpisce nemici o blocchi
    public void setHit()
    {
        if(hit) return;
        hit = true;
        dx = 0;
        remove = true;
    }

    public void update()
    {
        if(hit) return;

        // salva dx prima della collisione
        lastDx = dx;

        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // se era in movimento e ora si è fermato → collisione con tile
        if(lastDx != 0 && dx == 0)
        {
            breakBlockAhead();
            setHit();
            return;
        }

        // decelerazione
        if(dx > 0)
        {
            dx -= deceleration;
            if(dx < 0) dx = 0;
        }
        else if(dx < 0)
        {
            dx += deceleration;
            if(dx > 0) dx = 0;
        }

        // tempo di vita
        lifeTime += 1.0 / 60.0;

        // fade
        if(lifeTime > FULL_VISIBLE_TIME)
        {
            double fadeProgress =
                    (lifeTime - FULL_VISIBLE_TIME) /
                            (TOTAL_LIFE_TIME - FULL_VISIBLE_TIME);

            alpha = (float)(1.0 - fadeProgress);
            if(alpha < 0) alpha = 0;
        }

        animation.update();

        if(lifeTime >= TOTAL_LIFE_TIME)
        {
            remove = true;
        }
    }

    private void breakBlockAhead()
    {
        int col = (int)(x / tileSize);
        int row = (int)(y / tileSize);

        int targetCol = facingRight ? col + 1 : col - 1;

        if(tileMap.getType(row, targetCol) == Tile.BREAK)
        {
            tileMap.removeBreakBlock(row, targetCol);
        }
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();

        Composite old = g.getComposite();
        g.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, alpha));

        super.draw(g);

        g.setComposite(old);
    }
}
