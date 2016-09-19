
public class GameRequest {

	private String playerId;
	private String enemyId;
	private String gameMode;
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getEnemyId() {
		return enemyId;
	}
	public void setEnemyId(String enemyId) {
		this.enemyId = enemyId;
	}
	public String getGameMode() {
		return gameMode;
	}
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	public GameRequest(String playerId, String enemyId, String gameMode) {
		super();
		this.playerId = playerId;
		this.enemyId = enemyId;
		this.gameMode = gameMode;
	}
	
	
}
