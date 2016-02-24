/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.SoundFileNames.soundFileNames;

/**
 *
 * @author max
 */
public class PlaySoundEffect
{

    private static boolean soundEffectState = true;

    public static void playSoundEffect(Sound aSound)
    {
        if (soundEffectState)
        {
            SoundEffect aSoundEffect = new SoundEffect(soundFileNames.get(aSound));
            Thread soundEffectThread = new Thread(aSoundEffect);
            soundEffectThread.start();
        }
    }

    protected static void soundEffectsOff()
    {
        soundEffectState = false;

    }

    protected static void soundEffectsOn()
    {
        soundEffectState = true;

    }

    protected static boolean isSoundEffectEnabled()
    {
        return soundEffectState;
    }

}
