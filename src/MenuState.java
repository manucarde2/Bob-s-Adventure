import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class MenuState extends GameState implements Serializable
{
    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "New game",
            "Continue",
            "Settings",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;
    private Color nomeColor;
    private Font nomeFont;
    private Font font;
    private Color menuColor;
    private AudioPlayer bgMusic;


    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {
            bg = new Background("/Backgrounds/Sfondo Bob Menu.png", 1);
            bg.setVector(-0.1, 0);
            titleColor = new Color(255, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            nomeFont = new Font("Century Gothic", Font.PLAIN, 10);
            nomeColor = Color.BLACK;
            menuColor = new Color(255,165,0);

            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        bgMusic = new AudioPlayer("/Music/Bob-l_Avventuriero-1.wav");
        bgMusic.setVolume(GameStateManager.volume);
        bgMusic.playLoop();
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
        g.drawString("Bob's Adventure", 50, 70);
        g.setColor(nomeColor);
        g.setFont(nomeFont);
        g.drawString("© Emanuele Cardellini & Andrea Ferragina 2025", 40, 230);

        //disegna menu options
        g.setFont(font);
        for(int i=0; i<options.length; i++)
        {
            if(i == currentChoice)
            {
                g.setColor(menuColor);
            }
            else
            {
                g.setColor(Color.BLACK);
            }
            g.drawString(options[i], 145, 140 + i * 15);
        }
    }

    private void select()
    {
        if(currentChoice == 0)
        {
            gsm.setState(GameStateManager.TUTORIALSTATE);
            bgMusic.close();
        }
        if(currentChoice == 1)
        {
            gsm.setState(GameStateManager.CURRENTLEVEL);
            bgMusic.close();
        }
        if(currentChoice == 2)
        {
            gsm.setState(GameStateManager.SETTINGSSTATE);
            bgMusic.close();
        }
        if(currentChoice == 3)
        {
            System.exit(0);
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

    public static void salvataggio(String nomeFile)
    {
        try
        {
            String userHome = System.getProperty("user.home");
            String appFolder;

            // Determina il percorso per ogni sistema operativo
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                appFolder = userHome + "\\AppData\\Local\\BobSalvataggio";
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                appFolder = userHome + "/.config/BobSalvataggio";
            } else {
                appFolder = userHome + "/.config/BobSalvataggio";
            }

            File appDirectory = new File(appFolder);
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }

            File file = new File(appDirectory, nomeFile + ".bin");
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream fOUT = new ObjectOutputStream(f);
            fOUT.writeInt(GameStateManager.CURRENTLEVEL);
            fOUT.writeInt(GameStateManager.volume);
            fOUT.writeInt(GameStateManager.scale);
            fOUT.flush();
            f.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void caricamento(String nomeFile)
    {
        GameStateManager a;
        try
        {
            String userHome = System.getProperty("user.home");
            String appFolder;

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                appFolder = userHome + "\\AppData\\Local\\BobSalvataggio";
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                appFolder = userHome + "/.config/BobSalvataggio";
            } else {
                appFolder = userHome + "/.config/BobSalvataggio";
            }

            File appDirectory = new File(appFolder);
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }

            File file = new File(appDirectory, nomeFile + ".bin");

            if (!file.exists()) {
                GameStateManager.CURRENTLEVEL = GameStateManager.LEVEL1STATE;
                GameStateManager.volume       = 50;
                GameStateManager.scale        = 2;

                System.out.println("File non trovato, creando file con valori predefiniti...");
                salvataggio(nomeFile);
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream fIN = new ObjectInputStream(f);
            GameStateManager.CURRENTLEVEL = fIN.readInt();
            GameStateManager.volume = fIN.readInt();
            GameStateManager.scale = fIN.readInt();
            f.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            GameStateManager.CURRENTLEVEL = GameStateManager.LEVEL1STATE;
            GameStateManager.volume = 50;
            GameStateManager.scale = 2;
        }
    }

    @Override
    public void keyReleased(int k)
    {

    }
}
