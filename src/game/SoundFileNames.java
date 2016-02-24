/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author max
 */
public class SoundFileNames
{
    protected static final Map<Sound, String> soundFileNames;
    static
    {
        Map<Sound, String> aMap = new HashMap<>();
        aMap.put(Sound.LaserFire, "laserFire.mp3");
        aMap.put(Sound.DuckQuack, "duckQuack.mp3");
        aMap.put(Sound.GunPlatformHit, "gunPlatformHit.mp3");
        soundFileNames = Collections.unmodifiableMap(aMap);
    }

}
