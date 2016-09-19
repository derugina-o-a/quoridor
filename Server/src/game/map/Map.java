package game.map;


import game.checker.MapChecker;
import game.command.BuildFenceCommand;
import game.command.Command;
import game.command.MovePawnCommand;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;;

 public class Map {
	 private String gameId;
	 
	private Command lastMove = null;
	public Command getLastMove() {
		return lastMove;
	}



	private final int length = 9; // board size
	private final Point blueStartLocation = new Point(4,8);
	private final Point redStartLocation = new Point(4,0);
	private Pawn bluePawn; // 
	
	private Pawn redPawn; // 	

	private FenceList fenceList = new FenceList(); // a list of fences
	
	
	public int getLength() {
		return length;
	}


	


	public FenceList getFenceList() {
		return fenceList;
	}


	public Map(String gameId) {
		this.gameId = gameId;
		bluePawn = new Pawn(blueStartLocation,"blue");
		redPawn = new Pawn(redStartLocation,"red");
		}
		
		
	
	
	public void moveRedPawn(Point newLocation) {
		if (validateNewPawnLocation(newLocation, "red").equals("")) {
			redPawn.setLocation(newLocation);
			lastMove = new MovePawnCommand(this.gameId,"red", newLocation);
		}
		
	}
	
	public Point getBlueLocation() {
		return bluePawn.getLocation();
	}
	



	public Point getBlueStartLocation() {
		return blueStartLocation;
	}





	public Point getRedStartLocation() {
		return redStartLocation;
	}





	public Point getRedLocation() {
		return redPawn.getLocation();
	}





	public void moveBluePawn(Point newLocation) {
		if (validateNewPawnLocation(newLocation, "blue").equals("")) {
			bluePawn.setLocation(newLocation);
			lastMove = new MovePawnCommand(this.gameId,"blue", newLocation);
		}
		
	}
	
	
	
	public String validateNewPawnLocation(Point newLocation, String colour) {
		String error = "";
		Pawn pawn;
		Point location;
		if (colour.equals("red")) {
			pawn = redPawn;
			
		}
		else {
			pawn = bluePawn;
		}
		location = pawn.getLocation();
		//check if the new location is on the board
		int x = newLocation.getX();
		int y = newLocation.getY();
		if (x < 0 || x > (length-1)) {
			error= "invalid coordinates for the new pawn location"; 
			return error;
		}
		if (y < 0 || y > (length-1)) {
			error= "invalid coordinates for the new pawn location: out of board"; 
			return error;
		}
		//check if the new location is available from the current location
		int x0 = location.getX();
		int y0 = location.getY();
		if ((x == x0) && ((y == (y0-1))||( y == y0+1))) {
			//ok
		}
		else if ((y==y0) && ((x == (x0-1))||(x == x0+1))) {
			//ok
		}
		else if ((x==x0)&&( y==y0)) {
			//ok
		}
		else {
			error = "new location is not reachable from the current location: too far from the pawn: "+x+" "+y;
			return error;
		}
		//check if a wall exists in the new location
		if (fenceList.fenceExists(newLocation)) {
			error = "new location is occupied by a fence";
			return error;
		}
		//check if there is the enemy in the new location
		if (colour.equals("red")) {
			
			if (newLocation.getX()==bluePawn.getLocation().getX() && newLocation.getY()==bluePawn.getLocation().getY()) {
				error = "new location is occupied by the enemy";
				return error;
			}
		}
		else {
			if (newLocation.getX()==redPawn.getLocation().getX() && newLocation.getY()==redPawn.getLocation().getY()) {
				error = "new location is occupied by the enemy";
				return error;
			}
		}
		return error;
	}
	public void buildFence(Point fisrtEnd, Point secondEnd, String colour) {
		if (validateNewFenceLocation(fisrtEnd, secondEnd).equals("")) {
			fenceList.add(new Fence(fisrtEnd, secondEnd));
			lastMove = new BuildFenceCommand(this.gameId,colour, new Fence(fisrtEnd, secondEnd));
		}
	}
	public String validateNewFenceLocation(Point firstEnd, Point secondEnd) {
		MapChecker checker = new MapChecker();
		String error = checker.validateNewFence(this, firstEnd, secondEnd);
		return error;
	}
	
	





	private boolean fenceClosesPawns(Point firstEnd, Point secondEnd) {
		return false;
	}
	
}
