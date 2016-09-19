package game.map;

import org.json.simple.JSONObject;
import java.math.*;;

public class Fence {
	Point firstEnd;
	Point secondEnd;
	public Point getFirstEnd() {
		return firstEnd;
	}
	public void setFirstEnd(Point firstEnd) {
		this.firstEnd = firstEnd;
	}
	public Point getSecondEnd() {
		return secondEnd;
	}
	public void setSecondEnd(Point secondEnd) {
		this.secondEnd = secondEnd;
	}
	public Fence(Point firstEnd, Point secondEnd) {
		super();
		this.firstEnd = firstEnd;
		this.secondEnd = secondEnd;
	}
	public String toJSONString() {
		JSONObject resultJson = new JSONObject();
	    resultJson = new JSONObject();
	    resultJson.put("x1",firstEnd.getX() );
	    resultJson.put("x2", secondEnd.getX());
	    resultJson.put("y1", firstEnd.getX());
	    resultJson.put("y2", secondEnd.getX());
	    return resultJson.toString();
	}
	
	public int getLength() {
		int sub = (firstEnd.getX()-secondEnd.getX())* (firstEnd.getX()-secondEnd.getX()) + (firstEnd.getY()-secondEnd.getY())*(firstEnd.getY()-secondEnd.getY()) ;
		return sub;
	}
}
