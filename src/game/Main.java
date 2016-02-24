/*
 * Main.java
 *
 */
package game;

/**
 *
 * @author M257 CT
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        MainGameLoop mainGameLoop = new MainGameLoop("Save planet earth from evil duck!");
        mainGameLoop.update();
    }

}
