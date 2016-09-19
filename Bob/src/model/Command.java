package model;

public class Command {

	protected String type;
	protected String gameId;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGameId() {
		return gameId;
	}
	public Command(String gameId) {
		
		this.gameId = gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
}
