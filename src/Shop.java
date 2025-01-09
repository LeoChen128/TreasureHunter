/**
 * The Shop class controls the cost of the items in the Treasure Hunt game.<p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method.<p>
 */
import java.util.Locale;
import java.util.Scanner;
import java.awt.Color;
import javax.swing.*;

public class Shop
{
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;

    // instance variables
    private double markdown;
    private Hunter customer;

    //Constructor
    public Shop(double markdown)
    {
        this.markdown = markdown;
        customer = null;
    }

    /** method for entering the shop
     * @param hunter  the Hunter entering the shop
     * @param buyOrSell  String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell)
    {
        customer = hunter;

        Scanner scanner = new Scanner(System.in);
        if (buyOrSell.equals("B") || buyOrSell.equals("b"))
        {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, true);
            if (cost == 0)
            {
                System.out.println("We ain't got none of those.");
            }
            else
            {
                if (TreasureHunter.getCheatMode()){
                    System.out.print("It'll cost you " + 1 + " gold. Buy it (y/n)? ");
                    String option = scanner.nextLine();

                    if (option.equals("y") || option.equals("Y"))
                    {
                        buyItem(item);
                    }
                }
                else {
                    System.out.print("It'll cost you " + cost + " gold. Buy it (y/n)? ");
                    String option = scanner.nextLine();

                    if (option.equals("y") || option.equals("Y")) {
                        buyItem(item);
                    }
                }
            }
        }
        else
        {
            System.out.println("What're you lookin' to sell? ");
            System.out.print("You currently have the following items: " + customer.getInventory());
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, false);
            if (cost == 0)
            {
                System.out.println("We don't want none of those.");
            }
            else
            {
                System.out.print("It'll get you " + cost + " gold. Sell it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    sellItem(item);
                }
            }
        }
    }

    /** A method that returns a string showing the items available in the shop (all shops sell the same items)
     *
     * @return  the string representing the shop's items available for purchase and their prices
     */
    public static final String reset = "\u001B[0m";
    public static final String random = "\033[0;189m";
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String ORANGE = "\033[38;2;225;153;0m"; //ORANGE
    public static final String BROWN = "\033[38;2;135;82;62m"; //BROWN
    public static final String BANANA_YELLOW = "\033[38;2;240;238;113m"; //BANANA YELLOW
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String GRAY_BOLD = "\033[1;37m";  // gray
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW

    public String inventory()
    {

        String str;
        if (TreasureHunter.getCheatMode()){
            str = CYAN_BOLD + "🥤 Water: " + reset + BLUE + 1 + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += BROWN + "🧵 Rope: " + reset + WHITE + 1 + reset + YELLOW_BRIGHT + " gold" + reset + "\n";
            str += YELLOW_BRIGHT + "🔪 Machete: " + reset + GRAY_BOLD + 1 + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += BROWN +  "🐎 Horse: " + reset + ORANGE + 1 + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += ORANGE + "⛵ Boat: " + reset + BANANA_YELLOW + 1 + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
        }
        else {
            str = CYAN_BOLD + "🥤 Water: " + reset + BLUE + WATER_COST + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += BROWN + "🧵 Rope: " + reset + WHITE + ROPE_COST + reset + YELLOW_BRIGHT + " gold" + reset + "\n";
            str += BLACK + "🔪 Machete: " + reset + GRAY_BOLD + MACHETE_COST + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += BROWN +  "🐎 Horse:  " + reset + ORANGE + HORSE_COST + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
            str += ORANGE + "⛵ Boat: " + reset + BANANA_YELLOW + BOAT_COST + reset + YELLOW_BRIGHT + " gold" + reset +"\n";
        }

        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     * @param item The item being bought.
     */
    public void buyItem(String item)
    {
        int costOfItem = checkMarketPrice(item, true);
        if (TreasureHunter.getCheatMode()){
            if (customer.buyItem(item, 1)){
                System.out.println("Ye' got yerself a " + item + ". Come again soon.");
            }
            else{
                System.out.println("You shockingly don't have a single coin of gold or already have this item.");
            }
        }
        else {
            if (customer.buyItem(item, costOfItem)) {
                System.out.println("Ye' got yerself a " + item + ". Come again soon.");
            } else {
                System.out.println("Hmm, either you don't have enough gold or you've already got one of those!");
            }
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     * @param item The item being sold.
     */
    public void sellItem(String item)
    {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice))
        {
            System.out.println("Pleasure doin' business with you.");
        }
        else
        {
            System.out.println("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying)
    {
        if (isBuying)
        {
            return getCostOfItem(item);
        }
        else
        {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item)
    {
        item = item.toLowerCase();
        if (item.equals("water"))
        {
            return WATER_COST;
        }
        else if (item.equals("rope"))
        {
            return ROPE_COST;
        }
        else if (item.equals("machete"))
        {
            return MACHETE_COST;
        }
        else if (item.equals("horse"))
        {
            return HORSE_COST;
        }
        else if (item.equals("boat"))
        {
            return BOAT_COST;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item)
    {
        int cost = (int)(getCostOfItem(item) * markdown);
        return cost;
    }
}
