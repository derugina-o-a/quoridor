package game.checker;

import game.map.Edge;
import game.map.Point;

import java.util.ArrayList;
import java.util.List;

public class EdgeList {
	private List<Point> edges;
	
	public EdgeList() {
		edges = new ArrayList<Point>();
	}
	
	 public boolean contains(Edge edge) {

		 for (int i=0; i< edges.size(); i++) {
			 if (edges.get(i).equals(edge) || edges.get(i).equals(new Edge(edge.getSecondEnd(),edge.getFirstEnd()))) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public void add(Edge edge) {
		 if (!this.contains(edge)) {
			 edges.add(edge);
		 }
	 }
	 public void delete(Edge edge) {
		 if (this.contains(edge)) {
			 this.remove(edge);
		 }
		
	 }
}
