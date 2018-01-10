// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Ai represents an abstract interface of which future child Ai types will use
// Ai provides random functions, and ship placing functions the Ai needs
// ===========================================================================


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public abstract class Ai extends Player {
	
	public abstract void setShips();
	
	public abstract String runTurn();
	
	// Function returns a random coordinate from the board.
	protected static String randomCord() {
		int x = (int)(Math.random() * 10);
		int y = (int)(Math.random() * 10) + 1;
		
		String s = String.valueOf(BattleShipGame.intToChar(x));
		s = s + String.valueOf(y);
		return s;
	}
	
	// Given a coordinate an x or y direction and a length, this function will create a coordinate
	// from size distance away from the original coordinate in the direction of ort, randomized where 
	// the cord is in the forward or reverse direction.
	protected static String randomRelativeCord(String firstCord, Ship.Orientation ort, final int size) {
		String otherCord = null;

		if (ort == Ship.Orientation.x) {// Horizontal 
			final int y = Integer.parseInt(firstCord.substring(1));
			int x = BattleShipGame.charToInt(firstCord.charAt(0));
			if (x + size > 10) { // the forward direction is out of bounds
				x -= size - 1;
			}
			else if (x - size < 0) // the backward direction is out of bounds
				x += size - 1;
			else // randomly decide if it should go in the forward or backward direction
				x = (int)(Math.random() * 2) + 1 == 1 ? x + size - 1 : x - size + 1;
			char n = (char)((int)'@' + x);
			otherCord = String.valueOf(n) + String.valueOf(y);
		}
		else {// Vertical
			final int x = BattleShipGame.charToInt(firstCord.charAt(0));
			int y = Integer.parseInt(firstCord.substring(1));
			if (y + size > 10) 
				y -= size - 1;
			else if (y - size < 0) 
				y += size - 1;
			else 
				y = (int)(Math.random() * 2) + 1 == 1 ? y - size + 1 : y + size - 1;
			otherCord = String.valueOf(BattleShipGame.intToChar(x - 1)) + String.valueOf(y);
		}
		return otherCord;
	}
	
	protected static Ship.Orientation randomOrientation() {
		return (int)(Math.random() *2) + 1  == 1 ? Ship.Orientation.x : Ship.Orientation.y;
	}
	
	// Function sets Ai's ships randomly
	protected void setShip(Ship ship) {
		Ship.Orientation ort = randomOrientation();
		String cord1 = null, cord2 = null;
		ArrayList<String> coordinates = null;
		while(coordinates == null) {
			cord1 = randomCord();
			cord2 = randomRelativeCord(cord1, ort, ship.sizeOf());
			try {
				ArrayList<String> cords = Ship.coordinatesBetween(cord1, cord2, ort, ship.sizeOf()); // this function can throw
				// check if the coordinates intercepts any other ship and
				// checks if the coordinates are atleast one tile away from each ship
				if (!this.isIntercepted(cords) && !isCloseToOthers(cords)) 
					coordinates = cords;
			} catch(Exception e) {
				System.out.println("Ai malfunction between coordinates");
				System.out.println("Cord1 : " + cord1 + " Cord2 : " + cord2 + " Ship type : " + ship.name() + " ship size : " + ship.sizeOf() + " orientation : " + (ship.getOrientation() == Ship.Orientation.x ? "X" : "Y"));
			}
		}
		ship.setCords(coordinates);
		ship.setOrientation(ort);
		friendGrid.setShips(ship);
	}
	// Function determines if a set of coordinates is atleast one tile away from all other ships
	// does this by taking each coordinate around each ship coordinate and checking if those coordinates equal other ships' coordinates
	protected boolean isCloseToOthers(List<String> shipCords) {
		Vector<String> closeCords = new Vector<String>();
		for (String shipCord : shipCords) {
			ArrayList<String> newCords = closeCords(shipCord);
			for (String closeCord : newCords) {
				closeCords.add(closeCord);
			}
		}
		// remove the original ships' coordinates
		for (String shipCord : shipCords) {
			int index = closeCords.indexOf(shipCord);
			if (index != -1) {
				closeCords.remove(index);
			}
		}
		
		return this.isIntercepted(closeCords);
	}
	// Function returns every possible coordinate around an original coordinate
	private static ArrayList<String> closeCords(String cord) {
		char x = cord.charAt(0);
		int y = Integer.parseInt(cord.substring(1));
		ArrayList<String> closeCords = new ArrayList<String>();
		boolean canGoUp = y > 1, canGoDown = y < 10, 
				canGoLeft = BattleShipGame.charToInt(x) > 1, canGoRight = BattleShipGame.charToInt(x) < 10;
		if (canGoUp) 
			closeCords.add(String.valueOf(x) + (y - 1));
		if (canGoDown) 
			closeCords.add(String.valueOf(x) + (y + 1));
		if (canGoLeft) 
			closeCords.add(String.valueOf((char)(x - 1)) + y);
		if (canGoRight) 
			closeCords.add(String.valueOf((char)(x + 1)) + y);
		if (canGoUp && canGoLeft)
			closeCords.add(String.valueOf((char)(x - 1)) + (y - 1));
		if (canGoUp && canGoRight)
			closeCords.add(String.valueOf((char)(x + 1)) + (y - 1));
		if (canGoDown && canGoLeft)
			closeCords.add(String.valueOf((char)(x - 1)) + (y + 1));
		if (canGoDown && canGoRight)
			closeCords.add(String.valueOf((char)(x + 1)) + (y + 1));
		
		return closeCords;
	}
	
	protected void setAiShips() {
		setShip(this.battleship);
		setShip(this.carrier);
		setShip(this.cruiser);
		setShip(this.destroyer);
		setShip(this.sub);
	}
	
}
