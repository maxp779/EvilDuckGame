/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author max
 */
/*
     * A neccesary class to have more than one laser fired by the duck at the same time,
     * this class deals directly with drawing the ducks lasers and with hit detection by them
     * on the players gun platform. Each DuckLaser object uses the methods duckLaserUpdate and
     * drawDuckLaser to animate and update the lasers, these methods are called by draw() above.
 */
public class DuckLaser
{

    private int duckLaserX;
    private int duckLaserY;
    private int duckBodyX;
    private int duckBodyY;
    private Rectangle duckLaserHitDetector;
    private Random randomNumberGenerator;
    private int randomFire;
    private GunPlatform playerGunPlatform;
    private Duck enemyDuck;

    public DuckLaser(GunPlatform playerGunPlatform, Duck enemyDuck)
    {
        this.enemyDuck = enemyDuck;
        this.playerGunPlatform = playerGunPlatform;
        randomNumberGenerator = new Random();
        duckLaserHitDetector = new Rectangle(duckLaserX, duckLaserY);
        randomFire = randomNumberGenerator.nextInt(2);
        this.duckLaserReset();
    }

    public void setDuckLaserCurrentValues(int duckBodyX, int duckBodyY)
    {
        this.duckBodyX = duckBodyX;
        this.duckBodyY = duckBodyY;
    }

    public void duckLaserUpdate()
    {
        //This if statement stops lasers appearing on the left at Y position 0, because the
        //DuckLaser objects initialised at the start use the ducks initial position
        //at the start of the game which is 0 so when they are created they appear
        //out of thin air at Y pos 0 without this if statement.
        if (this.duckLaserX == 0)
        {
            this.duckLaserX = duckBodyX;
        }

        if (this.duckLaserY < 500 && !duckLaserHitDetector.intersects(playerGunPlatform.getHitDetector()))
        {
            this.duckLaserY = this.duckLaserY + 1;

            //Makes lasers randomly come from the front of the duck to eliminate a blindspot
            //where the player cannot be hit
            if (this.randomFire == 0)
            {
                this.duckLaserHitDetector.setBounds(this.duckLaserX, this.duckLaserY, 5, 20);
            }
            if (this.randomFire == 1)
            {
                this.duckLaserHitDetector.setBounds(this.duckLaserX + 100, this.duckLaserY, 5, 20);
            }

        }

        if (this.duckLaserY >= 500)
        {
            this.duckLaserReset();
        }

        if (this.duckLaserHitDetector.intersects(playerGunPlatform.getHitDetector()))
        {
            playerGunPlatform.gunPlatformDamageTaken();
            this.duckLaserReset();
        }
    }

    //Resets the ducks lasers once they hit the player or go off the screen
    //so that they can be fired again.
    public void duckLaserReset()
    {
        this.duckLaserX = enemyDuck.getDuckBodyX();
        this.duckLaserY = enemyDuck.getDuckBodyY();
        this.duckLaserHitDetector.setBounds(duckLaserX, duckLaserY, 5, 20);
    }

    //Draws the ducks lasers on the screen
    protected void drawDuckLaser(Graphics g)
    {
        g.setColor(Color.RED);

        if (this.randomFire == 0)
        {
            g.fillRect(this.duckLaserX, this.duckLaserY, 5, 20);
        }
        if (this.randomFire == 1)
        {
            g.fillRect(this.duckLaserX + 100, this.duckLaserY, 5, 20);
        }
    }

    public int getDuckLaserY()
    {
        return this.duckLaserY;
    }

    public int getDuckLaserX()
    {
        return this.duckLaserX;
    }

}
