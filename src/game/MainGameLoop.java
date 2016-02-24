/*
 * MainGameLoop.java
 *
 */
package game;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.event.*;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static game.SoundFileNames.soundFileNames;

/**
 *
 * @author Max Power
 */
public class MainGameLoop extends JFrame
{

    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 600;
    private DrawingPanel drawingPanel;
    private ScorePanel scorePanel;
    private InfoPanel infoPanel;
    private JMenuBar aMenuBar;
    private JMenu gameMenu;
    private JMenu restartMenu;
    private JMenu soundMenu;
    private JMenuItem restart;
    private JMenuItem howToPlay;
    private JMenuItem about;
    private JMenuItem musicToggle;
    private JMenuItem soundEffectsToggle;
    private boolean gameStart = false;
    private boolean gameOver = false;
    private boolean paused = false;
    private boolean firstPlay = false;
    private final int startingGameSpeed = 10;
    private int currentGameSpeed = startingGameSpeed;
    private Thread musicThread;
    private MusicPlayer musicPlayer;

    protected GunPlatform playerGunPlatform;
    protected GunPlatformLaser gunPlatformLaser;
    protected Duck enemyDuck;

    /**
     * Creates a new instance of M257DrawingApplication
     *
     * @param title
     */
    public MainGameLoop(String title)
    {
        super(title);
        this.setupGame();
    }

    private void setupGame()
    {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.setupMenuBar();

        this.setupSound();

        this.setupGameObjects();

        this.setupJFrame();

        this.setupEventHandlers();

    }

    private void setupGameObjects()
    {
        //Setting up the gun platform
        playerGunPlatform = new GunPlatform(250, 490);

        //Setting up the duck
        enemyDuck = new Duck(playerGunPlatform);

        //Setting up DuckLaser objects to be fired by duck        
        gunPlatformLaser = new GunPlatformLaser(playerGunPlatform, enemyDuck);
    }

    private void setupSound()
    {
        musicPlayer = new MusicPlayer();
        musicThread = new Thread(musicPlayer);
        musicThread.start();
    }

    private void setupJFrame()
    {
        //set visible here, now size is available
        setVisible(true);
        drawingPanel = new DrawingPanel(getAvailableWidth(), getAvailableHeight());
        drawingPanel.setBackground(Color.black);
        this.add(drawingPanel, BorderLayout.CENTER);

        //Setup info panel at top
        infoPanel = new InfoPanel("HIT ENTER TO START THE GAME");
        infoPanel.setBackground(Color.yellow);
        this.add(infoPanel, BorderLayout.NORTH);

        //Setup score panel at bottom
        scorePanel = new ScorePanel(500, 50);
        scorePanel.setBackground(Color.yellow);
        this.add(scorePanel, BorderLayout.SOUTH);

        //given exiting on close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupEventHandlers()
    {
        //Event handlers for key presses/mouse movements/menu options
        this.addKeyListener(new KeyWatcher());
        this.addMouseListener(new MouseWatcher());
        this.addMouseMotionListener(new MouseMotionWatcher());
        restart.addActionListener(new MenuWatcher());
        about.addActionListener(new MenuWatcher());
        howToPlay.addActionListener(new MenuWatcher());
        musicToggle.addActionListener(new MenuWatcher());
        soundEffectsToggle.addActionListener(new MenuWatcher());
    }

    //Setup menu bar and menu items
    private void setupMenuBar()
    {
        aMenuBar = new JMenuBar();
        gameMenu = new JMenu("Game Info");
        restartMenu = new JMenu("Restart Game");
        soundMenu = new JMenu("Sound");
        restart = new JMenuItem("Restart Game");
        about = new JMenuItem("About");
        howToPlay = new JMenuItem("How To Play");
        musicToggle = new JMenuItem("Music: On");
        soundEffectsToggle = new JMenuItem("Sound Effects: On");
        gameMenu.add(howToPlay);
        gameMenu.add(about);
        restartMenu.add(restart);
        soundMenu.add(musicToggle);
        soundMenu.add(soundEffectsToggle);
        aMenuBar.add(gameMenu);
        aMenuBar.add(restartMenu);
        aMenuBar.add(soundMenu);
        this.setJMenuBar(aMenuBar);
    }

//These methods should only be called after the frame is visible.
//They tell you about the available width and height in the frame
    private int getAvailableWidth()
    {
        return getWidth() - getInsets().left - getInsets().right;
    }

    private int getAvailableHeight()
    {
        return getHeight() - getInsets().top - getInsets().bottom;
    }

    /*
     * Constant loop which calls the update methods and controls if the game
     * has been won or not, game speed is also adjusted from here, based on the
     * ducks health. These while loops also evaluate if the game is over and if
     * restart game has been clicked in the menu and act accordingly, the game
     * can be restarted at any time.
     */
    public void update()
    {
        while (true)
        {
            threadSleep(100);

            if (!firstPlay)
            {
                stateNotStarted();
            }

            if (paused && gameStart && !gameOver)
            {
                statePaused();
            }

            if (gameStart && !gameOver && !paused)
            {
                statePlaying();
            }

            if (gameOver)
            {
                stateGameOver();
            }
        }
    }

    protected void resetGame()
    {
        gameOver = true;
        gameStart = false;
        enemyDuck.resetDuck();
        gunPlatformLaser.resetGunPlatformLaser();
        playerGunPlatform.resetGunPlatform();
        currentGameSpeed = startingGameSpeed;
    }

    private void stateGameOver()
    {
        System.out.println("stateGameOver()");
        while (gameOver)
        {
            infoPanel.enterToStart.setText("PRESS 'ENTER' TO PLAY AGAIN");
            threadSleep(100);
        }
    }

    private void statePaused()
    {
        System.out.println("statePaused()");

        while (paused)
        {
            infoPanel.enterToStart.setText("GAME PAUSED, PRESS 'P' TO RESUME");
            threadSleep(100);
        }
    }

    private void stateNotStarted()
    {
        System.out.println("stateNotStarted()");

        while (!firstPlay)
        {
            infoPanel.enterToStart.setText("PRESS 'ENTER' TO START THE GAME");
            threadSleep(100);
        }
    }

    private void statePlaying()
    {
        System.out.println("statePlaying()");

        while (gameStart && !gameOver && !paused)
        {
            infoPanel.enterToStart.setText("Use the mouse to move and shoot. Press 'P' to pause.");
            //Updates the picture and score panel, removed the initial "hit enter to start" message.
            this.evaluateDifficulty();
            this.evaluateWinConditions();
            this.controlGameSpeed();

            drawingPanel.updatePictureState();
            scorePanel.updateDuckHealthLabel();
            scorePanel.updateGunPlatformHealthLabel();
        }
    }

    private void threadSleep(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);

        } catch (InterruptedException ex)
        {
            Logger.getLogger(MainGameLoop.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void controlGameSpeed()
    {
        try
        {
            Thread.sleep(currentGameSpeed);
        } catch (InterruptedException e)
        {
            System.out.println("thread interrupted");
        }
    }

    //The game  will get faster as the duck loses health increasing difficulty
    private void evaluateDifficulty()
    {
        int currentDuckHealth = drawingPanel.myPicture.enemyDuck.getDuckCurrentHealth();
        if (currentDuckHealth <= 25 && currentDuckHealth > 15)
        {
            currentGameSpeed = 8;
        }

        if (currentDuckHealth <= 20 && currentDuckHealth > 10)
        {
            currentGameSpeed = 7;
        }

        if (currentDuckHealth <= 10 && currentDuckHealth > 5)
        {
            currentGameSpeed = 6;
        }
        if (currentDuckHealth <= 5)
        {
            currentGameSpeed = 4;
        }
    }

    //Evaluation for winning conditionns
    //also displays winning/losing message
    private void evaluateWinConditions()
    {
        int currentDuckHealth = drawingPanel.myPicture.enemyDuck.getDuckCurrentHealth();
        if (currentDuckHealth <= 0)
        {
            resetGame();
            JOptionPane.showMessageDialog(this, "You win! "
                    + "Evil duck is defeated! "
                    + "congratulations. :)");
            //drawingPanel.myPicture.enemyDuck.resetDuck();
            return;
        }

        int gunPlatformHealth = drawingPanel.myPicture.playerGunPlatform.getGunPlatformCurrentHealth();
        if (gunPlatformHealth <= 0)
        {
            resetGame();
            JOptionPane.showMessageDialog(this, "You lose! "
                    + "All your base are belong to evil duck! :(");
            //drawingPanel.myPicture.enemyDuck.resetDuck();

        }
    }

    private class DrawingPanel extends JPanel // provided
    {

        public Picture myPicture;

        public DrawingPanel(int width, int height) // given
        {
            myPicture = new Picture(width, height, playerGunPlatform, gunPlatformLaser, enemyDuck);
            setSize(width, height);
        }

        //this method is invoked automatically when repaint occurs in
        //the outer container
        @Override
        public void paintComponent(Graphics g) // given
        {
            super.paintComponent(g);
            myPicture.draw(g); //This does the redrawing based on current state
        }

        //this is about updating the state of elements in the picture
        //not about redrawing
        public void updatePictureState()
        {
            myPicture.updatePictureState();
            this.repaint();
        }
    }

    /*
     * Detects left/right keypresses and moves the gun accordingly, also detects
     * space bar presses and fires the gun as long as there is currently no laser
     * in motion. Also starts the game when it is stopped.
     */
    private class KeyWatcher extends KeyAdapter
    {

        @Override
        public void keyPressed(KeyEvent k)
        {
            int currentGunXPosition = playerGunPlatform.getGunPlatformX();
            if (k.getKeyCode() == KeyEvent.VK_LEFT && currentGunXPosition > 0)
            {
                playerGunPlatform.setGunPlatformX(currentGunXPosition - 5);
            }
            if (k.getKeyCode() == KeyEvent.VK_RIGHT && currentGunXPosition < drawingPanel.getWidth() - 50)
            {
                playerGunPlatform.setGunPlatformX(currentGunXPosition + 5);
            }
            if (k.getKeyCode() == KeyEvent.VK_SPACE && gunPlatformLaser.getFiringLaser() == false)
            {
                playerGunPlatform.setGunPlatformXAfterFire(playerGunPlatform.getGunPlatformX() + 20);
                playerGunPlatform.setGunPlatformYAfterFire(playerGunPlatform.getGunPlatformY() - 20);
                gunPlatformLaser.setFiringLaser(true);

                SoundEffect aSoundEffect = new SoundEffect(soundFileNames.get(Sound.LaserFire));
                Thread soundEffectThread = new Thread(aSoundEffect);
                soundEffectThread.start();
            }
            if (k.getKeyCode() == KeyEvent.VK_ENTER)
            {
                System.out.println("KeyPressed(): Enter key!");
                gameStart = true;
                if (!firstPlay)
                {
                    firstPlay = true;
                }

                if (gameOver)
                {
                    gameOver = false;
                }
            }
            if (k.getKeyCode() == KeyEvent.VK_P)
            {
                System.out.println("KeyPressed(): Pause key!");
                if (gameStart && !gameOver)
                {
                    if (paused)
                    {
                        paused = false;
                    } else
                    {
                        paused = true;
                    }
                }
            }
        }
    }

    /*
     * Detects a click of the mouse and fires the gun as long as there is no
     * laser currently in motion.
     */
    private class MouseWatcher extends MouseAdapter
    {

        @Override
        public void mousePressed(MouseEvent e)
        {
            if (gunPlatformLaser.getFiringLaser() == false)
            {
                playerGunPlatform.setGunPlatformXAfterFire(playerGunPlatform.getGunPlatformX() + 20);
                playerGunPlatform.setGunPlatformYAfterFire(playerGunPlatform.getGunPlatformY() - 20);
                gunPlatformLaser.setFiringLaser(true);
                PlaySoundEffect.playSoundEffect(Sound.LaserFire);
            }
        }

    }

    /*
     * Allows the gun to follow the mouse when it is over the JFrame.
     */
    private class MouseMotionWatcher extends MouseMotionAdapter
    {

        @Override
        public void mouseMoved(MouseEvent e)
        {
            // -25 accounts for the length of the platform (50) and
            // allows the platform to be moved from the center not the corner
            if (e.getX() < drawingPanel.getWidth() - 25 && e.getX() > 25)
            {
                playerGunPlatform.setGunPlatformX(e.getX() - 25);
            }
        }
    }

    /*
     * Detects menu options being selected, breaks out of the update() while 
     * loop and sets the health variables back to initial values, hitting enter will
     * start the game again as normal. Also displays the about dialog box for
     * information about the program and starts/stops the music.
     */
    private class MenuWatcher implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == restart)
            {
                resetGame();
            }
            if (e.getSource() == about)
            {
                JOptionPane.showMessageDialog(drawingPanel, "Evil duck by Max Power 2012 v1.0");
            }

            if (e.getSource() == howToPlay)
            {
                JOptionPane.showMessageDialog(drawingPanel, "Move with the mouse and click to fire."
                        + " Alternatively use the Left/Right arrow keys to move and the spacebar to fire");
            }

            if (e.getSource() == musicToggle)
            {
                if (musicPlayer.isMusicPlaying())
                {
                    musicPlayer.musicOff();
                    musicToggle.setText("Music: Off");

                } else
                {
                    musicPlayer.musicOn();
                    musicToggle.setText("Music: On");
                }
            }

            if (e.getSource() == soundEffectsToggle)
            {
                if (PlaySoundEffect.isSoundEffectEnabled())
                {
                    PlaySoundEffect.soundEffectsOff();
                    soundEffectsToggle.setText("Sound Effects: Off");
                } else
                {
                    PlaySoundEffect.soundEffectsOn();
                    soundEffectsToggle.setText("Sound Effects: On");
                }
            }
        }
    }

    /*
     * A separate panel containing variables and methods to do with the yellow
     * bar at the bottom, keeps the player updated on their health and the ducks health.
     */
    private class ScorePanel extends JPanel
    {

        public JLabel duckHealthLabel;
        public JLabel gunPlatformHealthLabel;

        public ScorePanel(int width, int height)
        {
            super();
            this.setSize(width, height);

            duckHealthLabel = new JLabel("Duck Health: " + enemyDuck.getDuckCurrentHealth());
            this.add(duckHealthLabel);
            gunPlatformHealthLabel = new JLabel("Your Health: " + playerGunPlatform.getGunPlatformCurrentHealth());
            this.add(gunPlatformHealthLabel);
        }

        public void updateDuckHealthLabel()
        {
            duckHealthLabel.setText("Duck Health: " + enemyDuck.getDuckCurrentHealth());
        }

        public void updateGunPlatformHealthLabel()
        {
            gunPlatformHealthLabel.setText("Your Health: " + playerGunPlatform.getGunPlatformCurrentHealth());
        }

    }

    /*
     * A simple panel that tells the player how to start the game, it then displays 
     * intructions on how to play the game, a bit backwards but it works okay.
     */
    private class InfoPanel extends JPanel
    {

        public JLabel enterToStart;

        public InfoPanel(String aString)
        {
            enterToStart = new JLabel(aString);
            this.add(enterToStart);
        }
    }
}
