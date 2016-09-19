import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public class GameIDQueue {

	private Queue<String> gameIds;
	
	public GameIDQueue() {
		this.gameIds = new ArrayBlockingQueue<String>(10);
	}
	
	public void add(String id) {
		this.gameIds.add(id);
	}
	
	public String element() {
		return this.gameIds.element();
	}
}
