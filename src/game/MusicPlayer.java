/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author max
 */
public class MusicPlayer implements Runnable
{

    //boolean paused = false;
    Player playMP3;
    private boolean playMusic = true;

    private void playMusic()
    {
        String aPathname = "music.mp3";
        File aFile = new File(aPathname);
        if (aFile.exists())
        {
            try
            {
                //continue to loop                
                while (true)
                {
                    if (playMusic)
                    {
                        FileInputStream fis = new FileInputStream(aFile);
                        playMP3 = new Player(fis);
                        playMP3.play();
                    } else
                    {
                        Thread.sleep(100);
                    }
                }
            } catch (FileNotFoundException e)
            {

            } catch (JavaLayerException e)
            {

            } catch (InterruptedException ex)
            {
                Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
    protected boolean isMusicPlaying()
    {
        return playMusic;
    }

    protected void musicOff()
    {
        playMP3.close();
        playMusic = false;
    }

    protected void musicOn()
    {
        playMusic = true;
    }

    @Override
    public void run()
    {
        playMusic();
    }
}
