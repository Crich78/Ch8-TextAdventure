
/**
 * This is a main class that can run the game! It creates a new game object and activates the
 * play method of that game object.
 *
 * @author Connor Richardson
 * @version 2017.11.14
 */
public class GameMain
{
    /**
     * The method below creates the game object and runs the play method inside of it
     */
    
    
    public GameMain()
    {
        // initialise instance variables
        
        Game game = new Game();
        game.play();
    
    }
}
