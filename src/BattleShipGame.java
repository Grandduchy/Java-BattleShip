// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// BattleShipGame is where everything is connected into the game
// It provides all interfaces for the user playing and is where the program is ran
// ===========================================================================

public class BattleShipGame {
	public static boolean betterChars = false;
	public static boolean allowColour = false;
	public static boolean allowMusic = true;
	
	public static void main(String[] args) {
		AudioThread.initSound(); // start the sound
		System.out.println("Welcome to BattleShip!");
		while (true) {
			AudioThread.setSong("Title Screen.wav");
			System.out.println("------------------ Menu ------------------");
			System.out.println("(q) to quit.");
			System.out.println("(a) to play against AI.");
			System.out.println("(r) to play against Smart Ai");
			System.out.println("(p) to play against another player.");
			System.out.println("(s) for settings");
			System.out.println("(h) for help and instructions");
			String in = In.getString().toLowerCase();
			if (in.equals("q"))
				break;
			else if (in.equals("p"))
				userMenu();
			else if (in.equals("a"))
				aiMenu();
			else if (in.equals("r"))
				smartAiMenu();
			else if (in.equals("h"))
				printRules();
			else if (in.equals("s"))
				settingsMenu();
			else
				System.out.println("Invalid input.");
		}
		AudioThread.setSong("Ending.wav");
		try {
			System.out.print("Created by : ");
			printIfBetter("Joshua Challenger", "Joshua Challenger", "\\u001B[4m");
			System.out.print("\n");
			Thread.sleep(5000);
			System.out.println("Credit to Nintendo's NES and Gameboy Battleship Music");
			Thread.sleep(5000);
			System.out.println("Credit to http://www.freesfx.co.uk/ for the sound effects");
			Thread.sleep(5000);
			System.out.println("Thanks for playing!");
			Thread.sleep(7500);
		} catch(Exception e) {}
		finally {
			AudioThread.terminate();
		}
	}
	
	public static void initUserGame(User p1, User p2) {
		p1.setShips();
		p2.setShips();
		AudioThread.enableMainSound();
		runMainGame(p1, p2);
		AudioThread.disableMainSound();
		AudioThread.setSong("Win.wav");
		printGrids(p1, p2);
	}
	
	public static void initSimpleAiGame(User p1, SimpleAi p2) {
		p1.setShips();
		p2.setPlayerShips(p1);
		p2.setShips();
		AudioThread.enableMainSound();
		runMainGame(p1, p2);
		AudioThread.disableMainSound();
		if (p1.isDestroyed())
			AudioThread.setSong("Lose.wav");
		else
			AudioThread.setSong("Win.wav");
		printGrids(p1, p2);
		
	}
	
	// Smart Ai game is slightly different than simpleAiGame as 
	// this Ai needs to call extra functions inside the running game for it to operate
	public static void initSmartAiGame(User p1, SmartAi p2) {
		p1.setShips();
		p2.setShips();
		AudioThread.enableMainSound();
		loop:
		while (true) {
			String userCord = p1.runTurn();
			boolean hasHitAi = p2.setHit(p1.name(), userCord);
			registerShot(p1, hasHitAi);
			AudioThread.setHitEffect(hasHitAi);
			waitForSound(1000); // need to wait for the sound to catch up to the next one
			p1.setHitToGrid(hasHitAi, userCord);
			if (p2.isDestroyed()) {
				System.out.println(p1.name() + " has Won!");
				break loop;
			}
			String aiCord = p2.runTurn();
			boolean hasHitp1 = p1.setHit(p2.name(), aiCord);
			registerShot(p2, hasHitp1);
			p2.didHitLastShot(hasHitp1);
			AudioThread.setHitEffect(hasHitp1);
			p2.setHitToGrid(hasHitp1, aiCord);
			if (p1.isDestroyed()) {
				System.out.println(p2.name() + " has Won!");
				break loop;
			}
		}
		AudioThread.disableMainSound();
		if (p1.isDestroyed())
			AudioThread.setSong("Lose.wav");
		else
			AudioThread.setSong("Win.wav");
		printGrids(p1, p2);
	}
	
	private static void runMainGame(Player p1, Player p2) {
		loop:
		while(true) {
			String p1Cord = p1.runTurn(); // Get the coordinate the player chooses
			boolean hasHitp2 = p2.setHit(p1.name(), p1Cord); // determine if it hit the other player and register it on the ships
			registerShot(p1, hasHitp2);
			AudioThread.setHitEffect(hasHitp2); // create a sound effect if missed/hit
			waitForSound(1000); // wait for the sound to catch up
			p1.setHitToGrid(hasHitp2, p1Cord); // set the hit on the player's hostile grid
			if (p2.isDestroyed()) { // check if the second player is destroyed
				System.out.println(p1.name() + " has Won!");
				break loop;
			}
			String p2Cord = p2.runTurn();
			boolean hasHitp1 = p1.setHit(p2.name(), p2Cord);
			registerShot(p2, hasHitp1);
			AudioThread.setHitEffect(hasHitp1);
			p2.setHitToGrid(hasHitp1, p2Cord);
			if (p1.isDestroyed()) {
				System.out.println(p2.name() + " has Won!");
				break loop;
			}
		}
	}
	
	// Prints the hostile and friendly grids of both sides to display.
	public static void printGrids(Player p1, Player p2) {
		System.out.println((p1.shots() + p2.shots()) + " shots were fired.");
		System.out.println(p1.name() + " fired " + p1.shots() + " hit " + p1.hits() + " for an accuracy of " + p1.accuracy() + "%");
		System.out.println(p2.name() + " fired " + p2.shots() + " hit " + p2.hits() + " for an accuracy of " + p2.accuracy() + "%");
		while (true) {
			System.out.println("Printing commands = (1f) (1h) (2f) (2h)\n Enter any other key to quit");
			String in = In.getString();
			if (in.equalsIgnoreCase("1f"))
				p1.printfriendGrid();
			else if (in.equalsIgnoreCase("1h"))
				p1.printHostileGrid();
			else if (in.equalsIgnoreCase("2f"))
				p2.printfriendGrid();
			else if (in.equalsIgnoreCase("2h"))
				p2.printHostileGrid();
			else
				break;
		}
	}
	// Determines if the coordinate is valid, is valid if
	// Only has one character between A-J and IS uppercase
	// Has one number between 1-9 OR has a 10.
	public static boolean isValidCord(final String s) {
		boolean a = true;
		try {
			if (s.length() != 2) {
				if (s.length() == 3)
					a = true;
				else
					a = false;
			}
			if (!Character.isUpperCase(s.charAt(0)))
				a = false;
			if (Integer.parseInt(s.substring(1)) > 10)
				a = false;
			if (!isValidChar(s.charAt(0)))
				a = false;
		}
		catch (Exception e){ a = false;}
		return a;
	}
	
	// true if ABCDEFGHJ
	public static boolean isValidChar(final char s) {
		return (s - 'A') < 10;
	}
	// converts a character to an integer where the start is the ASCII value of 'A'
	// entering 'A' would give 0
	public static int charToInt(final char c) {
		return c - '\100';
	}
	// converts an integer to a character where 'A' is 0 and so on.
	public static char intToChar(final int i) {
		return (char)((int)'A' + i);
	}
	
	public static void printType(final Grid.Type type) {
		// Use ANSCI codes if possible, determined if it will use if allowColour and betterChars is true
		if (type == Grid.Type.water) 
			printIfBetter("~","\uD83C\uDF0A", "\u001B[34m");
		else if (type == Grid.Type.vertShip) 
			printIfBetter("#","\u2585", "\u001B[37m");
		else if (type == Grid.Type.hortShip) 
			printIfBetter("#", "\u2588", "\u001B[37m");
		else if (type == Grid.Type.hitmarker) 
			printIfBetter("*", "\uD83D\uDD25", "\u001B[31m");
		else if (type == Grid.Type.nohit) 
			printIfBetter("O", "O", "");
		else 
			System.out.print('?');
	}
	
	public static void printIfBetter(String original, String better, String colour) {
		if (allowColour)
			System.out.print(colour);
		
		if (betterChars)
			System.out.print(better);
		else
			System.out.print(original);
		
		if (allowColour)
			System.out.print("\u001B[0m"); // <-- resets
	}
	
	public static void printRules() {
		System.out.println("The object of Battleship is to try and sink all of the other player's before they sink all of your ships.");
		System.out.println("All of the other player's ships are somewhere on his/her board.");
		System.out.println("You try and hit them by entering the coordinates of one of the squares on the board.");
		System.out.println("Neither you nor the other player can see the other's board so you must try to guess where they are.");
		System.out.println("Each player places the 5 ships somewhere on their board. The ships can only be placed vertically or horizontally");
		System.out.println("Once the guessing begins, the players may not move the ships.");
		System.out.println("As soon as all of one player's ships have been sunk, the game ends.");
		System.out.println("A character in parentheses is a character you can enter to indicate your choice");
		
		System.out.println("--------Grid Types--------");
		if (betterChars) {
			System.out.println("Water : \uD83C\uDF0A\t Horizontal Ship : \u2588\t Vertical Ship : \u2585");
			System.out.println("Hit/HitMarker : \uD83D\uDD25\t Missed Target : O");
		}
		else {
			System.out.println("Water : ~\t Ship : #");
			System.out.println("Hit/HitMarker : *\t Missed Target : O");
		}
	}
	
	public static void settingsMenu() {
		System.out.println("---------- Settings ----------");
		System.out.println("Note - Better Characters and Colour is dependent on the console, an unsupported console will print indistinct characters.");
		System.out.println("If this \uD83D\uDD25 looks weird, better characters are not supported, try enabling UTF-8");
		System.out.println("If \u001B[34m this \u001B[0m looks weird, colors are not supported");
		while (true) {
			System.out.println("Better Characters : " + betterChars + " (b) to toggle");
			System.out.println("Music : " + allowMusic + " (m) to toggle");
			System.out.println("Colour : " + allowColour + " (c) to toggle");
			System.out.println("Enter (q) to exit.");
			String input = In.getString().toLowerCase();
			if (input.equals("b"))
				betterChars = !betterChars;
			if (input.equals("m")) {
				allowMusic = !allowMusic;
				if (allowMusic)
					AudioThread.initSound();
			}
			if (input.equals("c"))
				allowColour = !allowColour;
			if (input.equals("q"))
				break;
		}
	}
	
	private static void userMenu() {
		System.out.println("Player 1, enter your name : ");
		String name1 = In.getString().trim();
		if (name1.isEmpty())
			name1 = getRandomName();
		System.out.println("Player 2, enter your name : ");
		String name2 = In.getString().trim();
		if (name2.isEmpty()) {
			do {
				name2 = getRandomName();
			}while(name1.equals(name2));
		}
		initUserGame(new User(name1), new User(name2));
		
	}
	
	private static void smartAiMenu() {
		System.out.println("Smart Ai is a more fair ai that try to target your ships without 'chances' to hit.");
		System.out.println("Player 1, enter your name : ");
		String name = In.getString();
		if (name.isEmpty())
			name = getRandomName();
		String aiName = "";
		do {
			aiName = getRandomName();
		} while (aiName.equals(name));
		initSmartAiGame(new User(name), new SmartAi(aiName));
	}
	
	private static void aiMenu() {
		System.out.println("Player 1, enter your name : ");
		String name = In.getString();
		if (name.isEmpty())
			name = getRandomName();
		String aiName = "";
		do {
			aiName = getRandomName();
		} while(aiName.equals(name));
		int diffuculty = 0;
		while (true) {
			System.out.println("Enter an Ai Diffuculty :");
			System.out.println("Percentage shows how likely an Ai will hit your ship.");
			System.out.println("Kindergareten (k) 10%");
			System.out.println("Casual (c) 30%");
			System.out.println("Passable (p) 50%");
			System.out.println("Ultra-violence (u) 75%");
			System.out.println("Nightmare (n) 90%");
			System.out.println("Godlike (g) 100%");
			System.out.println("Enter your own (e)");
			String input = In.getString().toLowerCase();
			if (input.equals("k")) {
				diffuculty = 10; break;
			}
			else if (input.equals("c")) {
				diffuculty = 30; break;
			}
			else if (input.equals("p")) {
				diffuculty = 50; break;
			}
			else if (input.equals("u")) {
				diffuculty = 75; break;
			}
			else if (input.equals("n")) {
				diffuculty = 90; break;
			}
			else if (input.equals("g")) {
				diffuculty = 100; break;
			}
			else if (input.equals("e")) {
				System.out.println("Enter an integer between 100-0");
				diffuculty = getPosInt(); break;
			}
			else
				System.out.println("Invalid input, try again.");
		}
		initSimpleAiGame(new User(name), new SimpleAi(aiName, diffuculty));
	}
	// Function gets a positive integer in the range of 0-100
	public static int getPosInt() {
		int x = 0;
		while (true) {
			String s = In.getString();
			s = s.trim();
			try {
				x = Integer.parseInt(s);
				if (x < 0)
					throw new RuntimeException("Value must be equal to or greater than 0.");
				else if (x > 100)
					throw new RuntimeException("Value must be less than or equal to 100");
				break;
			} catch (NumberFormatException e) {
				System.out.println("An integer or real number was not entered, " + e.getMessage() + ", Try Again.");
			} catch (RuntimeException e) {
				System.out.println("Invalid number was entered, " + e.getMessage() + ", Try Again.");
			}

		}
		return x;
	}
	
	private static String getRandomName() {
		String names[] = { "The Flood", "Captain Blackbeard", "Black Barty", "El Draque", "Raleigh", "William Kidd",
				"Captain Cook", "Long John Silver", "The Hook", "Conrad",
				"One Eyed Cooper", "Cheng I Sao", "Captain Jack Sparrow", "Mr. Computer"};
		
		return names[(int)(Math.random() * names.length)]; 
	}
	// Function makes the thread sleep for a few seconds
	// Needed as sound effects will overwrite each other if played multiple times quickly.
	private static void waitForSound(int mili) {
		try {
			Thread.sleep(mili);
		}catch(Exception e) {}
	}
	
	private static void registerShot(Player p, boolean didHit) {
		if (didHit)
			p.registerHit();
		else
			p.registerShot();
	}
	
}
