// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// User class represents the human playing the game
// ===========================================================================

import java.lang.StringBuilder;
import java.util.ArrayList;

public class User extends Player {
	
	User() {
		this("Anonymus");
	}
	
	User(final String s) {
		this.name = s;
	}
	
	// Place ships for the player
	@Override
	public void setShips() {
		System.out.println(name + " is now placing ships.");
		System.out.println("----------- Grid Placement Menu -----------");
		promptInstructions();
		while (true) {
			AudioThread.setSong("Set Up.wav");
			String type = In.getString().toLowerCase();
			// Place a ship corresponding to the letter
			// Its important to first remove the ship if it exists
			// otherwise it would create a duplicate ship on the grid.
			if (type.equals("a")) {
				removeExistingShip(carrier);
				shipPlace(carrier);
			}
			else if (type.equals("b")) {
				removeExistingShip(battleship);
				shipPlace(battleship);
			}
			else if (type.equals("c")) {
				removeExistingShip(cruiser);
				shipPlace(cruiser);
			}
			else if (type.equals("s")) {
				removeExistingShip(sub);
				shipPlace(sub);
			}
			else if (type.equals("d")) {
				removeExistingShip(destroyer);
				shipPlace(destroyer);
			}
			else if (type.equals("g")) {
				friendGrid.printGrid();
			}
			else if (type.equals("q")) {
				String unplaced = this.unplacedShips();
				if (unplaced.isEmpty())
					break;
				else {
					System.out.println("To proceed to the next part, you must place all ships");
					System.out.println("The following ships have not been placed.\n: " + unplaced);
				}
					
			}
			else {
				System.out.println("Invalid input");
				promptInstructions();
			}
		}
		
	}
	// Runs the turn for the player, keeps looping until he enters a coordinate
	@Override
	public String runTurn() {
		String fireCord = new String();
		System.out.println("It is now " + this.name + "'s turn.");
		System.out.println("Enter (f) to print your board, enter (h) to print hostile board");
		while (true) {
			System.out.println("Enter any coordinate to continue");
			String input = In.getString().toUpperCase();
			if (input.toLowerCase().equals("f")) {
				System.out.println("  ---Friendly Grid---");
				this.friendGrid.printGrid();
			}
			else if (input.toLowerCase().equals("h")) {
				System.out.println("  ---Hostile Grid---");
				this.hostileGrid.printGrid();
			}
			else if (BattleShipGame.isValidCord(input)) {
				fireCord = input;
				break;
			}
			else {
				System.out.println("Invalid input");
			}
		}
		return fireCord;
	}
	
	// Function prompts the user to enter two coordinates which later places the whole ship on the grid
	private void shipPlace(Ship ship) {
		System.out.println("----------- Ship Placement Menu -----------");
		System.out.println("Enter two coordinates equal to the length of the ship, eg : (A3) (C3) would fit a 3 space ship");
		System.out.println("Enter (q) at any time to quit the current menu, press (g) to print the grid");
		ArrayList<String> cords = null;
		
		while (true) {
			System.out.println(ship.name() + " is " + ship.sizeOf() + " long.");
			String firstCord = getCord("Enter the first coordinate of your ship.");
			String secondCord = getCord("Enter the second coordinate of your ship.");
			if (firstCord.isEmpty() || secondCord.isEmpty()) // the player has quit, indicated by returned empty string
				break;
			Ship.Orientation ort = orientType(firstCord, secondCord);
			if (ort == Ship.Orientation.noOrt) {
				System.out.println("Coordinates does not create a vertical or horizontal ship, try again.");
				continue;
			}
			try {
				cords = Ship.coordinatesBetween(firstCord, secondCord, ort, ship.sizeOf());
				// this function will throw if the coordinates do not equal to the size of the ship
			} catch(Exception e) {
				System.out.println("Try Again, " + e.getMessage());
				continue;
			}
			if (!isIntercepted(cords)) {
				// Everything is ok, set the ship
				ship.setCords(cords);
				ship.setOrientation(ort);
				friendGrid.setShips(ship);
				System.out.println(ship.name() + " placed.");
				break;
			}
			else {
				System.out.println("Try Again, Current ship intercepts another ship.");
			}
		}
		
	}
	
	// Function will determine the orientation of the two strings, if they do not match to a orientation
	// noOrt will be default value.
	private Ship.Orientation orientType(final String s1, final String s2) {
		char c1 = s1.charAt(0), c2 = s2.charAt(0);
		int i1 = Integer.parseInt(s1.substring(1)), i2 = Integer.parseInt(s2.substring(1));
		return c1 == c2 && i1 != i2 ? Ship.Orientation.y : i1 == i2 && c1 != c2 ? Ship.Orientation.x : Ship.Orientation.noOrt;
	}
	
	// Function returns empty string if user enters 'q' indicating a quit.
	private String getCord(String mes) {
		String in = null;
		while (true) {
			System.out.println(mes);
			in = In.getString().toUpperCase();
			if (BattleShipGame.isValidCord(in)) {
				break;
			}
			else if (in.toLowerCase().equals("q")) {
				return new String();
			}
			else if (in.toLowerCase().equals("g")) {
				friendGrid.printGrid();
			}
			else {
				System.out.println(in + " is not a valid coordinate.");
			}
		}
		return in;
	}
	// Function returns every ship that has not yet been placed.
	private String unplacedShips() {
		StringBuilder str = new StringBuilder();
		if (this.carrier.isEmpty())
			str.append(carrier.name() + " ");
		if (battleship.isEmpty())
			str.append(battleship.name() + " ");
		if (cruiser.isEmpty())
			str.append(cruiser.name() + " ");
		if (sub.isEmpty())
			str.append(sub.name() + " ");
		if (destroyer.isEmpty())
			str.append(destroyer.name() + " ");
		
		if (str.length() > 1) { // remove the extra space at the end if it exists.
			if (str.charAt(str.length() -1) == ' ')
				str.deleteCharAt(str.length() -1);
		}
		return str.toString();
	}
	
	
	private void promptInstructions() {
		System.out.println("To proceed you must enter in all ship types.");
		System.out.println("The ship types are :");
		System.out.println("Aircraft Carrier - (a)[5], Battleship - (b)[4], Cruiser - (c)[3]");
		System.out.println("Submarine - (s)[3], Destroyer - (d)[2]");
		System.out.println("Enter the character in the parentheses to place the ship, the number in square brackets indicate the length.");
		System.out.println("To view the grid, enter (g), To quit enter (q)");
	}
	
	// Removes the ship given by the function
	private void removeExistingShip(Ship ship) {
		if (!ship.isEmpty()) {
			friendGrid.removeShip(ship);
			String name = ship.name();
			int size = ship.sizeOf();
			ship.resetShip(name, size);
		}
	}
}
