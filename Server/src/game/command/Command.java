package game.command;

import org.json.simple.JSONObject;

public class Command {

	protected String gameId;
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public void setPlayerColour(String playerColour) {
		this.playerColour = playerColour;
	}

	protected String type;

	
	protected String playerColour;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPlayerColour() {
		return playerColour;
	}
	public Command(String gameId,String playerColour) {
		this.gameId = gameId;
		this.playerColour = playerColour;
	}
	
	public String toJSONString() {
		JSONObject resultJson = new JSONObject();
	    resultJson = new JSONObject();
	    resultJson.put("game_id",gameId );
	    resultJson.put("type",type );
	    resultJson.put("player_colour", playerColour);
	    return resultJson.toString();
	        
	}
	
	
}
