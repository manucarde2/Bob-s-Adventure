import java.awt.*;

public abstract class MapObject
{
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    //posizione e vettori
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    //dimensioni
    protected int width;
    protected int height;

    //collisioni
    protected int cwidth;
    protected int cheight;

    //collision
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    //animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;

    //movimenti
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    //attributi movimenti
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    protected boolean bloccoDanno;
    protected boolean fineLivello;

    protected Tile bloccoItem;
    boolean havePowerUp;
    int tx;
    int ty;
    int savedPowerUp;

    //costruttore
    public MapObject(TileMap tm)
    {
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    public boolean intersects(MapObject o)
    {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    public Rectangle getRectangle()
    {
        return new Rectangle((int)x - cwidth,(int)y - cheight, cwidth, cheight);
    }

    public void calculateCorners(double x, double y)
    {
        int leftTile = (int) (x - cwidth / 2) / tileSize;
        int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
        int topTile = (int) (y - cheight / 2) / tileSize;
        int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;

        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        topLeft = (tl == Tile.BLOCKED) || (tl == Tile.ITEM) || (tl == Tile.BREAK);
        topRight = (tr == Tile.BLOCKED) || (tr == Tile.ITEM) || (tr == Tile.BREAK);
        bottomLeft = (bl == Tile.BLOCKED) || (bl == Tile.ITEM) || (bl == Tile.BREAK);
        bottomRight = (br == Tile.BLOCKED) || (br == Tile.ITEM) || (br == Tile.BREAK);

        if(tl == Tile.DANNO || tr == Tile.DANNO || bl == Tile.DANNO || br == Tile.DANNO)
            bloccoDanno = true;

        if(tl == Tile.ENDLEVEL || tr == Tile.ENDLEVEL || bl == Tile.ENDLEVEL || br == Tile.ENDLEVEL)
            fineLivello = true;

        if(tl == Tile.ITEM && tr == Tile.ITEM)
        {
            havePowerUp = true;
            tx = (leftTile*32) + 16;
            ty = (topTile*32) - 16;
            savedPowerUp = tileMap.getItem(topTile, leftTile);
        }

        if(tl == Tile.BREAK && tr == Tile.BREAK && this.getClass() == Player.class)
        {
            tileMap.removeBreakBlock(topTile, leftTile);
        }
    }

    public void checkTileMapCollision()
    {
        currCol = (int)x / tileSize;
        currRow = (int)y /tileSize;

        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);
        if(dy < 0)
        {
            if(topLeft || topRight)
            {
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            }
            else
            {
                ytemp += dy;
            }
        }
        if(dy > 0)
        {
            if(bottomLeft || bottomRight)
            {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            }
            else
            {
                ytemp += dy;
            }
        }

        calculateCorners(xdest, y);
        if(dx < 0)
        {
            if(topLeft || bottomLeft)
            {
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            }
            else
            {
                xtemp += dx;
            }
        }
        if(dx>0)
        {
            if(topRight || bottomRight)
            {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            }
            else
            {
                xtemp += dx;
            }
        }

        if(!falling)
        {
            calculateCorners(x, ydest + 1);
            if(!bottomLeft && !bottomRight)
            {
                falling = true;
            }
        }
    }

    public int getX()
    {
        return (int)x;
    }

    public int getY()
    {
        return (int)y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getCwidth()
    {
        return cwidth;
    }

    public int getCheight()
    {
        return cheight;
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition()
    {
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b)
    {
        left = b;
    }
    public void setRight(boolean b)
    {
        right = b;
    }
    public void setUp(boolean b)
    {
        up = b;
    }
    public void setDown(boolean b)
    {
        down = b;
    }
    public void setJumping(boolean b)
    {
        jumping = b;
    }

    public boolean notOnScreen()
    {
        return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
    }

    public void draw(Graphics2D g)
    {
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
