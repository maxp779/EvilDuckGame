/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.SoundFileNames.soundFileNames;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author max
 */
public class GunPlatform
{

    private int gunPlatformX, gunPlatformY, gunX, gunY;
    private Rectangle gunPlatformHitDetector;
    private int gunPlatformXAfterFire;
    private int gunPlatformYAfterFire;
    private int gunPlatformStartingHealth = 5;
    private int gunPlatformCurrentHealth = gunPlatformStartingHealth;

    public GunPlatform(int gunPlatformX, int gunPlatformY)
    {
        this.gunPlatformX = gunPlatformX;
        this.gunPlatformY = gunPlatformY;
        gunX = gunPlatformX + 15;
        gunY = gunPlatformY - 20;
        gunPlatformHitDetector = new Rectangle(gunPlatformX, gunPlatformY);
    }

    protected void drawGunPlatform(Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(gunPlatformX, gunPlatformY, 50, 10);
        g.setColor(Color.green);
        g.fillRect(gunPlatformX + 15, gunPlatformY - 20, 20, 20);
    }

    protected void updateGunPlatformHitDetector()
    {
        gunPlatformHitDetector.setBounds(gunPlatformX, gunPlatformY - 20, 50, 10);
    }

    protected void gunPlatformDamageTaken()
    {
        gunPlatformCurrentHealth--;
        PlaySoundEffect.playSoundEffect(Sound.GunPlatformHit);
    }

    protected void resetGunPlatform()
    {
        gunPlatformCurrentHealth = gunPlatformStartingHealth;
    }

    protected Rectangle getHitDetector()
    {
        return this.gunPlatformHitDetector;
    }

    public int getGunPlatformX()
    {
        return this.gunPlatformX;
    }

    public int getGunPlatformY()
    {
        return this.gunPlatformY;
    }

    public void setGunPlatformX(int xCoordinate)
    {
        this.gunPlatformX = xCoordinate;
    }

    public void setGunPlatformY(int yCoordinate)
    {
        this.gunPlatformY = yCoordinate;
    }

    public void setGunPlatformXAfterFire(int xCoordinate)
    {
        this.gunPlatformXAfterFire = xCoordinate;
    }

    public int getGunPlatformXAfterFire()
    {
        return this.gunPlatformXAfterFire;
    }

    public void setGunPlatformYAfterFire(int yCoordinate)
    {
        this.gunPlatformYAfterFire = yCoordinate;
    }

    public int getGunPlatformYAfterFire()
    {
        return this.gunPlatformYAfterFire;
    }

    public int getGunPlatformCurrentHealth()
    {
        return gunPlatformCurrentHealth;
    }

    public void setGunPlatformCurrentHealth(int health)
    {
        gunPlatformCurrentHealth = health;
    }

    public int getGunX()
    {
        return gunX;
    }

    public int getGunY()
    {
        return gunY;
    }
}
