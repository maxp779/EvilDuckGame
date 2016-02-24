/*
 * Picture.java
 *
 * This class includes a constructor that can be used to set up a Picture object
 * to allow drawing on an area of known size
 * The method draw receives a graphics context 
 *
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

/**
 *
 * @author Max Power
 */
public class Picture
{

    public int width, height;
    /*
     * duckBodyX and duckBodyY control other duck related drawing variables
     * negating the need for getter/setter methods for every single duck
     * variable, only duckBodyX and duckBodyY will need to be updated.
     */

    public Boolean reachedEnd = false;

    protected GunPlatform playerGunPlatform;
    protected GunPlatformLaser gunPlatformLaser;
    protected Duck enemyDuck;

    public Picture(int width, int height, GunPlatform playerGunPlatform, GunPlatformLaser gunPlatformLaser, Duck enemyDuck)
    {
        this.playerGunPlatform = playerGunPlatform;
        this.gunPlatformLaser = gunPlatformLaser;
        this.enemyDuck = enemyDuck;
        this.width = width;
        this.height = height;

//        //Setting up the gun platform
//        playerGunPlatform = new GunPlatform(250, 490);
//
//        //Setting up the duck
//        enemyDuck = new Duck(playerGunPlatform);
//
//        //Setting up DuckLaser objects to be fired by duck        
//        gunPlatformLaser = new GunPlatformLaser(playerGunPlatform, enemyDuck);
    }

    /*
     * Keeps the duck moving back and forth across the screen, booleans prevent
     * it continuing off the screen.
     */
    public void updatePictureState()
    {
        int currentDuckBodyX = enemyDuck.getDuckBodyX();

        //The -110 is the estimated total length of the duck including beak
        // the duck being of length 100 and the beak of length 10.
        if (currentDuckBodyX == width - 110)
        {
            reachedEnd = true;
        }
        if (currentDuckBodyX == 0)
        {
            reachedEnd = false;
        }

        if (reachedEnd == true)
        {
            enemyDuck.setDuckBodyX(currentDuckBodyX - 1);
        }
        if (reachedEnd == false)
        {
            enemyDuck.setDuckBodyX(currentDuckBodyX + 1);
        }
        
    }

    public void drawBackground(Graphics g)
    {
        //Draw filled circle (earth)
        g.setColor(Color.blue);
        g.fillOval(100, 100, 100, 100);
        g.setColor(Color.green);
        g.fillOval(120, 120, 30, 70);
        g.fillOval(160, 120, 30, 30);
        g.fillOval(160, 160, 30, 10);
        g.setColor(Color.white);
        g.drawString("<-- Earth", 210, 160);

        //Draw moon and some stars
        g.setColor(Color.white);
        g.fillOval(200, 75, 50, 50);
        g.drawString("<-- Moon", 270, 100);
        g.fillOval(50, 400, 5, 5);
        g.fillOval(100, 70, 5, 5);
        g.fillOval(150, 250, 5, 5);
        g.fillOval(200, 350, 5, 5);
        g.fillOval(250, 420, 5, 5);
        g.fillOval(300, 25, 5, 5);
        g.fillOval(350, 390, 5, 5);
        g.fillOval(400, 430, 5, 5);
        g.fillOval(450, 290, 5, 5);
        g.fillOval(500, 460, 5, 5);
    }

    /*
     * Draws everything seen on the screen, animates the duck and gun platform,
     * also deals with gun platform hit detection, gun platform lasers hit detection
     * (to see if the players laser hits the duck) and lastly animates DuckLaser objects
     * incrementally as the duck loses health.
     */
    public void draw(Graphics g)
    {
        this.drawBackground(g);

        //Draw gun platform
        playerGunPlatform.drawGunPlatform(g);

        //Draw duck body
        enemyDuck.drawDuckBody(g);

        //Draw duck head
        enemyDuck.drawDuckHead(g);

        //Draw duck eye, color is determined randomly to make it look more evil
        enemyDuck.drawDuckEye(g);

        //Draw beak
        enemyDuck.drawDuckBeak(g);

        //Update duck hit detection Rectangle and gunPlatform hit detection Recrangle
        
        //duckHitDetector.setBounds(duckBodyX, duckBodyY, 120, 50);
        enemyDuck.updateDuckHitDetector();
        
        //gunPlatformHitDetector.setBounds(gunPlatformX, gunPlatformY - 20, 50, 10);
        playerGunPlatform.updateGunPlatformHitDetector();
        
        
        //Update platform laser and check for hit on duck, also removes duck health if
        //the laser hits the duck      
        gunPlatformLaser.drawLaser(g);
        
        enemyDuck.drawAllDuckLasers(g);
        
       
    }

//    public GunPlatform getPlayerGunPlatform()
//    {
//        return playerGunPlatform;
//    }
//
//    public GunPlatformLaser getGunPlatformLaser()
//    {
//        return gunPlatformLaser;
//    }
//
//    public Duck getEnemyDuck()
//    {
//        return enemyDuck;
//    }



}
