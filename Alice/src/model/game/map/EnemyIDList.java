package model.game.map;

import java.util.ArrayList;
import java.util.List;

public class EnemyIDList {

	private List<String> idList;
	
	public EnemyIDList() {
		idList = new ArrayList<>();
	}
	
	public boolean contains(String id) {
		if (this.idList.contains(id)) return true;
		else return false;
	}
	
	public void remove(String id) {
		this.idList.remove(id);
	}
	
	public void add (String id) {
		this.idList.add(id);
	}
}
