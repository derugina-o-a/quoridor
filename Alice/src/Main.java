import java.io.IOException;
import java.net.UnknownHostException;

import model.connection.Connector;

import org.json.simple.JSONObject;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Map map = new Map("blue");
		//System.out.println(map.moveEnemyPawn());
		
		String login = "alice";
		String url = "localhost";
		String port = "8000";
		String password = "12345";		
				
		//create connector
		Connector connector = new Connector(password, login, url, port);
		
		//create bot
		Bot myBot = new Bot(connector);
		try {
			myBot.startPVPGame("bob", connector);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//testJSON();

		
	}

	
}
