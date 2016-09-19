package model.game;

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
	
	
}
