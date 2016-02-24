/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

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
public class SoundEffect implements Runnable
{
    private final String soundFileName;
    
    protected SoundEffect(String soundFileName)
    {
        this.soundFileName = soundFileName;
    }
    
    private void playSound()
    {
        try
        {
            FileInputStream fis = new FileInputStream(soundFileName);
            Player playMP3 = new Player(fis);
            playMP3.play();
        } catch (JavaLayerException | FileNotFoundException ex)
        {
            Logger.getLogger(SoundEffect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        playSound();
    }

}
