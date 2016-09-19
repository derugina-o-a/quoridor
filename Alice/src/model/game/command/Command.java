package model.game.command;

import org.json.simple.JSONObject;

public class Command {

	protected String type;
	protected String gameId;
	protected String playerColour;
	
	public String getPlayerColour() {
		return playerColour;
	}
	public void setPlayerColour(String playerColour) {
		this.playerColour = playerColour;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGameId() {
		return gameId;
	}
	public Command(String colour,String gameId) {
		this.playerColour = colour;
		this.gameId = gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String toJSONString() {
		JSONObject resultJson = new JSONObject();
	    resultJson = new JSONObject();
	    resultJson.put("type",type );
	    resultJson.put("player_colour", playerColour);
	    return resultJson.toString();
	        
	}
}
