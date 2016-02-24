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
import java.util.Random;

/**
 *
 * @author max
 */
public class Duck
{

    private int duckBodyX, duckBodyY;
    private int duckHeadX, duckHeadY, duckEyeX, duckEyeY, duckEyeBrowX1,
            duckEyeBrowY1, duckEyeBrowX2, duckEyeBrowY2, duckBeakX, duckBeakY;
    private int duckStartingHealth = 30;
    private int maxNumberOfDuckLasers = 25;
    private int duckCurrentHealth = duckStartingHealth;
    private Rectangle duckHitDetector;
    private Random eyeColorGenerator = new Random();
    private Color[] aColorArray = new Color[5];
    private GunPlatform playerGunPlatform;
    private DuckLaser[] duckLaserArray;

    protected Duck(GunPlatform playerGunPlatform)
    {
        this.playerGunPlatform = playerGunPlatform;

        duckBodyX = 0;
        duckBodyY = 75;
        duckHeadX = duckBodyX + 50;
        duckHeadY = duckBodyY - 15;
        duckEyeX = duckBodyX + 75;
        duckEyeY = duckBodyY - 10;
        duckEyeBrowX1 = duckBodyX + 75;
        duckEyeBrowY1 = duckBodyY - 12;
        duckEyeBrowX2 = duckBodyX + 90;
        duckEyeBrowY2 = duckBodyY - 6;
        duckBeakX = duckBodyX;
        duckBeakY = duckBodyY;

        duckHitDetector = new Rectangle(duckBodyX, duckBodyY);

        aColorArray[0] = Color.YELLOW;
        aColorArray[1] = Color.RED;
        aColorArray[2] = Color.ORANGE;
        aColorArray[3] = Color.PINK;
        aColorArray[4] = Color.BLACK;

        this.numberOfDuckLasers(maxNumberOfDuckLasers);
    }

    protected void numberOfDuckLasers(int numberOfLasers)
    {
        duckLaserArray = new DuckLaser[numberOfLasers];
        for (int count = 0; count < numberOfLasers; count++)
        {
            duckLaserArray[count] = new DuckLaser(playerGunPlatform, this);
        }
    }

    protected void drawDuckBody(Graphics g)
    {

        //Draw duck body, if the ducks health is 10 or less the colors randomly change
        //to show the duck is enraged
        int randomInt = eyeColorGenerator.nextInt(4);
        if (duckCurrentHealth <= 10)
        {
            g.setColor(aColorArray[randomInt]);
            g.fillOval(duckBodyX, duckBodyY, 100, 50);

        } else
        {
            g.setColor(Color.YELLOW);
            g.fillOval(duckBodyX, duckBodyY, 100, 50);
        }

        g.setColor(Color.BLACK);
        g.drawString("Evil duck!", duckBodyX + 20, duckBodyY + 25);
    }

    protected void drawDuckHead(Graphics g)
    {
        //Draw duck head and eyebrow, if ducks health is 15 or less the colors randomly change
        //to show the duck is enraged
        int randomInt = eyeColorGenerator.nextInt(4);
        if (duckCurrentHealth <= 15)
        {
            g.setColor(aColorArray[randomInt]);
            g.fillOval(duckBodyX + 50, duckBodyY - 15, 50, 25);

        } else
        {
            g.setColor(Color.YELLOW);
            g.fillOval(duckBodyX + 50, duckBodyY - 15, 50, 25);
        }

        //draw eyebrow
        g.setColor(Color.black);
        g.drawLine(duckBodyX + 75, duckBodyY - 12, duckBodyX + 90, duckBodyY - 6);
    }

    protected void drawDuckEye(Graphics g)
    {
        int randomInt = eyeColorGenerator.nextInt(4);

        g.setColor(aColorArray[randomInt]);
        g.fillOval(duckBodyX + 75, duckBodyY - 10, 10, 10);

    }

    protected void drawDuckBeak(Graphics g)
    {
        g.setColor(Color.orange);
        g.fillOval(duckBodyX + 90, duckBodyY - 6, 20, 10);
    }

    protected void updateDuckHitDetector()
    {
        duckHitDetector.setBounds(duckBodyX, duckBodyY, 120, 50);
    }

    protected Rectangle getDuckHitDetector()
    {
        return this.duckHitDetector;
    }

    protected void damageTaken()
    {
        this.duckCurrentHealth--;
        PlaySoundEffect.playSoundEffect(Sound.DuckQuack);
    }

    /*
     * Setters and getter for duck variables.
     */
    public void setDuckBodyX(int xCoordinate)
    {
        this.duckBodyX = xCoordinate;
    }

    public void setDuckBodyY(int yCoordinate)
    {
        this.duckBodyY = yCoordinate;
    }

    public int getDuckBodyX()
    {
        return this.duckBodyX;
    }

    public int getDuckBodyY()
    {
        return this.duckBodyY;
    }

    public int getDuckCurrentHealth()
    {
        return duckCurrentHealth;
    }

    public void setDuckCurrentHealth(int health)
    {
        duckCurrentHealth = health;
    }

    protected void drawAllDuckLasers(Graphics g)
    {
        int duckAggressionLevel = duckStartingHealth - duckCurrentHealth;
        if (duckAggressionLevel > duckLaserArray.length - 1)
        {
            duckAggressionLevel = 20;
        }
        for (int count = 0; count < duckAggressionLevel; count++)
        {
            duckLaserArray[count].setDuckLaserCurrentValues(duckBodyX, duckBodyY);
            duckLaserArray[count].duckLaserUpdate();
            duckLaserArray[count].drawDuckLaser(g);
        }

    }

    protected void resetAllDuckLasers()
    {
        for (int count = 0; count < duckLaserArray.length - 1; count++)
        {
            duckLaserArray[count].duckLaserReset();
        }
    }

    protected void resetDuck()
    {
        duckBodyX = 0;
        duckBodyY = 75;
        duckHeadX = duckBodyX + 50;
        duckHeadY = duckBodyY - 15;
        duckEyeX = duckBodyX + 75;
        duckEyeY = duckBodyY - 10;
        duckEyeBrowX1 = duckBodyX + 75;
        duckEyeBrowY1 = duckBodyY - 12;
        duckEyeBrowX2 = duckBodyX + 90;
        duckEyeBrowY2 = duckBodyY - 6;
        duckBeakX = duckBodyX;
        duckBeakY = duckBodyY;
        duckHitDetector = new Rectangle(duckBodyX, duckBodyY);
        this.resetAllDuckLasers();
        duckCurrentHealth = duckStartingHealth;
    }
}
