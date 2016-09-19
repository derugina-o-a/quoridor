package game.map;

import org.json.simple.JSONObject;


public class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(Point point) {
		if (this.getX() == point.getX() && this.getY()==point.getY()) {
			return true;
		}
		else return false;
	}
	
	public String toJSONString(){
		JSONObject resultJson = new JSONObject();
	    resultJson = new JSONObject();
	    resultJson.put("x",x );
	    resultJson.put("y", y);
	   
	    return resultJson.toString();
	        
	}
}
