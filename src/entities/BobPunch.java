package entities;

import engine.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BobPunch extends MapObject
{
    private boolean remove;
    private boolean hit; // nuovo stato

    private BufferedImage[] sprites;

    // movimento
    private double deceleration = 0.30;

    // fade / tempo vita
    private float alpha = 1.0f;
    private double lifeTime = 0; // in secondi
    private final double TOTAL_LIFE_TIME = 0.5; // 0.5 secondi totali
    private final double FULL_VISIBLE_TIME = TOTAL_LIFE_TIME / 3.0; // primo terzo completamente visibile

    public BobPunch(TileMap tm, boolean right)
    {
        super(tm);

        facingRight = right;

        // parte velocissimo
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
            animation.setDelay(-1); // non animata
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

    // chiamalo quando il pugno colpisce un nemico
    public void setHit()
    {
        if(hit) return;
        hit = true;
        dx = 0; // fermalo subito
        remove = true; // sparisce subito
    }

    public void update()
    {
        if(hit) return; // se ha già colpito, non fare altro

        // movimento standard engine
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

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
        /*if(dx == 0 && !hit) // per far sparire il pugno quando tocca un blocco
        {
            setHit();
        }*/

        // aggiorna il tempo di vita
        lifeTime += 1.0 / 60.0; // assumendo 60 fps

        // fade lineare dopo FULL_VISIBLE_TIME
        if(lifeTime > FULL_VISIBLE_TIME)
        {
            double fadeProgress = (lifeTime - FULL_VISIBLE_TIME) / (TOTAL_LIFE_TIME - FULL_VISIBLE_TIME);
            alpha = (float)(1.0 - fadeProgress);
            if(alpha < 0) alpha = 0;
        }

        animation.update();

        // fine vita
        if(lifeTime >= TOTAL_LIFE_TIME)
        {
            remove = true;
        }
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        super.draw(g);

        g.setComposite(old);
    }
}
