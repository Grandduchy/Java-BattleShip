// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// EnemyGrid is the hostile guessing grid given to the player
// It documents for the player where the player hit and missed shots
// ===========================================================================

import java.util.ArrayList;

public class EnemyGrid extends Grid {
	
	
	EnemyGrid() {
		// Constructor sets each value of the grid equal to a water type
		for (int i = 0; i != Grid.y; i++) {
			ArrayList<Grid.Type> row = new ArrayList<Grid.Type>(Grid.x);
			for (int x = 0; x != Grid.x; x++) {
				row.add(Grid.Type.water);
			}
			grid.add(row);
		}
	}
	
}
