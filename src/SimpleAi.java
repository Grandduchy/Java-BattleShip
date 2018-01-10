// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// SimpleAi represents an Ai based on chances.
// SimpleAi works by having taking the players ships on the grid and the ai has a chance to hit them
// ===========================================================================

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimpleAi extends Ai {
	// Order represents if the ai should destroy the ship from Head -> Tail or Tail -> Head
	private enum Order {
		increasing, decreasing
	}
	
	SimpleAi(String name, int chance) {
		this.name = name;
		this.rate = (float)chance / 100;
	}
	
	@Override
	public void setShips() {
		setAiShips();
	}

	@Override
	public String runTurn() {
		
		String coordinate = null;
		boolean canHit = Math.random() <= rate;
		if (canHit) {
			
			Ship ship = nonDestroyedShip();
			if (ship != null) {
				if (holeShipIndex == -1 ) { // the ship hasn't been hit before, set the types.
					order = (int)(Math.random() * 2) + 1 == 1 ? Order.increasing : Order.decreasing;
					holeShipIndex = order == Order.increasing ? 0 : ship.sizeOf() - 1;
				 }
				Ship current = ships.get(shipIndex);
				String cords[] = current.getCords();
				// set the coordinate to a hole on the ship
				coordinate = cords[order == Order.increasing ? holeShipIndex++ : holeShipIndex--];
				current.hit(coordinate);
				if (current.isDestroyed()) { // reset indexes if its been destroyed.
					holeShipIndex = -1;
					shipIndex++;
				}
			}
			else {
				throw new RuntimeException("All are destroyed.");
			}
		}
		else {
			String cord = null;
			do {
				cord = randomCord(); // get a random coordinate that does not hit another ship.
			} while(doesHitOtherShip(cord));
			coordinate = cord;
		}
		return coordinate;
	}

	// Function sets the player ships to the Ai ships
	public void setPlayerShips(Ship... ships) {
		for (Ship s : ships) {
			this.ships.add(s);
		}
		Collections.shuffle(this.ships);
	}
	// Function sets the player ships to the Ai ships
	public void setPlayerShips(Player player) {
		ships.add(player.battleship);
		ships.add(player.carrier);
		ships.add(player.cruiser);
		ships.add(player.destroyer);
		ships.add(player.sub);
		Collections.shuffle(this.ships);
	}
	
	// Checks if the coordinate hits a ship
	private boolean doesHitOtherShip(String cord) {
		for (Ship ship : ships) {
			if (ship.canHit(cord)) 
				return true;
		}
		return false;
	}
	// function returns the first non destroyed ship
	private Ship nonDestroyedShip() {
		Ship ship = null;
		int index = 0;
		for (Iterator<Ship> iter = ships.iterator(); iter.hasNext(); index++) {
			Ship s = iter.next();
			if (!s.isDestroyed()) {
				ship = s;
				shipIndex = index;
			}
		}
		return ship;
		
	}
	
	private ArrayList<Ship> ships = new ArrayList<Ship>(5);
	private float rate = 0.0f;
	private int holeShipIndex = -1; // the coordinate of a single ship's index
	private int shipIndex = 0; // the index of each indivisual ship.
	private Order order = null;
	
}
