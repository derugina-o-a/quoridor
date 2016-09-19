package game.checker;

import game.map.Edge;
import game.map.Point;

import java.util.ArrayList;
import java.util.List;
//not oriented Graph
public class Graph {
 private List<Edge> edges;
 private List<Point> vertexes;
 
 public List<Edge> getEdges() {
	return edges;
}

public Graph() {
	 edges = new ArrayList();
	 vertexes = new ArrayList();
 }

public List<Point> getNeihgbourVertexes(Point point) {
	List<Point> neighbours = new ArrayList();
	for (int i=0; i<vertexes.size();i++) {
		if (!point.equals(vertexes.get(i))) {
			if (this.contains(new Edge(point, vertexes.get(i)))) {
				neighbours.add(vertexes.get(i));
			}
		}
	}
	return neighbours;
}
 

public List<Point> getVertexes() {
	return vertexes;
}

public int getVertexCount() {
	return vertexes.size();
}


public int getEdgeCount() {
	return edges.size();
}

public boolean contains(Point vertex) {
	 if (vertexes.contains(vertex)) {
		 return true;
	 }
	 else return false;
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
 
 private void remove(Edge edge) {
	int index=0;
	
	while 
 }
 
 
 public void add(Point vertex) {
	 if (!this.contains(vertex)) {
		 vertexes.add(vertex);
	 }
 }
 public void delete(Point vertex) {
	 if (vertexes.contains(vertex)) {
		vertexes.remove( vertexes.indexOf(vertex));
	 }
	
 }
 
 public Graph(Graph graph) {
	 edges = new ArrayList<Edge>();
	 for (int i=0; i<graph.getEdges().size();i++) {
		 edges.add(graph.getEdges().get(i));
		 
	 }
	 vertexes = new ArrayList<Point>();
	 for (int i=0; i<graph.getVertexes().size();i++) {
		 vertexes.add(graph.getVertexes().get(i));
		 
	 }
 }
}
