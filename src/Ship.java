// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Ship represents any kind of generic ship type
// ===========================================================================

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.RuntimeException;

public class Ship {
	
	
	enum Orientation {
		noOrt, x, y
	}
	
	Ship(String name, int sz) {
		this.name = name;
		this.size = sz;
		this.timesHit = 0;
		this.holes = new ArrayList<Pair<String,Boolean>>(sz);
	}
	// sets the coordinates of the ship
	public void setCords(List<String> cords) {
		if (cords.size() != this.size)
			throw new RuntimeException("Amount of coordinates exceeds length of ship");
		for (Iterator<String> it = cords.iterator(); it.hasNext();) {
			String x = it.next();
			if (BattleShipGame.isValidCord(x))
				holes.add(new Pair<String, Boolean>(x, false));
			else
				throw new RuntimeException("A coordinate passed is not valid.");
		}
	}
	
	
	public int sizeOf() {
		return size;
	}
	
	public String name() {
		return this.name;
	}
	 
	public void setName(String s) {
		this.name = s;
	}
	
	public Boolean isDestroyed() {
		return this.timesHit == size || this.isEmpty();
	}
	// Checks if the coordinate can hit any of the ship's coordinates
	public boolean canHit(String cords) {
		boolean b = false;
		for (Pair<String, Boolean> p : holes) {
			if (cords.equals(p.first)) {
				b = true;
			}
		}
		return b;
	}
	// Checks if the coordinate has already been hit
	public boolean alreadyHit(String cords) {
		boolean b = false;
		for (Pair<String, Boolean> p : holes) {
			if (cords.equals(p.first) && p.second)
				b = true;
		}
		return b;
	}
	
	public boolean isEmpty() {
		return this.holes.isEmpty();
	}
	
	// Hits the ship at the coordinate
	public void hit(String cords) {
		if (canHit(cords)) {
			for (Pair<String,Boolean> p : holes) {
				if (cords.equals(p.first) && !p.second ) {
					p.second = new Boolean(true);
					timesHit++;
				}
			}
		}
	}
	// prints to console each coordinate of the ship
	public void printCords() {
		for (final Pair<String, Boolean> p : holes) {
			System.out.println("Cord : " + p.first + " was hit : " + p.second);
		}
	}
	// function returns if the ship has been hit atleast once.
	public boolean isHit() { 
		for (Pair<String, Boolean> spot : holes) {
			if (spot.second)
				return true;
		}
		return false;
	}
	// returns each coordinate of the ship
	public String[] getCords() {
		String cords[] = new String[holes.size()];
		for (int i = 0; i != cords.length; i++) {
			cords[i] = holes.get(i).first;
		}
		return cords;
	}
	// modifies the current ship completely reseting all variables of the ship object
	public void resetShip(String name, int sz) {
		this.name = name;
		this.size = sz;
		this.timesHit = 0;
		this.holes = new ArrayList<Pair<String,Boolean>>(sz);
	}
	
	public void setOrientation(Orientation o) {
		this.orientation = o;
	}
	
	public Orientation getOrientation() {
		return this.orientation;
	}
	
	// Returns every coordinate between two coordinates.
	// In a coordinate either the number of the character will be constant
	// If character is changing, its in the horizontal direction, and each coordinate between is the alphabetical order of the two
	// If the number is changing, its in the vertical direction, and coordinates between is the changing number
	public static ArrayList<String> coordinatesBetween(String a, String b, Ship.Orientation o, int size) {
		ArrayList<String> cords = new ArrayList<String>(size);
		
		if (o == Ship.Orientation.noOrt) throw new RuntimeException("Orientation was not filled");
		else if (o == Ship.Orientation.x) {
			if (!a.substring(1).equals(b.substring(1))) // check if orientation is in horizontal
				throw new RuntimeException("Orientation is horizontal but numbers are not on the same level.");
			// get the largest and smallest characters as integers
			int large = BattleShipGame.charToInt(a.charAt(0)) > BattleShipGame.charToInt(b.charAt(0)) ? a.charAt(0) : b.charAt(0);
			int small = large == a.charAt(0) ? b.charAt(0) : a.charAt(0);
			
			if (large - small + 1 != size)
				throw new RuntimeException("Coordinates given do not equal the size of the ship.");
			
			// iterate through the large and small and add the constant with the changing variable
			for (int iter = small; iter != large + 1; iter++) {
				char x = BattleShipGame.intToChar(iter - 'A');
				cords.add(String.valueOf(x) + a.substring(1));
			}
		}
		else {
			if (a.charAt(0) != b.charAt(0))
				throw new RuntimeException("Orientation is vertical, but letters are not on the same level.");
			// If one of the coordinates contain a 10, add the coordinate and make the coordinate to a 9
			// and do the original function.
			if (a.length() != 2) {
				cords.add(String.valueOf(a.charAt(0)) + 10);
				a = String.valueOf(a.charAt(0)) + 9;
				--size;
			}
			else if (b.length() != 2) {
				cords.add(String.valueOf(b.charAt(0)) + 10);
				b = String.valueOf(a.charAt(0)) + 9;
				--size;
			}
			
			int large = (a.charAt(1) > b.charAt(1) ? a.charAt(1) : b.charAt(1));
			int small = (large == a.charAt(1) ? b.charAt(1) : a.charAt(1));
			large -= '0'; small -= '0';
			
			
			if (large - small + 1 != size)
				throw new RuntimeException("Coordinates given do not equal the size of the ship.");
			
			for (int iter = small; iter != large + 1; iter++) {
				cords.add(String.valueOf(a.charAt(0)) + iter);
			}
			
		}
		return cords;
	}
	
	
	private Orientation orientation;
	private String name;
	private Integer size;
	private Integer timesHit;
	private ArrayList<Pair<String,Boolean>> holes; 
	
}