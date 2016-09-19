package model.game;


import java.util.List;

import model.game.command.BuildFenceCommand;
import model.game.command.Command;
import model.game.command.MovePawnCommand;
import model.game.map.Fence;
import model.game.map.FenceList;
import model.game.map.Map;
import model.game.map.Point;

public class Game {
	private String id;
	private String status;
	private String bluePlayerLogin;
	private String redPlayerLogin;
	private Map map;
	private int bluePawnErrors;
	private int redPawnErrors;
	private final int maxErrors;
	public String getLastMove() {
		
		Point lastMove = map.getLastMove();
		if (lastMove!=null) {
			return lastMove.getX()+";"+ lastMove.getY();
		}
		else return "none";
		
	}
	public FenceList getFences() {
		return map.getFenceList();
	}
	
	public String getColour(String player_login) {
		if (player_login.equals(bluePlayerLogin)) {
			return "blue";
		}
		else if (player_login.equals(redPlayerLogin)) {
			return "red";
		}
		else return "invalid_player_login";
	}
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getBluePlayerLogin() {
		return bluePlayerLogin;
	}


	public void setBluePlayerLogin(String bluePlayerLogin) {
		this.bluePlayerLogin = bluePlayerLogin;
	}


	public String getRedPlayerLogin() {
		return redPlayerLogin;
	}


	public void setRedPlayerLogin(String redPlayerLogin) {
		this.redPlayerLogin = redPlayerLogin;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void buildFence(Fence fence) {
		buildFence(fence.getFirstEnd(),fence.getSecondEnd());
	}
	public void buildFence(Point firstEnd, Point secondEnd) {
		map.buildFence(firstEnd, secondEnd);
	}
	
	public void movePawn(int x, int y, String colour) {
		movePawn(new Point(x,y), colour);
	}
	public void movePawn(Point newLocation, String colour) {
		if (colour.equals("blue")) {
			moveBluePawn(newLocation);
		}
		else if (colour.equals("red")) {
			moveRedPawn(newLocation);
		}
	}
	public void moveRedPawn(Point newLocation) {
		this.map.moveRedPawn(newLocation);
		redPawnErrors = 0;
		if (newLocation.getY()==map.getBlueStartLocation().getY()) {
			status = "red_win";
		}
		else {
			status = "blue_turn";
		}
	}
	
	public void moveBluePawn(Point newLocation) {
		this.map.moveBluePawn(newLocation);
		bluePawnErrors = 0;
		if (newLocation.getY()==map.getRedStartLocation().getY()) {
			status = "blue_win";
		}
		else {
			status = "red_turn";
		}
	}
	
	public String validateNewPawnLocation(int x, int y, String colour){
		return validateNewPawnLocation(new Point(x,y),colour);
		
	}
	
	public String validateNewPawnLocation(Point newLocation, String colour){
		String error = this.map.validateNewPawnLocation(newLocation, colour);
		if (!error.equals("")) {
			if (colour.equals("blue")) {
				bluePawnErrors++;
				if (bluePawnErrors==maxErrors) {
					status = "red_turn";
					bluePawnErrors = 0;
				}
			}
			else {
				redPawnErrors++;
				if (redPawnErrors==maxErrors) {
					status = "blue_turn";
					redPawnErrors = 0;
				}
			}
		}
		return error; 
	}
	public String validateNewFenceLocation(int x1, int y1, int x2, int y2, String colour){
		
		
		return validateNewFenceLocation(new Point(x1,y1), new Point(x2, y2),colour);
	}
	
	public String validateNewFenceLocation(Point firstEnd, Point secondEnd, String colour ){
		String error =this.map.validateNewFenceLocation(firstEnd, secondEnd);
		if (!error.equals("")) {
			if (colour.equals("blue")) {
				bluePawnErrors++;
				if (bluePawnErrors==maxErrors) {
					status = "red_turn";
					redPawnErrors = 0;
				}
			}
			else {
				redPawnErrors++;
				if (redPawnErrors==maxErrors) {
					status = "blue_turn";
					bluePawnErrors = 0;
				}
			}
		}
		return error;
	}
	public Game(String id,String bluePlayerLogin, String redPlayerLogin) {
		this.id = id;
		this.bluePlayerLogin = bluePlayerLogin;
		this.redPlayerLogin = redPlayerLogin;
		status = "waiting_to_start";
		map = new Map();
		bluePawnErrors = 0;
		redPawnErrors = 0;
		maxErrors = 2;
	}
	public void start() {
		this.status = "blue_turn";
	}
	public String getStatus() {
		return status;
	}
	
	
	public void processCommand(Command command) {
		if (command.getType().equals("move_pawn")) {
			MovePawnCommand movePawnCommand = (MovePawnCommand)command;
			movePawn(movePawnCommand.getNewPawnLocation(), movePawnCommand.getPlayerColour());
		}
		else if  (command.getType().equals("build_fence")) {
			BuildFenceCommand fenceCommand = (BuildFenceCommand)command;
			buildFence(fenceCommand.getFence());
		}
	}
}
