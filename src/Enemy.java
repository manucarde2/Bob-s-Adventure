public class Enemy extends MapObject
{
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    private int mapVoid;

    protected boolean flinching;
    protected long flinchTimer;

    public Enemy(TileMap tm, int mapVoid)
    {
        super(tm);
        setMapVoid(mapVoid);
    }

    public boolean isDead()
    {
        return dead;
    }
    public int getDamage()
    {
        return damage;
    }
    public void hit(int damage)
    {
        if(dead || flinching)
        {
            return;
        }
        health -= damage;
        if(health < 0)
        {
            health = 0;
        }
        if(health == 0)
        {
            dead = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void setMapVoid(int mapVoid)
    {
        this.mapVoid = mapVoid;
    }

    public void update()
    {
        if(y >= mapVoid) health = 0;
    }
}
