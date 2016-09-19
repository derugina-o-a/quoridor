package model.game.command;

import org.json.simple.JSONObject;

import model.game.map.Point;

public class MovePawnCommand extends Command {

	public MovePawnCommand(String colour, String gameId, Point newPawnLocation) {
		super(colour,gameId);
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
	@Override
	public String toJSONString() {
		JSONObject resultJson = new JSONObject();
	    resultJson = new JSONObject();
	    resultJson.put("type",type );
	    resultJson.put("player_colour", playerColour);
	    resultJson.put("coordinates", newPawnLocation.toJSONString());
	    return resultJson.toString();
	        
	}
}
