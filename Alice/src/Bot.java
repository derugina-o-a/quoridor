import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.connection.Connector;
import model.game.Game;
import model.game.command.BuildFenceCommand;
import model.game.command.Command;
import model.game.command.MovePawnCommand;
import model.game.map.Fence;
import model.game.map.Point;



public class Bot {
	
	private int id;	
	private String name;
	private String description;
	private String authorName;
	private String authorId;
	private Connector connector;
	private Point location;
	private Point blueLocation = new Point(4,8);
	private Point redLocation = new Point(4,0);
	private HashMap<String,Game> gameList = new HashMap<String,Game>(); 
	public Bot(Connector connector) {
		this.connector = connector;
	}
	private int stepCounter;
	
	
	
	
	public Command chooseNextStep(String gameId, String colour) {
		Command command;
		
		if (stepCounter ==0) {
			 Fence  fence = new Fence(new Point(4,4), new Point(5,4));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else if (stepCounter ==1 ) {
			 Fence  fence = new Fence(new Point(6,4), new Point(7,4));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else if (stepCounter ==3 ) {
			 Fence  fence = new Fence(new Point(8,4), new Point(8,3));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else if (stepCounter ==4 ) {
			 Fence  fence = new Fence(new Point(0,4), new Point(0,3));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else if (stepCounter ==5) {
			 Fence  fence = new Fence(new Point(1,3), new Point(2,3));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else {
			 Fence  fence = new Fence(new Point(3,3), new Point(3,4));
			 command = createBuildFenceCommand(colour,gameId, fence);
		}
		/*if (stepCounter ==0) {
			Point newPawnLocation =new Point(location.getX()-1, location.getY());
			command = createMovePawnCommand(colour,gameId, newPawnLocation);
			
		}
		else if (stepCounter ==1 ) {
			 Fence  fence = new Fence(new Point(0,1), new Point(0,2));
			 command = createBuildFenceCommand(colour,gameId, fence);
			
		}
		else {
			 Point newPawnLocation;
			 if(colour.equals("red")) {
				 
				   newPawnLocation = new Point(location.getX(), location.getY()+1);
			   }
			 else {
				   newPawnLocation = new Point(location.getX(), location.getY()-1);
			   }
		
			 command = createMovePawnCommand(colour,gameId, newPawnLocation);
			
		}*/
		
		return command;
	}
	
	
	private Command createBuildFenceCommand(String colour, String gameId,Fence fence) {
			
			Command command = new BuildFenceCommand(colour,gameId,fence);
			return command;
		}
	

	private Command createMovePawnCommand(String colour,String gameId,Point newLocation) {
		
			Command command = new MovePawnCommand(colour,gameId,newLocation);
			return command;
		}
	
	public void startPVPGame(String enemyID, Connector connector ) throws NumberFormatException, UnknownHostException, IOException, InterruptedException {
		clearLogs();
		String requestResult;
		//login
		boolean login_fl = connector.login();
		if (login_fl) {
			//startPVPGame
			String content = connector.PVPGame(enemyID);
			String gameId = connector.getGameId(content);
		    String colour = connector.getColour(content);
		    //create new game and add to the list
		    Game game;
		    if (colour.equals("blue")) {
		    	 game = new Game(gameId,connector.getLogin(), enemyID);
		    }
		    else {
		    	 game = new Game(gameId, enemyID, connector.getLogin());
		    }
		   
		    gameList.put(gameId,game);
		    
		    //play
		       if (colour.equals("red")) {
		    	   location = redLocation;
		       }
		       else location = blueLocation;
		       
		       
			   String status = getStatus();
			 
			  
			   while (!status.equals("blue_win") && !status.equals("red_win") && !status.equals("nichya")) {
				   if (status.equals(colour+"_turn")) {
					  Command command = chooseNextStep(gameId,colour);
					  requestResult = connector.processCommand(command);
					 content = getContent(requestResult);  
					  if (content.equals("1")) {
						  stepCounter++;
						  game.processCommand(command);
						  if (command.getType().equals("move_pawn")) {
							  location = ((MovePawnCommand) command).getNewPawnLocation();
							  
						  }
					    	
					  }
					  else {
					    	//process error
					  }

					  status = getStatus();

				   }
				   else {
					  
						   Thread.sleep(10000);
					
						  status = getStatus();

				 }
			   }
			   saveLogs(status);
		}
		
		
	}
	
	
	
	
	public String getStatus() throws NumberFormatException, UnknownHostException, IOException {
		saveLogs("status requested");
		String status = connector.getStatus();
		saveLogs("status: "+ status);
		Command command = connector.getLastMove();
		if (command != null ){
			String gameId = command.getGameId();
			Game game = gameList.get(gameId);
			if (command.getPlayerColour() != game.getColour(connector.getLogin())) {
				game.processCommand(command);
			}
			
		}
		return status;
	}
	
	public void disconnect() {
		
	}
	
	public Bot(int id, String name, String description, String authorName,
			String authorId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.authorName = authorName;
		this.authorId = authorId;
		this.stepCounter = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public void saveLogs(String log) {
		try(FileWriter writer = new FileWriter("logs.txt", true))
        {
           // запись текста в файл с логами
           String text = log+"\r\n";
            writer.append(text);
          
           
            writer.flush();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 	
		
	
	}
	
	public void clearLogs() {
		File f = new File("logs.txt");
		if (f.exists()) {
			f.delete();
		}
	
	}
	
	
	
	private String getContent(String requestResult) {
		try {
			Object obj;
			JSONParser parser = new JSONParser();
			obj = parser.parse(requestResult);
			JSONObject jsonObj = (JSONObject) obj;
			String content = (String) jsonObj.get("content");
			return content;
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
