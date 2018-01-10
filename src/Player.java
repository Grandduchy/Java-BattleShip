// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Player is an abstract interface that each kind of player possible of playing the game needs to inherit from.
// The class provides printing functions, debugger functions, ship placing functions, and general
// functions needed for it to operate.
// ===========================================================================

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public abstract class Player {

	public abstract void setShips();
	
	public abstract String runTurn();
	// Writes to the hostile grid a hit marker or a missed shot or a coordinate.
	public void setHitToGrid(final boolean didHit, final String cord) {
		if (didHit) {
			hostileGrid.setTile(cord, Grid.Type.hitmarker);
		}
		else {
			hostileGrid.setTile(cord, Grid.Type.nohit);
		}
	}
	
	// Function sets a hit to a ship if it hit and prints out a message, boolean returned determines if the shot actually hit a ship.
	public boolean setHit(final String otherName, final String cord) {
		boolean didHit = false;
		if (carrier.canHit(cord)) {
			carrier.hit(cord);
			System.out.println(otherName + " hit " + this.name + "'s ship on " + cord + "!");
			didHit = true;
		}
		else if (battleship.canHit(cord)) {
			battleship.hit(cord);
			System.out.println(otherName + " hit " + this.name + "'s ship on " + cord + "!");
			didHit = true;
		}
		else if (cruiser.canHit(cord)) {
			cruiser.hit(cord);
			System.out.println(otherName + " hit " + this.name + "'s ship on " + cord + "!");
			didHit = true;
		}
		else if (sub.canHit(cord)) {
			sub.hit(cord);
			System.out.println(otherName + " hit " + this.name + "'s ship on " + cord + "!");
			didHit = true;
		}
		else if (destroyer.canHit(cord)) {
			destroyer.hit(cord);
			System.out.println(otherName + " hit " + this.name + "'s ship on " + cord + "!");
			didHit = true;
		}
		else {
			System.out.println(otherName + " missed on " + cord + ".");
			didHit = false;
		}
		if (didHit) 
			friendGrid.setTile(cord, Grid.Type.hitmarker);
		else
			friendGrid.setTile(cord, Grid.Type.nohit);
		return didHit;
	}
	// Function returns if each ship is destroyed
	public boolean isDestroyed() {
		return carrier.isDestroyed() && battleship.isDestroyed() && cruiser.isDestroyed()
				&& sub.isDestroyed() && destroyer.isDestroyed();
	}
	
	// Function is mostly used for debugging, where each ship is already placed, ready for manipulation.
	public void debuggerShipPlace() {
		ArrayList<String> asd = new ArrayList<String>();
		asd.add("A1"); asd.add("A2"); asd.add("A3"); asd.add("A4"); asd.add("A5");
		this.carrier.setCords(asd);
		this.carrier.setOrientation(Ship.Orientation.y);
		friendGrid.setShips(this.carrier);
		
		ArrayList<String> fng = new ArrayList<String>();
		fng.add("B1"); fng.add("B2"); fng.add("B3"); fng.add("B4");
		this.battleship.setCords(fng);
		this.battleship.setOrientation(Ship.Orientation.y);
		friendGrid.setShips(this.battleship);
		
		ArrayList<String> lop = new ArrayList<String>();
		lop.add("C1"); lop.add("C2"); lop.add("C3");
		this.cruiser.setCords(lop);
		this.cruiser.setOrientation(Ship.Orientation.y);
		friendGrid.setShips(this.cruiser);
		
		ArrayList<String> bgn = new ArrayList<String>();
		bgn.add("D1"); bgn.add("D2"); bgn.add("D3");
		this.sub.setCords(bgn);
		this.sub.setOrientation(Ship.Orientation.y);
		friendGrid.setShips(this.sub);
		
		ArrayList<String> ggg = new ArrayList<String>();
		ggg.add("E1"); ggg.add("E2");
		this.destroyer.setCords(ggg);
		this.destroyer.setOrientation(Ship.Orientation.y);
		friendGrid.setShips(this.destroyer);
	}
	
	public void printfriendGrid() {
		friendGrid.printGrid();
	}
	
	public void printHostileGrid() {
		hostileGrid.printGrid();
	}
	
	public String name() {
		return this.name;
	}
	// Function checks if the list given intercepts any of the coordinates
	protected boolean isIntercepted(List<String> list) {
		Vector<String> allcords = new Vector<String>(10);
		allcords.addAll(Arrays.asList(carrier.getCords()));
		allcords.addAll(Arrays.asList(battleship.getCords()));
		allcords.addAll(Arrays.asList(cruiser.getCords()));
		allcords.addAll(Arrays.asList(sub.getCords()));
		allcords.addAll(Arrays.asList(destroyer.getCords()));
		
		boolean isEqual = false;
		
		for (String item : list) {
			for (String otherCord : allcords) {
				if (item.equals(otherCord)) {
					isEqual = true;
				}
			}
		}
		
		return isEqual;
	}
	
	
	public String name; // Name of the player
	protected FriendGrid friendGrid = new FriendGrid();
	protected EnemyGrid hostileGrid = new EnemyGrid();
	protected final Ship carrier = new Ship("Aircraft Carrier", 5);
	protected final Ship battleship = new Ship("Battleship", 4);
	protected final Ship cruiser = new Ship("Cruiser", 3);
	protected final Ship sub = new Ship("Submarine", 3);
	protected final Ship destroyer = new Ship("Destroyer", 2);
	
}
