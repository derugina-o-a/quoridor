package model.game;

import java.util.List;

public class Game {

	private Map map;
	
	public List<Fence> getFences() {
		return map.getFenceList();
	}
	
	public void buildFence(Point firstEnd, Point secondEnd) {
		map.buildFence(firstEnd, secondEnd);
	}
	public void movePawn(Point newLocation) {
		map.moveMyPawn(newLocation);
	};
	
	public int getState() {
		return 0;
	}
}
