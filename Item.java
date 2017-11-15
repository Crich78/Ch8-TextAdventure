
/**
 * This is the Item Class. Items have a description and weight. They can be put into each room. 
 * This class builds each item object and reports to the rest of the program the attributes of it
 *
 * @author Connor Richardson
 * @version 2017.11.14
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private double weight;

    /**
     * Constructor for Item Objects
     */
    public Item(String description, double weight)
    {
       this.description =  description;
       this.weight = weight;
    }

    /**
     * Getters for weight and descriptioin
     */
    public String getDescription()
    {
     return description;   
    }
    
    public double getWeight()
    {
     return weight;   
    }
    
    
}
