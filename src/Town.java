import java.util.Random;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;


    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();
        Terrain previousTerrain = terrain;
        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            item = item.toUpperCase();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                Random rand = new Random();
                int deathChance = rand.nextInt(4) + 1;
                if (hunter.getOriginalGold() != 25){
                    hunter.removeItemFromKit(item);
                    printMessage += "\nUnfortunately, your " + item + " broke.";
                    if (item.equals("BOAT") && deathChance == 1){
                        System.out.println("Unfortunately for you, your boat has sunk bc it was terrible. oops.");
                        TreasureHunter.setGameOver(true);
                    }
                    else if (item.equals("WATER") && deathChance == 1){
                        System.out.println("Unfortunately for you, ur water was tainted with lead bc the townspeople hated u. oops.");
                        TreasureHunter.setGameOver(true);
                    }
                    else if (item.equals("MACHETE") && deathChance == 1){
                        System.out.println("sadly, u have no idea how to use a machete so u messed up a little. oops.");
                        TreasureHunter.setGameOver(true);
                    }
                    else if (item.equals("ROPE") && deathChance == 1){
                        System.out.println("Unfortunately for you, ur rope broke bc it was basically a piece of string so u should've known better. oops.");
                        TreasureHunter.setGameOver(true);
                    }
                    else if (item.equals("HORSE") && deathChance == 1){
                        System.out.println("Unfortunately for you, ur horse hated u and so it bucked u off and u broke several bones. oops.");
                        TreasureHunter.setGameOver(true);
                    }

                }
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    public int diceRoll(){
        int rollOne = (int) (Math.random() * 6) + 1;
        int rollTwo = (int) (Math.random() * 6) + 1;
        return rollOne + rollTwo;
    }

    public void diceResult(int choice, int wager){
        int total = diceRoll();
        System.out.println(total);
        int bound1 = total + 2;
        int bound2 = total - 2;
        int hunterGold = hunter.getGold();
        if (hunter.getGold() == 0 || wager > hunter.getGold()){
            printMessage = "You don't have enough gold!";
        }
        else{
            hunter.changeGold(-1 * wager);
            if (choice == total) {
                hunter.changeGold(wager * 3);
                printMessage = "Wow, you actually won.... Here's yer gold :(";
            }
            else if(bound2 <= choice && choice <= bound1){
                hunter.changeGold(wager);
                printMessage = "Yer guess was close to the actual number. Here's yer gold back";
            }
            else{
                printMessage = "HA HA HA, YOU LOST! Give up yer gold :)";
            }
        }
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }

        else if (hunter.getOriginalGold() == 25){
            noTroubleChance = 0.5;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int)(Math.random() * 10) + 1;
            if (TreasureHunter.getCheatMode()){
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  100 + " gold.";
                hunter.changeGold(100);
            }
            else {
                if (Math.random() + 0.45 > noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                }
            }
        }
    }

    public Integer treasureHunt() {
        Random rand = new Random();

        return rand.nextInt(101) + 1;
    }

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        if (hunter.getOriginalGold() != 25) {
            double rand = Math.random();
            return (rand < 0.5);
        }
        else{
            return false;
        }
    }
}
