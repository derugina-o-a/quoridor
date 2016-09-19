package model.game.map;


import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;;

 public class Map {
	 
	private Point lastMove = null;
	public Point getLastMove() {
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


	public Map() {
		bluePawn = new Pawn(blueStartLocation,"blue");
		redPawn = new Pawn(redStartLocation,"red");
		}
		
		
	
	
	public void moveRedPawn(Point newLocation) {
		if (validateNewPawnLocation(newLocation, "red").equals("")) {
			redPawn.setLocation(newLocation);
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
	public void buildFence(Point fisrtEnd, Point secondEnd) {
		if (validateNewFenceLocation(fisrtEnd, secondEnd).equals("")) {
			fenceList.add(new Fence(fisrtEnd, secondEnd));
		}
	}
	public String validateNewFenceLocation(Point firstEnd, Point secondEnd) {
		int x0 = firstEnd.getX();
		int y0 = firstEnd.getY();
		int x1 = firstEnd.getX();
		int y1= firstEnd.getY();
		String error = "";
		//check if coordinates are on the board and don't occupy bottom and top cells
		if ((y0 == 0 || y0 == length-1)|| (y1 == 0 || y1 == length-1)) {
			error = "a fence can't be situated on bottom or top cells";
			return error;
		}
		if ((x0 > 8 || x0 <0)|| (x1 > 8 || x1< 0) || (y0 < 1 || y0 > length-2) || (y1 < 1 || y1 > length-2)) {
			error = "fence coordinates are out of the board";
			return error;
		}
		
		//check if coordinates are occupied by a pawn
		
		if ( (x0 == redPawn.getLocation().getX() && y0 ==  redPawn.getLocation().getY()) || (x0 == bluePawn.getLocation().getX() && y0 ==  bluePawn.getLocation().getY()) ) {
			error = "fence coordinates are occupied by a pawn";
			return error;
		}

		if ( (x1 == redPawn.getLocation().getX() && y1 ==  redPawn.getLocation().getY()) || (x1 == bluePawn.getLocation().getX() && y1 ==  bluePawn.getLocation().getY()) ) {
			error = "fence coordinates are occupied by a pawn";
			return error;
		}
		//check if coordinates are occupied by another fence
		if (fenceList.fenceExists(firstEnd) || fenceList.fenceExists(secondEnd)) {
			error = "fence coordinates are occupied by a fence";
			return error;
		}
		//check if the new fence closes any of the pawns
		if (fenceClosesPawns(firstEnd, secondEnd)) {
			error = "fence closes a path to a pawn";
			return error;
		}
		
		return error;
	}
	
	





	private boolean fenceClosesPawns(Point firstEnd, Point secondEnd) {
		return false;
	}
	
}
