package model.game.map;

import org.json.simple.JSONObject;


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
}
