// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// FriendGrid is the grid where the player's ships are displayed.
// The grid displays the enemies shots and misses alongside with his/her ships.
// ===========================================================================

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FriendGrid extends Grid {
	
	FriendGrid() {
		// Constructor sets each value of grid to water type
		 for (int i = 0; i != Grid.y; i++) {
			ArrayList<Grid.Type> row = new ArrayList<Grid.Type>(Grid.x);
			for (int x = 0; x != Grid.x; x++) {
				row.add(Grid.Type.water);
			}
			grid.add(row);
		}
	}
	// Function sets ships to the grid
	public void setShips(Ship... s) {
		for (final Ship e : s) {
			ships.add(e);
			setShipToGrid(e);
		}
	}
	// Function sets ships to the grid
	public void setShips(List<Ship> list) {
		for (Iterator<Ship> it = list.iterator(); it.hasNext();) {
			Ship s = it.next();
			ships.add(s);
			setShipToGrid(s);
		}
	}
	// Function removes ships from the grid
	public void removeShip(Ship... s) {
		for (final Ship e : s) {
			if (ships.contains(e)) {
				ships.remove(e);
				removeShipGrid(e);
			}
		}
	}
	// Function removes ships from the grid
	public void removeShip(List<Ship> list) {
		for (Iterator<Ship> it = list.iterator(); it .hasNext();) {
			Ship s = it.next();
			if (ships.contains(s)) {
				ships.remove(s);
				removeShipGrid(s);
			}
		}
	}
	
	// Function sets each tile to the grid to the correct ship tile
	private void setShipToGrid(final Ship s) {
		List<String> cords = Arrays.asList(s.getCords());
		for (String cord : cords) {
			// determine orientation
			Grid.Type t = s.getOrientation() == Ship.Orientation.x ? Grid.Type.hortShip : Grid.Type.vertShip;
			setTile(cord, t);
		}
	}
	// Function sets each tile of ship to water
	private void removeShipGrid(final Ship s) {
		List<String> cords = Arrays.asList(s.getCords());
		for (String cord : cords) {
			setTile(cord, Grid.Type.water);
		}
	}
	
	private ArrayList<Ship> ships = new ArrayList<Ship>(5);
	
}

