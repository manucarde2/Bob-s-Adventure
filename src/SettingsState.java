import java.awt.*;
import java.awt.event.KeyEvent;

public class SettingsState extends GameState
{
    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Music",
            "Sound Effects",
            "Screen Scale",
            "Return to menu"
    };

    private int screenScale;

    private Color titleColor;
    private Font titleFont;
    private Font font;

    // Offset verticale per abbassare le voci
    private final int verticalOffset = 30;

    public SettingsState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {
            bg = new Background("/Backgrounds/Sfondo Bob GameOver.png", 1);
            bg.setVector(-0.1, 0, 0, 0);
            titleColor = new Color(255, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);

            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        screenScale = GameStateManager.scale;
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
        // Disegna il background
        bg.draw(g);

        // Disegna il titolo
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("SETTINGS", 40, 50);  // Posizione del titolo (resta invariata)

        // Disegna le opzioni del menu, abbassate
        g.setFont(font);
        for(int i = 0; i < options.length; i++)
        {
            if(i == currentChoice)
            {
                g.setColor(Color.YELLOW);
            }
            else
            {
                g.setColor(Color.white);
            }
            g.drawString(options[i], 50, 100 + i * 30 + verticalOffset); // Abbassiamo tutto con l'offset
        }

        // Calcolare la posizione orizzontale centrata per le barre, ma spostata a destra
        int barWidth = 100;  // Larghezza della barra
        int barX = 180;  // Posizione a destra rispetto alla colonna delle opzioni

        // Posizione verticale delle barre, abbassata con offset
        int volumeY = 90 + verticalOffset;  // Posizione verticale della barra volume
        int volumeSY = 130 + verticalOffset;  // Posizione verticale della barra volume
        int scaleY = 170 + verticalOffset;  // Posizione verticale della barra screen scale

        // Disegna il volume con la barra
        g.setColor(Color.white);
        g.drawString("Music: " + GameStateManager.musicVolume, barX, volumeY - 10);  // Etichetta volume sopra la barra
        g.drawRect(barX, volumeY, barWidth, 10);  // Barra volume
        g.setColor(Color.blue);
        g.fillRect(barX, volumeY, GameStateManager.musicVolume, 10); // Riempie la barra in base al volume

        g.setColor(Color.white);
        g.drawString("Sound Effects: " + GameStateManager.effectVolume, barX, volumeSY - 10);
        g.drawRect(barX, volumeSY, barWidth, 10);  // Barra volume
        g.setColor(Color.blue);
        g.fillRect(barX, volumeSY, GameStateManager.effectVolume, 10);

        // Disegna la scala dello schermo con il valore
        g.setColor(Color.white);
        g.drawString("Screen Scale: " + screenScale, barX, scaleY - 10);  // Etichetta scala sopra la barra
        g.drawRect(barX, scaleY, barWidth, 10);  // Barra scale
        g.setColor(Color.green);
        g.fillRect(barX, scaleY, screenScale * 20, 10); // Riempie la barra in base alla scala
    }

    private void select()
    {
        if(currentChoice == 0)
        {
            // Si può regolare il volume usando la barra (sinistra/destra)
        }
        if(currentChoice == 1)
        {
            // Si può regolare il volume degli effetti usando la barra (sinistra/destra)
        }
        if(currentChoice == 2)
        {
            // Si può regolare la scala dello schermo
        }
        if(currentChoice == 3)
        {
            gsm.setState(GameStateManager.MENUSTATE);  // Torna al menu
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

        // Aggiungere la logica per modificare il volume
        if(k == KeyEvent.VK_LEFT)
        {
            if(currentChoice == 0 && GameStateManager.musicVolume > 0)  // Volume
            {
                GameStateManager.musicVolume--;
            }
            if(currentChoice == 1 && GameStateManager.effectVolume > 0)  // Screen scale
            {
                GameStateManager.effectVolume--;
            }
            if(currentChoice == 2 && screenScale > 1)  // Screen scale
            {
                screenScale--;
            }
        }

        if(k == KeyEvent.VK_RIGHT)
        {
            if(currentChoice == 0 && GameStateManager.musicVolume < 100)  // Volume
            {
                GameStateManager.musicVolume++;
            }
            if(currentChoice == 1 && GameStateManager.effectVolume < 100)  // Screen scale
            {
                GameStateManager.effectVolume++;
            }
            if(currentChoice == 2 && screenScale < 5)  // Screen scale
            {
                screenScale++;
            }
        }

        if(screenScale != GameStateManager.scale)
            gsm.resizeScale(screenScale);
    }

    @Override
    public void keyReleased(int k)
    {

    }
}
