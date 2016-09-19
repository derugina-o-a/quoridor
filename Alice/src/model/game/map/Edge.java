package model.game.map;

public class Edge {
	private Point firstEnd;
	private Point secondEnd;
	public Point getFirstEnd() {
		return firstEnd;
	}
	public Point getSecondEnd() {
		return secondEnd;
	}
	public Edge(Point firstEnd, Point secondEnd) {
		this.firstEnd = firstEnd;
		this.secondEnd = secondEnd;
	}
	
	

}
