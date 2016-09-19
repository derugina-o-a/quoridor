package model.connection;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;

import model.game.command.BuildFenceCommand;
import model.game.command.Command;
import model.game.command.MovePawnCommand;
import model.game.map.Fence;
import model.game.map.Point;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/*import com.fasterxml.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.simple.parser.ParseException;
import quoridor.model.GameObj;
import quoridor.model.RequestMsg;
import quoridor.model.ResponseMsg;
import quoridor.model.TypeRequestMsg;
import quoridor.model.TypeStatusMsg;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;*/
import org.json.simple.parser.ParseException;

public class Connector {
	private Socket conn;
	private Command lastMove;
	private Point blueLocation = new Point(4,8);
	private Point redLocation = new Point(4,0);
	private Point location;
	private String password;
	private String login;
	private String url;
	private String port;
	public Connector(String password, String login, String url, String port) {
		super();
		this.password = password;
		this.login = login;
		this.url = url;
		this.port = port;
		this.lastMove = null;
	}
	
	public Command getLastMove() {
		return lastMove;
	}
	
	public boolean login() throws NumberFormatException, UnknownHostException, IOException {
		 String requestResult = loginRequest();
		 String content = getContent(requestResult);
		 if (content.equals("1")) {
			 return true;
		 }
		 else {
			 return false;
		 }
	}
	
	public String getLogin() {
		return login;
	}

	public String getStatus() throws NumberFormatException, UnknownHostException, IOException {
		 String requestResult = statusRequest();
		 //save last move
		 saveLastMove(getContent(requestResult));
		return getStatus(requestResult);
	}
	
	private void saveLastMove(String requestResult) {
		try {
			Object obj;
			JSONParser parser = new JSONParser();
			obj = parser.parse(requestResult);
			JSONObject jsonObj = (JSONObject) obj;
			String gameId = (String) jsonObj.get("game_id");
			String lastMoveContent = (String) jsonObj.get("last_move");
			if (lastMoveContent != null) {
				if (!lastMoveContent.equals("none") ) {
					obj = parser.parse(lastMoveContent);
					jsonObj = (JSONObject) obj;
					String type = (String) jsonObj.get("type");
					
					String coordinates = (String) jsonObj.get("coordinates");
					Object coordinatesObj = parser.parse(coordinates);
					JSONObject coordinatesJsonObj = (JSONObject) coordinatesObj;
					if (type.equals("build_fence")) {
						//get coordinatesv
						String x1 = (long) coordinatesJsonObj.get("x1")+"";
						String x2 = (long) coordinatesJsonObj.get("x2")+"";
						String y1 = (long) coordinatesJsonObj.get("y1")+"";
						String y2 = (long) coordinatesJsonObj.get("y2")+"";
						Fence fence = new Fence(new Point(Integer.parseInt(x1),Integer.parseInt(y1)), new Point(Integer.parseInt(x2),Integer.parseInt(y2)));
						String playerColour = (String) jsonObj.get("player_colour");
						lastMove = new BuildFenceCommand(playerColour,gameId,fence );
					
					}
					else if (type.equals("move_pawn")) {
						String x = (long) coordinatesJsonObj.get("x")+"";
						String y = (long) coordinatesJsonObj.get("y")+"";
						
						String playerColour = (String) jsonObj.get("player_colour");
						lastMove = new MovePawnCommand(playerColour,gameId,new Point(Integer.parseInt(x), Integer.parseInt(y)) );
						
					}
			}
			
			}
			
				
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public String PVPGame(String enemyID) throws NumberFormatException, UnknownHostException, IOException {
		 //game request
	       saveLogs("PVPGame requested with enemy: "+enemyID);
	       String requestResult = PVPGameRequest(enemyID);
	       saveLogs("The server answered : " + requestResult);
	       String content = getContent(requestResult);
	       return content;
	}
	
	
	
	private String getStatus(String requestResult) {
		try {
			Object obj;
			JSONParser parser = new JSONParser();
			obj = parser.parse(requestResult);
			JSONObject jsonObj = (JSONObject) obj;
			String status = (String) jsonObj.get("status");
			return status;
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
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
	
	public String getGameId(String content) {
		try {
			Object obj;
			JSONParser parser = new JSONParser();
			obj = parser.parse(content);
			JSONObject jsonObj = (JSONObject) obj;
			String game_id = (String) jsonObj.get("game_id");
			return game_id;
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String getColour(String content) {
		try {
			Object obj;
			JSONParser parser = new JSONParser();
			obj = parser.parse(content);
			JSONObject jsonObj = (JSONObject) obj;
			String colour = (String) jsonObj.get("colour");
			return colour;
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private String loginRequest() throws NumberFormatException, UnknownHostException, IOException {
		
		saveLogs("login requested");
	  
		this.conn = new Socket(url, Integer.valueOf(port));
		//this.out = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
        //this.in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        /*this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);*/
        this.password = password;
        this.login = login;
      //  out.write("HelloWorld!");
       /* mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);*/
        
        InputStream sin = conn.getInputStream();
        OutputStream sout = conn.getOutputStream();
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
		JSONObject resultJson = new JSONObject();
        resultJson.put("password", password);
        resultJson.put("login", login);
        String content = resultJson.toString();
        resultJson = new JSONObject();
        resultJson.put("type","login_request" );
        resultJson.put("password", password);
        resultJson.put("content", content);
        out.writeUTF(resultJson.toString());
        out.flush();
        String line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        
        saveLogs(line);
        return line;
	}
	
	private String PVPGameRequest(String enemyId) throws NumberFormatException, UnknownHostException, IOException {
		this.conn = new Socket(url, Integer.valueOf(port));
		//this.out = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
        //this.in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        /*this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);*/
        this.password = password;
        this.login = login;
      //  out.write("HelloWorld!");
       /* mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);*/
        
        InputStream sin = conn.getInputStream();
        OutputStream sout = conn.getOutputStream();
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
        JSONObject resultJson = new JSONObject();
        resultJson.put("enemy_login", enemyId);
        String addInfo = resultJson.toString();
		resultJson = new JSONObject();
        resultJson.put("game_mode", "1");
        resultJson.put("add_info", addInfo);
        String content = resultJson.toString();
        resultJson = new JSONObject();
        resultJson.put("type","game_mode_request" );
        resultJson.put("login", login);
        resultJson.put("password", password);
        resultJson.put("content", content);
        out.writeUTF(resultJson.toString());
        out.flush();
        String line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        return line;
	}
	
	private String statusRequest() throws NumberFormatException, UnknownHostException, IOException {
		this.conn = new Socket(url, Integer.valueOf(port));
		//this.out = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
        //this.in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        /*this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);*/
        this.password = password;
        this.login = login;
      //  out.write("HelloWorld!");
       /* mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);*/
        
        InputStream sin = conn.getInputStream();
        OutputStream sout = conn.getOutputStream();
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
        JSONObject resultJson = new JSONObject();
      
		resultJson = new JSONObject();
        resultJson.put("game_id", "121");
       
        String content = resultJson.toString();
        resultJson = new JSONObject();
        resultJson.put("type","game_status_request" );
        resultJson.put("login", login);
        resultJson.put("password", password);
        resultJson.put("content", content);
        out.writeUTF(resultJson.toString());
        out.flush();
        String line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        return line;
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
	
	
	public String movePawnRequest(String gameId, Point newLocation) throws NumberFormatException, UnknownHostException, IOException {
		this.conn = new Socket(url, Integer.valueOf(port));
		//this.out = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
        //this.in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        /*this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);*/
        this.password = password;
        this.login = login;
      //  out.write("HelloWorld!");
       /* mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);*/
        
        InputStream sin = conn.getInputStream();
        OutputStream sout = conn.getOutputStream();
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
        JSONObject resultJson = new JSONObject();
        resultJson = new JSONObject();
        resultJson.put("x", newLocation.getX());
        resultJson.put("y", newLocation.getY());
        String coordinates = resultJson.toString();
		resultJson = new JSONObject();
        resultJson.put("game_id", "121");
        resultJson.put("coordinates", coordinates);
        resultJson.put("type", "pawn");
        String content = resultJson.toString();
        resultJson = new JSONObject();
        resultJson.put("type","next_move_request" );
        resultJson.put("login", login);
        resultJson.put("password", password);
        resultJson.put("content", content);
        saveLogs("next_move_request");
        out.writeUTF(resultJson.toString());
        out.flush();
        String line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        saveLogs("The server says: "+ line);
        return line;
	}
	

	public String buildFenceRequest(String gameId, Fence fence) throws NumberFormatException, UnknownHostException, IOException {
		this.conn = new Socket(url, Integer.valueOf(port));
		//this.out = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
        //this.in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        /*this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);*/
        this.password = password;
        this.login = login;
      //  out.write("HelloWorld!");
       /* mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);*/
        
        InputStream sin = conn.getInputStream();
        OutputStream sout = conn.getOutputStream();
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
        JSONObject resultJson = new JSONObject();
        resultJson = new JSONObject();
        resultJson.put("x1", fence.getFirstEnd().getX());
        resultJson.put("y1", fence.getFirstEnd().getY());
        resultJson.put("x2", fence.getSecondEnd().getX());
        resultJson.put("y2", fence.getSecondEnd().getY());
        String coordinates = resultJson.toString();
		resultJson = new JSONObject();
        resultJson.put("game_id", "121");
        resultJson.put("coordinates", coordinates);
        resultJson.put("type", "fence");
        String content = resultJson.toString();
        resultJson = new JSONObject();
        resultJson.put("type","next_move_request" );
        resultJson.put("login", login);
        resultJson.put("password", password);
        resultJson.put("content", content);
        saveLogs("next_move_request");
        out.writeUTF(resultJson.toString());
        out.flush();
        String line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        saveLogs("The server says: "+ line);
        return line;
	}
	
	public String processCommand(Command command) throws NumberFormatException, UnknownHostException, IOException {
		String requestResult = "";
		if (command.getType().equals("move_pawn")) {
			MovePawnCommand movePawnCommand = (MovePawnCommand) command;
			requestResult = movePawnRequest(movePawnCommand.getGameId(), movePawnCommand.getNewPawnLocation());
		}
		else if (command.getType().equals("build_fence")){
			BuildFenceCommand buildFenceCommand = (BuildFenceCommand) command;
			requestResult = buildFenceRequest(buildFenceCommand.getGameId(), buildFenceCommand.getFence());
		}
		return requestResult;
	}
	
}
