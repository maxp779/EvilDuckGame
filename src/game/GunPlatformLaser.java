/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author max
 */
public class GunPlatformLaser
{

    private Boolean laserFired = false;
    private GunPlatform playerGunPlatform;
    private Rectangle gunPlatformLaserHitDetector;
    private int gunPlatformXAfterFire, gunPlatformYAfterFire;
    private Duck enemyDuck;
    private int laserRange = 70;

    protected GunPlatformLaser(GunPlatform playerGunPlatform, Duck enemyDuck)
    {
        this.playerGunPlatform = playerGunPlatform;
        this.enemyDuck = enemyDuck;
        gunPlatformXAfterFire = playerGunPlatform.getGunPlatformX() + 20;
        gunPlatformYAfterFire = playerGunPlatform.getGunPlatformY() - 20;
        gunPlatformLaserHitDetector = new Rectangle(playerGunPlatform.getGunPlatformX(), playerGunPlatform.getGunPlatformY());
    }

    protected void drawLaser(Graphics g)
    {
        //Update platform laser and check for hit on duck, also removes duck health if
        //the laser hits the duck

        if (laserFired == true)
        {
            g.setColor(Color.ORANGE);
            g.fillRect(gunPlatformXAfterFire+5, gunPlatformYAfterFire, 10, 20);
            gunPlatformLaserHitDetector.setBounds(gunPlatformXAfterFire+5, gunPlatformYAfterFire, 10, 20);
            gunPlatformYAfterFire = gunPlatformYAfterFire - 2;
            this.checkForHit();
            this.checkForMiss();

        } else
        {
            gunPlatformXAfterFire = playerGunPlatform.getGunPlatformX() + 15;
            gunPlatformYAfterFire = playerGunPlatform.getGunPlatformY() - 20;
        }
    }

    //if a hit on the enemyDuck is detected remove health from it and reset the
    //gunPlatformLasers position
    protected void checkForHit()
    {
        if (gunPlatformLaserHitDetector.intersects(enemyDuck.getDuckHitDetector()))
        {
            laserFired = false;
            gunPlatformLaserHitDetector.setBounds(playerGunPlatform.getGunPlatformX(), playerGunPlatform.getGunPlatformY(), 10, 20);
            enemyDuck.damageTaken();
        }
    }

    //if the lasers position has gone past the duck then the player has missed the duck
    //the lasers position is then reset
    protected void checkForMiss()
    {
        if (gunPlatformLaserHitDetector.getY() <= laserRange)
        {
            laserFired = false;
            gunPlatformLaserHitDetector.setBounds(playerGunPlatform.getGunPlatformX(), playerGunPlatform.getGunPlatformY(), 10, 20);
        }
    }

    public void resetGunPlatformLaser()
    {
        laserFired = false;
    }

    /*
     * Setter and getter to check if gun platform has fired its laser
     */
    public void setFiringLaser(Boolean aBoolean)
    {
        this.laserFired = aBoolean;
    }

    public Boolean getFiringLaser()
    {
        return this.laserFired;
    }
}
