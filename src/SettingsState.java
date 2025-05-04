import java.awt.*;
import java.awt.event.KeyEvent;

public class SettingsState extends GameState
{
    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Volume",
            "Return to menu"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;


    public SettingsState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {
            bg = new Background("/Backgrounds/Sfondo Game Over.jpg", 1);
            bg.setVector(-0.1, 0);
            titleColor = new Color(255, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);

            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init()
    {

    }

    @Override
    public void update()
    {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g)
    {
        //disegna il background
        bg.draw(g);

        //disegna il titolo
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("IMPOSTAZIONI", 40, 70);

        //disegna menu options
        g.setFont(font);
        for(int i=0; i<options.length; i++)
        {
            if(i == currentChoice)
            {
                g.setColor(Color.YELLOW);
            }
            else
            {
                g.setColor(Color.white);
            }
            g.drawString(options[i], 145, 140 + i * 15);
        }
    }

    private void select()
    {
        if(currentChoice == 0)
        {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1)
        {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_ENTER)
        {
            select();
        }
        if(k == KeyEvent.VK_UP)
        {
            currentChoice--;
            if(currentChoice == -1)
            {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if(currentChoice == options.length)
            {
                currentChoice = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k)
    {

    }
}
