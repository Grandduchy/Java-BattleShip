// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// SmartAi tries to battle without already knowing where the ships already are
// Ai works by first splitting the grid in half as a checker board
// Ai then uses modes to determine what to do where :
// Hunt Mode : Finding and Searching for enemy ships
// Search Mode : Finding the correct orrientation of the ship to know which direction to fire
// Destroy Mode : Destroying the ship
// End Hunt Mode : A rare mode where Ai will fire on all remaining spots not used, Ai will almost never use this mode.
// ===========================================================================
import java.util.Collections;
import java.util.Vector;

public class SmartAi extends Ai {
	
	SmartAi(String name) {
		this.name = name;
		// From a board get all coordinates of one 'color' while splitting the board.
		for (int y = 1; y != 11; y++) {
			for (int x = (y % 2 == 0 ? 0 : 1); x < 10; x += 2) {
				possibleCords.add(String.valueOf(BattleShipGame.intToChar(x)) + y);
			}
		}
		Collections.shuffle(possibleCords);
		// Get all coordinates of the other color.
		for (int y = 1; y != 11; y++) {
			for (int x = (y % 2 != 0 ? 0 : 1); x < 10; x+=2) {
				otherCords.add(String.valueOf(BattleShipGame.intToChar(x)) + y);
			}
		}
		Collections.shuffle(otherCords);
	}
	
	@Override
	public void setShips() {
		this.setAiShips();
	}

	@Override
	public String runTurn() {
		
		// Test if we need to go into end hunt mode
		if (possibleCords.isEmpty() && !isEndHuntMode) {
			reset();
			isHuntMode = false;
			isEndHuntMode = true;
		}
		
		if (isHuntMode || isEndHuntMode) {
			lastCord = getCord();
		}
		else if (isSearchMode){
			String coordinate = null;
			// Get a non out of bounds coordinate 
			while (coordinate == null) {
				coordinate = aroundCords[searchModeIter];
				if (coordinate == null)
					searchModeIter++;
			}
			lastCord = coordinate;
		}
		else {// its in destroy mode
			String coordinate = cordFromIter(lastCord, searchModeIter);
			if (coordinate == null && isReverse) { // we've completely destroyed the ship and hit a wall
				reset();
				lastCord = getCord();
			}
			else if (coordinate == null) { // hit wall in forward direction
				isReverse = true;
				changeIterDirection();
				lastCord = cordFromIter(originalHit, searchModeIter);
				if (lastCord == null) { // ship is destroyed and hit a wall in the reverse direction
					reset();
					lastCord = getCord();
				}
			} else {
				lastCord = coordinate;
			}
		}
		
		
		possibleCords.remove(lastCord);
		otherCords.remove(lastCord);
		return lastCord;
	}
	
	
	
	public void didHitLastShot(boolean didHit) {
		
		if (didHit && isHuntMode) { // we just hit the ship the first time
			originalHit = lastCord;
			isHuntMode = false;
			isSearchMode = true;
			aroundHitCord();
		}
		else if (!didHit && isSearchMode) { // No ship on that side, try on another side
			++searchModeIter;
		}
		else if (didHit && isSearchMode) { // we are finding the correct orientaiton of the ship
			isSearchMode = false;
			isDestroyMode = true;
		}
		else if (!didHit && isDestroyMode) { //end of tail or head
			if (isReverse) { // we've destroyed the ship
				reset();
			}
			else { // we reached a tail or head
				changeIterDirection();
				isReverse = true;
				lastCord = originalHit;
			}
		}
		// we ignore if the ai is in the ending hunt mode
	}
	
	private void changeIterDirection() {
		searchModeIter = searchModeIter <= 1 ? searchModeIter + 2 : searchModeIter - 2;
	}
	
	private void aroundHitCord() {
		aroundCords = new String[4]; // Note the representation of the array is clockwise
		aroundCords[0] = upCord(originalHit);
		aroundCords[1] = rightCord(originalHit);
		aroundCords[2] = downCord(originalHit);
		aroundCords[3] = leftCord(originalHit);
	}
	
	// From a coordinate get a coordinate directly up towards it
	// up, down, left, right cord functions all return null if its a wall
	private static String upCord(String cord) {
		char x = cord.charAt(0);
		int y = Integer.parseInt(cord.substring(1));
		boolean canGoUp = y > 1;
		if (canGoUp)
			return String.valueOf(x) + (y - 1);
		return null;
	}
	
	private static String downCord(String cord) {
		char x = cord.charAt(0);
		int y = Integer.parseInt(cord.substring(1));
		boolean canGoDown = y < 10;
		if (canGoDown)
			return String.valueOf(x) + (y + 1);
		return null;
	}
	
	private static String leftCord(String cord) {
		char x = cord.charAt(0);
		int y = Integer.parseInt(cord.substring(1));
		boolean canGoLeft = BattleShipGame.charToInt(x) > 1;
		if (canGoLeft)
			return String.valueOf((char)(x - 1)) + y;
		return null;
	}
	
	private static String rightCord(String cord) {
		char x = cord.charAt(0);
		int y = Integer.parseInt(cord.substring(1));
		boolean canGoRight = BattleShipGame.charToInt(x) < 10;
		if (canGoRight)
			return String.valueOf((char)(x + 1)) + y;
		return null;
	}
	
	// Takes a clockwise iterator and returns the relative coordinate from the coordinate
	private String cordFromIter(String cord, int iter) {
		String s = null;
		switch (iter) {
		case 0 :
			s = upCord(cord);
			break;
		case 1 :
			s = rightCord(cord);
			break;
		case 2 :
			s = downCord(cord);
			break;
		case 3 :
			s = leftCord(cord);
			break;
		}
		return s;
	}
	
	private String getCord() {
		return !possibleCords.isEmpty() ? possibleCords.firstElement() : otherCords.firstElement(); 
	}
	
	private void reset() {
		originalHit = new String();
		searchModeIter = 0;
		aroundCords = null;
		isReverse = false;
		isDestroyMode = false;
		isSearchMode = false;
		isHuntMode = true;
		isEndHuntMode = false;
	}
	
	private int searchModeIter = 0; // In search mode iterator will move clockwise around a coordinate while keeping track 
	private String aroundCords[]; // In search mode represents the value of a relative clockwise coordinate
	private boolean isReverse = false; // in destroy mode, determines if we go back tail or head.
	private boolean isDestroyMode = false;
	private boolean isSearchMode = false;
	private boolean isHuntMode = true;
	private boolean isEndHuntMode = false;
	private String originalHit; // represents the first hit on a ship
	private String lastCord; // represents the last shot the ai took
	private Vector<String> possibleCords = new Vector<String>(50); // represents a color of a board
	private Vector<String> otherCords = new Vector<String>(50); // represents the other color of a board
	// note otherCords will almost never be used.
}
