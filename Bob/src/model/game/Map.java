package model.game;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;;

 class Map {
	private final int length = 9; // board size
	private final Point blueLocation = new Point(4,8);
	private final Point redLocation = new Point(4,0);
	private Pawn enemyPawn; // 
	
	private Pawn myPawn; // 	

	private List<Fence> fenceList = new ArrayList<>(); // a list of fences
	
	
	public int getLength() {
		return length;
	}


	public Pawn getEnemyPawn() {
		return enemyPawn;
	}


	public Pawn getMyPawn() {
		return myPawn;
	}


	public List<Fence> getFenceList() {
		return fenceList;
	}


	public Map(String enemyColour) {
		if (enemyColour.equals("blue")) {
			enemyPawn = new Pawn(blueLocation,enemyColour);
			myPawn = new Pawn(redLocation,"red");
		}
		else {
			enemyPawn = new Pawn(redLocation,enemyColour);
			myPawn = new Pawn(blueLocation,"blue");
		}
		
	}
	
	public void moveMyPawn(Point newLocation) {
		if (validateNewPawnLocation(newLocation)) {
			myPawn.setLocation(newLocation);
		}
		
	}
	
	public String moveEnemyPawn() {
		
		return "";
	}
	
	private boolean validateNewPawnLocation(Point newLocation) {
		return true;
	}
	public void buildFence(Point fisrtEnd, Point secondEnd) {
		if (validateNewFence(fisrtEnd, secondEnd)) {
			fenceList.add(new Fence(fisrtEnd, secondEnd));
		}
	}
	private boolean validateNewFence(Point fisrtEnd, Point secondEnd) {
		return true;
	}
	
}
