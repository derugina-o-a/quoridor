import game.Game;
import game.map.EnemyIDList;
import game.map.Point;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Main {
	private int blueTries;
	private int redTries;
	private static HashMap <String, EnemyIDList> pvpRequests = new HashMap<String,EnemyIDList>(); // <player_id, <enemy_ids..>> - enemy requests game with player
	private static HashMap <String, Game> activeGames = new  HashMap<String,Game>(); //List of active games
	private static HashMap<String, HashMap<String, GameIDQueue>> gameQueues = new HashMap<String, HashMap<String, GameIDQueue>>();// hash map<playerj_id, <hashmap <playeri_id, queue <game_ids>>>, storing for each player hash map of waiting for game players with queues of game_ids
	private String password;
	private String login;
	private List<Session> sessionList = new ArrayList<>();
	private List<Bot> botList = new ArrayList<>();
	private List<Game> gameList = new ArrayList<>();
	static int port = 8000;
	static int pool = 10;
	static int sessions = 0;
	public static void main(String[] args) throws IOException {
		
		clearLogs();
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(port);
		while (true) {
            Socket socket = serverSocket.accept();
            socket.setSoTimeout(30000000);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            
            if (sessions > pool) {
                socket.close();
                break;
            } else {
            	//sessions++;
            	String line = null;
            	line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
            	//read JSON
            	JSONParser parser = new JSONParser();
            	String result = "";
            	Object obj;
				try {
					obj = parser.parse(line);
					JSONObject jsonObj = (JSONObject) obj;
					String requestType = (String) jsonObj.get("type");
					String userLogin = (String) jsonObj.get("login");
					String userPassword = (String) jsonObj.get("password");
					String requestContent = (String) jsonObj.get("content");
					if (checkPassword(userLogin, userPassword)) {
						if (requestType.equals("login_request")) {
							saveLogs("login_request: "+userLogin);
							result = processLoginRequest(requestContent);
						}
						else if (requestType.equals("game_mode_request")) {
							saveLogs("game_mode_request: "+userLogin);
							result = processGameModeRequest(requestContent, userLogin);
						}
						else if (requestType.equals("game_status_request")) {
							saveLogs("game_status_request: "+userLogin);
							result = processGameStatusRequest(requestContent);
						}
						else if (requestType.equals("next_move_request")) {
							saveLogs("next_move_request: "+userLogin);
							result = processNextStepRequest(requestContent, userLogin);
						}
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
				
            	
               // System.out.println("The dumb client just sent me this line : " + line);
             out.writeUTF(result); // отсылаем клиенту обратно ответ за запрос.
             out.flush();
				
            }
		
		}
		

	}
	private static String processLoginRequest(String requestContent) {
		
    	 JSONObject resultJson = new JSONObject();
         resultJson.put("type", "login_request");
         resultJson.put("content", "1");
        
    	return resultJson.toString();
	}
	
	private static boolean checkPassword(String login, String password) {
		
		return true;
	}
	
	private static void addSession(String login, String password) {
		//
	}
	private static String processGameModeRequest(String requestContent, String login) {
		Object obj;
		JSONParser parser = new JSONParser();
    	String result = "";
		try {
			obj = parser.parse(requestContent);
			JSONObject jsonObj = (JSONObject) obj;
			String game_mode = (String) jsonObj.get("game_mode");
			String add_info = (String) jsonObj.get("add_info");
			//String game_id = (String) jsonObj.get("game_id");
			if (game_mode.equals("0")) {
				//tournament mode
				processTournamentModeRequest();
			}
			else if (game_mode.equals("1")) {
				//player versus player mode
				try {
					obj = parser.parse(add_info);
					jsonObj = (JSONObject) obj;
					String player2 = (String) jsonObj.get("enemy_login");
					String player1 = login;
					result = processPVPModeRequest(player1, player2);
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if (game_mode.equals("2")) {
				//player versus bot mode
				processPVBModeRequest();
			}
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	private static String processGameStatusRequest(String requestContent) {
		Object obj;
		JSONParser parser = new JSONParser();
    	String result = "";
    	String status = "";
		String content = "";
		try {
			obj = parser.parse(requestContent);
			JSONObject jsonObj = (JSONObject) obj;
			String game_id = (String) jsonObj.get("game_id");
			boolean fl = checkIfGameExists(game_id);
			if (!fl) {
				status = "invalid_game_id_or_player_login";
				content = "none";
			}
			else {
				Game game = activeGames.get(game_id);
				status = game.getStatus();
				JSONObject resultJson = new JSONObject();
				resultJson.put("game_id",game_id);
				resultJson.put("last_move", game.getLastMove());
				content = resultJson.toString();
			}
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = createServerResponce(status, "game_status_responce", content);
		return result;
	}
	private static void processTournamentModeRequest() {
		
	}
	private static String processPVPModeRequest(String player1_login, String player2_login) {
		String result = "";
		String content = "";
		String status = "";
		EnemyIDList list = pvpRequests.get(player1_login);
			//check if player2 is waiting a game
			if (list != null) {
				if (pvpRequests.get(player1_login).contains(player2_login)) {
					String gameId = gameQueues.get(player1_login).get(player2_login).element();
					status = "new_game";
					JSONObject resultJson = new JSONObject();
					resultJson.put("colour","red");
					resultJson.put("game_id",gameId);
					content = resultJson.toString();
					startPVPGame(player1_login, player2_login, gameId);
					pvpRequests.get(player1_login).remove(player2_login);
				}
				else {
					String gameId = getGameId();
					status = "wait_opponent_for_responce";
					JSONObject resultJson = new JSONObject();
					resultJson.put("colour","blue");
					resultJson.put("game_id",gameId);
					content = resultJson.toString();
					//add player1 to the list of waiting player2 opponents
					pvpRequests.get(player2_login).add(player1_login);
					//add game_id to the list of waiting player2 games
					gameQueues.get(player2_login).get(player1_login).add(gameId);
					createPVPGame(player1_login, player2_login, gameId);
				}
			}
			else {
				String gameId = getGameId();
				status = "wait_opponent_for_responce";
				JSONObject resultJson = new JSONObject();
				resultJson.put("colour","blue");
				resultJson.put("game_id",gameId);
				content = resultJson.toString();
				//add player1 to the list of waiting player2 opponents
				pvpRequests.put(player2_login, new EnemyIDList());
				pvpRequests.get(player2_login).add(player1_login);
				//add game_id to the list of waiting player2 games
				gameQueues.put(player2_login,new HashMap<String, GameIDQueue>());
				gameQueues.get(player2_login).put(player1_login, new GameIDQueue());
				gameQueues.get(player2_login).get(player1_login).add(gameId);
				createPVPGame(player1_login, player2_login, gameId);
			}
		
		
		result = createServerResponce(status, "game_mode_responce", content);
		return result;
	}
	
	private static String getGameId()  {
		return "121";
	}
	private static void startPVPGame(String player1_login, String player2_login, String gameId) {
		
		activeGames.get(gameId).start();
	}
	private static void createPVPGame(String player1_login, String player2_login, String gameId) {
		Game game = new Game(gameId,player1_login, player2_login);
		activeGames.put(gameId, game);
		
	}
	private static boolean checkIfGameExists(String game_id) {
		if (activeGames.containsKey(game_id)) {
			return true;
		}
		else return false;
	}
	
	private static void processPVBModeRequest() {
		
	}
	
	
	private static String processNextStepRequest(String requestContent, String user_login) {
		Object obj;
		JSONParser parser = new JSONParser();
    	String result = "";
    	String status = "";
		String content = "";
		try {
			obj = parser.parse(requestContent);
			JSONObject jsonObj = (JSONObject) obj;
			String game_id = (String) jsonObj.get("game_id");
			String coordinates = (String) jsonObj.get("coordinates");
			String type = (String) jsonObj.get("type");
			String error = validateNextStep(game_id, user_login, coordinates, type);
			if (error.equals("")) {
				//process next step
				if (type.equals("pawn")) {
					try {
						obj = parser.parse(coordinates);
						jsonObj = (JSONObject) obj;
						String x = (long) jsonObj.get("x")+"";
						String y = (long) jsonObj.get("y")+"";
						Game game = activeGames.get(game_id);
						game.movePawn(Integer.parseInt(x), Integer.parseInt(y),game.getColour(user_login));
						status = game.getStatus();
						content = "1";
						saveLogs(user_login+" moves pawn: "+ coordinates);
						if (status.equals("blue_win")||status.equals("red_win")||status.equals("nichya")) {
							saveLogs(status);
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						saveLogs(e.getMessage());
					}
				}
				else if (type.equals("fence")) {
					try {
						obj = parser.parse(coordinates);
						jsonObj = (JSONObject) obj;
						String x1 = (long) jsonObj.get("x1")+"";
						String y1 = (long) jsonObj.get("y1")+"";
						String x2 = (long) jsonObj.get("x2")+"";
						String y2 = (long) jsonObj.get("y2")+"";
						Game game = activeGames.get(game_id);
						game.buildFence(game.getColour(user_login),new Point(Integer.parseInt(x1),Integer.parseInt(y1)), new Point(Integer.parseInt(x2),Integer.parseInt(y2)));
						status = game.getStatus();
						content = "1";
						saveLogs(user_login+" builds fence: "+ coordinates);
						if (status.equals("blue_win")||status.equals("red_win")||status.equals("nichya")) {
							saveLogs(status);
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						saveLogs(e.getMessage());
					}
				}
				
			}
			else  {
				status= error;
				content = "0";
			}
			
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			saveLogs(e.getMessage());
		}
		
		result = createServerResponce(status, "next_move_responce", content);
		return result;
		
		
		
	}
	
	private static String validateNextStep(String game_id, String user_login, String coordinates, String type ) {
		String error="";
		//check if there is active game with this game_id
		if (!activeGames.containsKey(game_id)) {
			return "invalid_game_id";
		}
		else {
			Game game = activeGames.get(game_id);
			if (!game.getBluePlayerLogin().equals(user_login)&&!game.getRedPlayerLogin().equals(user_login) ) {
				return "invalid_game_id_or_user_login";
			}
			//check who's turn it is now
			String status = game.getStatus();
			String colour = game.getColour(user_login);
			if ((status.equals("blue_turn") && colour.equals("red")) || (status.equals("red_turn") && colour.equals("blue"))) {
				return "wait_your_turn_to_move";
			}
			
			//check coordinates
			//check x
			//check y
			
			if (type.equals("pawn")) {
				
				Object obj;
				JSONParser parser = new JSONParser();
		    	
				try {
					obj = parser.parse(coordinates);
					JSONObject jsonObj = (JSONObject) obj;
					
					String x = (long) jsonObj.get("x")+"";
					String y = (long) jsonObj.get("y")+"";
					error = game.validateNewPawnLocation(Integer.parseInt(x),Integer.parseInt(y), colour);
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					saveLogs(e.getMessage());
				}
				
			}
			else if (type.equals("fence")) {
				
				Object obj;
				JSONParser parser = new JSONParser();
		    	
				try {
					obj = parser.parse(coordinates);
					JSONObject jsonObj = (JSONObject) obj;
					
					String x1 = (long) jsonObj.get("x1")+"";
					String y1 = (long) jsonObj.get("y1")+"";
					String x2 = (long) jsonObj.get("x2")+"";
					String y2 = (long) jsonObj.get("y2")+"";
					error = game.validateNewFenceLocation(Integer.parseInt(x1),Integer.parseInt(y1),Integer.parseInt(x2),Integer.parseInt(y2),colour);
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else return "invalid_move_type";
		}
		
		return error;
	}
	private static String createServerResponce(String status, String type, String content) {
		String responce = "";
		JSONObject resultJson = new JSONObject();
		resultJson.put("status",status);
		resultJson.put("type", type);
		resultJson.put("content", content);
		responce = resultJson.toString();
		return responce;
	}
	public static void saveLogs(String log) {
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
	
	public static void clearLogs() {
		File f = new File("logs.txt");
		if (f.exists()) {
			f.delete();
		}
	
	}
}
