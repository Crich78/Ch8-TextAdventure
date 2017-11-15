/**
 *  This is the main class in our text adventure. You start off lost in a dense forest. You need to find your way out before it gets dark!
 *  This runs the game and holds the descriptions of rooms and items, as well as the code making the game actually run.
 *  
 * @author  Connor Richardson
 * @version 2017.11.13
 */
import java.util.Stack; //Needed to import to add the method that allows you to go back multiple rooms
public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room lastRoom;
    private Room cliff;
    private Room win;
    private Room fail;
    private int timer = 0;
    private Stack multiLastRooms = new Stack(); // Used a stack to remember which rooms I was in and go back in a LIFO order.
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together. Also link items to rooms
     */
    private void createRooms()
    {
        Room a1, a2, a3, b1, b2, b3, c1, c2, c3, bridge, outskirts;
       
        // create the rooms
        a1= new Room("see a strong river flowing south to the west. The trees seem to be letting up a little to the north.");
        a2 = new Room(" are still in a very dense forest. Maybe the trees are clearing up the the north?");
        a3 = new Room("see a 30 foot cliff looming over the forest to the east. No way you could climb that. Every other direction is full of trees.");
        b1 = new Room("see a strong river flowing to the west. Heavily wooded areas are in all other directions.");
        b2 = new Room("see only trees around you.");
        b3 = new Room("see a 30 foot cliff to the east. There might be one spot that is climbable. Trees surround you every other way.");
        c1 = new Room(" see the river cuts east here, but there seems to be a small wooden bridge to the south over it. The trees are less the direction that the river flows.");
        c2 = new Room("are on a peaceful riverbank. If you weren't lost, this might be a nice spot for a picnic.");
        c3 = new Room("see a 30 foot cliff to your east and a strong flowing river to the south. Your options are definitely limited.");
        outskirts = new Room("make your way out of the trees and find yourself in an open field.");
        cliff = new Room("managed to climb up the rocks to the top of the cliff. Going down, doesn't look as easy. You have to almost be out though now!");
        bridge = new Room("cross the bridge and find a small trail heading south!");
        win = new Room(" manage to spot a road not too far off! Congratulations on finding your way out of the woods! Have a safe ride home! :)" );
        fail = new Room(" are entirely lost. It is pitch black out and there is no way that you are getting out of here tonight. I'm sorry, you LOSE.");
        
        // initialise room exits
        a1.setExit("north", outskirts);
        a1.setExit("east", a2);
        a1.setExit("south", b1);
        
        a2.setExit("east", a3);
        a2.setExit("west", a1);
        a2.setExit("south", b2);
        a2.setExit("north", outskirts);
        
        a3.setExit("north", outskirts);
        a3.setExit("west", a2);
        a3.setExit("south", b3);
        
        b1.setExit("north", a1);
        b1.setExit("east", b2);
        b1.setExit("south", c1);
        
        b2.setExit("east", b3);
        b2.setExit("west", b1);
        b2.setExit("south", c2);
        b2.setExit("north", a2);
        
        b3.setExit("north", a3);
        b3.setExit("west", b2);
        b3.setExit("south", c3);
        b3.setExit("up", cliff);
        
        c1.setExit("north", b1);
        c1.setExit("east", c2);
        c1.setExit("south" , bridge);
        
        c2.setExit("east", c3);
        c2.setExit("west", c1);
        c2.setExit("north", b2);
        
        c3.setExit("west", c2);
        c3.setExit("north", b3);
        
        outskirts.setExit("north", win);
        outskirts.setExit("east", a3);
        outskirts.setExit("west", a1);
        outskirts.setExit("south", a2);
        
        cliff.setExit("north", outskirts);
        cliff.setExit("east", win);
        
        bridge.setExit("north", c1);
        bridge.setExit("south", win);
        
        c3.addItem(new Item ("shiny stone", 0.1));
        a1.addItem(new Item ("sturdy branch", 2));
        a3.addItem(new Item("water bottle" , 0.5));
        a3.addItem(new Item("ripped backpack" , 1));
        currentRoom = b2;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if(currentRoom == win)
            {
                finished = true;
            }
            if (timer > 10)
            {
                currentRoom = fail;
                System.out.println(currentRoom.getLongDescription());
                finished = true;
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are lost in the woods!");
        System.out.println("The sun will go down soon and it is up to you to find your way out!");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case RELAX:
                relax();
                break;
                
            case BACK:
                back();
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around in a dense woods.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /**
    *  This allows the player to look around the room and recieve the long
    *  description over again.
    */
    private void look()
    {
     System.out.println(currentRoom.getLongDescription());   
    }
    
    /**
     * This allows the player to enjoy nature for a little while.
     */
    private void relax()
    {
        System.out.println("You take a few deep breaths and really notice the beauty of the environment around you.");
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * this also keeps track of which rooms you were into by passing the room you are in to a stack right before you leave it
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            System.out.println("That way is blocked!");
        }
        else {
            lastRoom = currentRoom;
            multiLastRooms.push (lastRoom);
            currentRoom = nextRoom;
            timer = timer + 1;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    /**
     * Added a method that allows you to go to the room right before the current room.
     * Commented out because I solved it in a better way that lets you go back multiple rooms
     
    private void back()
    {
        if (lastRoom == null)
        System.out.println("You haven't gone anywhere yet!");
        else {
            currentRoom = lastRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    */
    
   
    /**
     * Added this method that uses a stack to remember which rooms you have been in, and it orders it for easy LIFO access.
     * Also added an if statement so that you can climb up the cliff, but can't get back down the way you came. This meant nullifying the back method for that.
     * It gives you an error statement if you try
     */
    private void back()
    {
        if (currentRoom == cliff)
        {System.out.println("No way you can get back down.");
        }
        else{
        if (multiLastRooms.empty())
        {
            System.out.println("Well you wouldn't be lost if you could remember where you were before this...");
        }
        
        else
        {
            currentRoom = (Room) multiLastRooms.pop();
            System.out.println("You retrace your foot steps and find your way back to where you were earlier.");
            System.out.println(currentRoom.getLongDescription());
        }
    
    
    }
}
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
