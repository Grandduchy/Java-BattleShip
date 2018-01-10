// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Grid represents a general interface for each future child grid type
// Grid's interface is printing and setting tiles
// ===========================================================================

import java.util.ArrayList;

public abstract class Grid {
	// Enum represents a type on each grid tile
	enum Type{
		water, hitmarker, nohit, vertShip, hortShip
	}
	
	
	final static int x = 10;
	final static int y = 10;
	
	public void printGrid() {
		System.out.print("   ");
		for (int x = 0; x != Grid.x; x++) { // Print the alphabet
			System.out.print((char)('A' + x) + " ");
		}
		System.out.println();
		for (int y = 0; y != Grid.y; y++) {
			if (y+1 == 10) // the number 10 modifies the spacing, so the space preceding the number must be different.
				System.out.print((y+1) + " ");
			else
				System.out.print((y+1) + "  ");
			
			for (int x = 0; x != Grid.x; x++) { // Print the type of each tile in the horizontal direction
				Grid.Type type = grid.get(y).get(x);
				BattleShipGame.printType(type);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	// Function sets the tile type at the given coordinate.
	public void setTile(String cord, Grid.Type type) {
		int x = cord.charAt(0) - 'A';
		int y = Integer.parseInt(String.valueOf(cord.substring(1))) - 1;
		ArrayList<Grid.Type> newArr = grid.get(y);
		newArr.set(x, type);
		grid.set(y, newArr);
	}
	
	
	protected ArrayList<ArrayList<Grid.Type>> grid = new ArrayList<ArrayList<Grid.Type>>(Grid.y);
}
