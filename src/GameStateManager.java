public class GameStateManager
{
    private GameState[] gameStates;
    private int currentState;
    public GamePanel gamePanel;

    public static final int NUMGAMESTATES = 9;
    public static final int MENUSTATE = 0;
    public static final int GAMEOVERSTATE = 1;
    public static final int WINSTATE = 2;
    public static final int SETTINGSSTATE = 3;
    public static final int LEVEL1STATE = 4;
    public static final int LEVEL2STATE = 5;
    public static final int LEVEL3STATE = 6;
    public static final int LEVEL5STATE = 8;
    public static final int LEVEL6STATE = 9;

    public static int scale;
    public static int volume;

    public GameStateManager(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
        gameStates = new GameState[NUMGAMESTATES];

        currentState = MENUSTATE;
        loadState(currentState);

        scale = GamePanel.SCALE;
        volume = 50;
    }

    private void loadState(int state)
    {
        if(state == MENUSTATE)
        {
            gameStates[state] = new MenuState(this);
        }

        if(state == SETTINGSSTATE)
        {
            gameStates[state] = new SettingsState(this);
        }

        if(state == LEVEL1STATE)
        {
            gameStates[state] = new Level1State(this);
        }

        if(state == LEVEL2STATE)
        {
            gameStates[state] = new Level2State(this);
        }

        if(state == LEVEL3STATE)
        {
            gameStates[state] = new Level3State(this);
        }

        if(state == LEVEL5STATE)
        {
            gameStates[state] = new Level5State(this);
        }

        if(state == GAMEOVERSTATE)
        {
            gameStates[state] = new GameOverState(this);
        }

        if(state == WINSTATE)
        {
            gameStates[state] = new WinState(this);
        }
    }

    private void unloadState(int state)
    {
        gameStates[state] = null;
    }

    public void setState(int state)
    {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
        //gameStates[currentState].init();
    }

    public void resizeScale(int newScale)
    {
        scale = newScale;
        GamePanel.SCALE = scale;
        gamePanel.resize();
    }

    public void update()
    {
        try
        {
            gameStates[currentState].update();
        }
        catch (Exception e)
        {

        }


    }

    public void draw(java.awt.Graphics2D g)
    {
        try
        {
            gameStates[currentState].draw(g);
        }
        catch (Exception e)
        {

        }
    }

    public void keyPressed(int k)
    {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k)
    {
        gameStates[currentState].keyReleased(k);
    }
}
