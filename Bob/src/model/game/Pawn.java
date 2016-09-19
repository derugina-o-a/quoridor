package model.game;

public class Pawn {
	private Point location;
	private String colour;
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public Pawn(Point location, String colour) {
		super();
		this.location = location;
		this.colour = colour;
	}
	
}
