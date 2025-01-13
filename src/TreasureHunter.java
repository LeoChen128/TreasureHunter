/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private static boolean cheatMode;
    private static boolean gameOver;
    private boolean rubies;
    private boolean emeralds;
    private boolean opals;
    private boolean leftTown;
    private List<String > choices = new ArrayList<>();
    private static int luck;
    private int moneyWonInCasino;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        cheatMode = false;
        gameOver = false;
        rubies = false;
        emeralds = false;
        opals = false;
        leftTown = false;
        moneyWonInCasino = 0;
        luck = 0;
    }

    public static int getLuck(){
        return luck;
    }

    public boolean getEasyMode(){
        return easyMode;
    }

    public static boolean getGameOver(){
        return gameOver;
    }

    public static void setGameOver(boolean newGameOver){
        gameOver = newGameOver;
    }


    // starts the game; this is the only public method
    public void play ()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    public static boolean getCheatMode(){
        return cheatMode;
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        choices.add("M");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        name = name.toLowerCase();

        if (name.equals("cheat")){
            cheatMode = true;

            System.out.println("You have entered cheat mode. Reenter your name.");
            System.out.print("What's your name, Hunter? ");
            name = scanner.nextLine();
        }

        // set hunter instance variable
        hunter = new Hunter(name, 10);


        if (!cheatMode) {
            System.out.print("Hard mode? (y/n): ");
            String hard = scanner.nextLine();
            if (hard.equals("y") || hard.equals("Y")) {
                if (!cheatMode) {
                    hardMode = true;
                }
            } else {
                System.out.print("Easy mode? (y/n): ");
                String easy = scanner.nextLine().toLowerCase();
                if (easy.equals("y")) {
                    if (!cheatMode) {
                        easyMode = true;
                        hunter.changeGold(15);
                        hunter.setOriginalGold(25);
                    }
                }
            }
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode)
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if (easyMode){
            // In easy mode, you get more money back when you sell items
            markdown = 1;

            // The town is "easier," or less "tough"
            toughness = 0.2;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        if (!gameOver) {
            Scanner scanner = new Scanner(System.in);
            String choice = "";


            while (!(choice.equals("X") || choice.equals("x"))) {
                System.out.println();
                System.out.println(currentTown.getLatestNews());
                System.out.println("***");
                System.out.println(hunter);
                System.out.println(currentTown);
                System.out.println("(B)uy something at the shop. 🪙");
                System.out.println("(S)ell something at the shop. 💰");
                System.out.println("(M)ove on to a different town. 🧭");
                System.out.println("(L)ook for trouble! (￣ε(#￣)☆╰╮o(￣皿￣///)");
                System.out.println("(H)unt for treasure! 💎");
                System.out.println("Go to the (C)asino and make bank! 🎰");
                System.out.println("Give up the hunt and e(X)it. 😢");
                System.out.println();
                System.out.print("What's your next move? ");
                choice = scanner.nextLine();
                choice = choice.toUpperCase();
                processChoice(choice);
                if (choice.equals("M")){
                    if (currentTown.leaveTown()){
                        choices.add("M");
                    }
                }
                choices.add(choice);
                if (choice.equals("H")){
                    choices.clear();
                }
            }
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s")) {
            currentTown.enterShop(choice);

        }
        else if (choice.equals("M") || choice.equals("m")) {
            if (currentTown.leaveTown()) {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if (choice.equals("L") || choice.equals("l")) {
            currentTown.lookForTrouble();
        }
        else if (choice.equals("H") || choice.equals("h")) {
            if (choices.isEmpty()){
                System.out.println();
                System.out.println("Lmao u thought u could fool us?? U gotta travel before u can hunt for treasure again, buddy.");
            }
            else if (!choices.getFirst().equals("H") && choices.contains("M")) {

                System.out.println();

                int randomNum = currentTown.treasureHunt();

                if (randomNum <= 25 + luck) {
                    System.out.println("You got the rubies!");
                    if (!rubies) {
                        rubies = true;
                    } else {
                        System.out.println("You already have rubies though... :(");
                    }
                } else if (randomNum >= (luck) + 25 && randomNum <= luck + 25 + 25 + luck) {
                    System.out.println("You got the emeralds!!");
                    if (!emeralds) {
                        emeralds = true;
                    } else {
                        System.out.println("You already have emeralds though... :(");
                    }
                } else if (randomNum >= (2 * luck) + 50 && randomNum <= ((2 * luck) + 50) + 25 +luck) {
                    System.out.println("You got the opals!");
                    if (!opals) {
                        opals = true;
                    } else {
                        System.out.println("You already have opals though... :(");
                    }
                } else{
                    System.out.println("You got nothing :(");
                }


                if (rubies) {
                    if (emeralds) {
                        if (opals) {
                            System.out.println("Congratulations, you won!!!");
                            gameOver = true;
                        }
                    }
                }
            }


        }
        else if (choice.equals("C") || choice.equals("c")) {
            int beforeMoney = hunter.getGold();
            Scanner scan = new Scanner(System.in);
            System.out.println("How much do you wager?: ");
            int value = scan.nextInt();
            Scanner s = new Scanner(System.in);
            System.out.println("Choose a number between 1-12: ");
            int number = s.nextInt();
            currentTown.diceResult(number,value);
            moneyWonInCasino += (hunter.getGold() - beforeMoney);
            if (moneyWonInCasino >= 10){
                while (moneyWonInCasino > 10){
                    moneyWonInCasino -= 10;
                    luck += 2;
                }
            }
            else if (moneyWonInCasino <= -10){
                while (moneyWonInCasino < -10) {
                    luck -= 2;
                    moneyWonInCasino += 10;
                }
            }
            if (luck == -100){
                System.out.println("Your luck is so low that we've run out of ways to save u. Game over buddy.");
                gameOver = true;
            }
            if (luck == 100){
                System.out.println("You were so lucky that u must've been cheating so the gang that runs the casino is after u. bye bye.");
                gameOver = true;
            }
        }
        else if (choice.equals("X") || choice.equals("x"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }

    }
}
