package model.game.checker;

import java.util.ArrayList;
import java.util.List;

import model.game.map.Edge;
import model.game.map.Point;
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
	return null;
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
	 if (edges.contains(edge) || edges.contains(new Edge(edge.getSecondEnd(),edge.getFirstEnd()))) {
		 return true;
	 }
	 else return false;
 }
 public void add(Edge edge) {
	 if (!this.contains(edge)) {
		 edges.add(edge);
	 }
 }
 public void delete(Edge edge) {
	 if (edges.contains(edge)) {
		 edges.remove(edges.indexOf(edge));
	 }
	 else if (edges.contains(new Edge(edge.getSecondEnd(),edge.getFirstEnd()))) {
		 edges.remove(edges.indexOf(new Edge(edge.getSecondEnd(),edge.getFirstEnd())));
	 }
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
	 edges = new ArrayList();
	 for (int i=0; i<graph.getEdges().hashCode();i++) {
		 edges.add(graph.getEdges().get(i));
	 }
 }
}
