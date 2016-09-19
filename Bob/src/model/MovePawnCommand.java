package model;

import model.game.Point;

public class MovePawnCommand extends Command {

	public MovePawnCommand(String gameId, Point newPawnLocation) {
		super(gameId);
		this.newPawnLocation= newPawnLocation;
		this.type = "move_pawn";
	}

	private Point newPawnLocation;
	
	

	public Point getNewPawnLocation() {
		return newPawnLocation;
	}

	public void setNewPawnLocation(Point newPawnLocation) {
		this.newPawnLocation = newPawnLocation;
	}
	
}
