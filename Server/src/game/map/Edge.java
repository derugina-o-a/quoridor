package game.map;

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
	
	

	public boolean equals(Edge edge) {
		if (this.getFirstEnd().equals(edge.getFirstEnd()) && this.getSecondEnd().equals(edge.getSecondEnd())) {
			return true;
		}
		else return false;
	}
}
