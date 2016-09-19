package model.game.checker;

import java.util.ArrayList;
import java.util.List;

import model.game.map.Edge;
import model.game.map.Fence;
import model.game.map.Map;
import model.game.map.Point;

public class MapChecker {
	private Map map;

	private List<Point> visitedPoints;
	public String validateNewFence(Map map, Point firstEnd, Point secondEnd) {
		this.map = map;
		int x0 = firstEnd.getX();
		int y0 = firstEnd.getY();
		int x1 = firstEnd.getX();
		int y1= firstEnd.getY();
		String error = "";
		//check if coordinates are on the board and don't occupy bottom and top cells
		if ((y0 == 0 || y0 == map.getLength()-1)|| (y1 == 0 || y1 == map.getLength()-1)) {
			error = "a fence can't be situated on bottom or top cells";
			return error;
		}
		if ((x0 > 8 || x0 <0)|| (x1 > 8 || x1< 0) || (y0 < 1 || y0 > map.getLength()-2) || (y1 < 1 || y1 > map.getLength()-2)) {
			error = "fence coordinates are out of the board";
			return error;
		}
		
		//check if coordinates are occupied by a pawn
		
		if ( (x0 == map.getRedLocation().getX() && y0 ==  map.getRedLocation().getY()) || (x0 == map.getBlueLocation().getX() && y0 == map.getBlueLocation().getY()) ) {
			error = "fence coordinates are occupied by a pawn";
			return error;
		}

		if ( (x1 == map.getRedLocation().getX() && y1 ==  map.getRedLocation().getY()) || (x1 == map.getBlueLocation().getX() && y1 ==  map.getBlueLocation().getY()) ) {
			error = "fence coordinates are occupied by a pawn";
			return error;
		}
		//check if coordinates are occupied by another fence
		if (map.getFenceList().fenceExists(firstEnd) || map.getFenceList().fenceExists(secondEnd)) {
			error = "fence coordinates are occupied by a fence";
			return error;
		}
		//check if the new fence closes any of the pawns
		if (!fenceClosesPawns(firstEnd, secondEnd)) {
			error = "fence closes a path to a pawn";
			return error;
		}
		
		return error;
	
	}
	
	private boolean fenceClosesPawns(Point firstEnd, Point secondEnd) {
		//build a non-oriented graph associated with the map
		Graph graph = buildGraph();
		//check if there are still at list two paths from the pawns: to the top and to the bottom cells
		if (searchPaths("blue",map.getBlueLocation(), graph) && searchPaths("red",map.getRedLocation(),graph)) {
			return true;
		}
		else return false;
	}
	
	private Graph buildGraph() {
		Graph graph = new Graph();
		//add horizontal edges
		for (int i=0; i<map.getLength(); i++) {
			for (int j=0; j<map.getLength()-1;j++) {
				graph.add(new Edge(new Point(j,i),new Point(j+1,i)));
			}
		}
		//add vertical edges
		for (int i=0; i<map.getLength()-1; i++) {
			for (int j=0; j<map.getLength();j++) {
				graph.add(new Edge(new Point(j,i),new Point(j,i+1)));
			}
		}
		//delete edges where fences disturb to move
		for (int i=0; i< map.getFenceList().getSize(); i++) {
			Fence fence = map.getFenceList().getFence(i);
			Point firstEnd = fence.getFirstEnd();
			Point secondEnd = fence.getSecondEnd();
			//delete paths connected with the first end
			graph = deletePaths(graph,firstEnd);
			graph = deletePaths(graph,secondEnd);
			
		}
		return graph;
		
	}
	
	private Graph deletePaths(Graph graph, Point fenceEnd) {
		Graph newGraph = new Graph(graph);
		if (fenceEnd.getX()==0) {
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()-1), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()+1), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()), new Point(fenceEnd.getX()+1,fenceEnd.getY())));
		}
		else if (fenceEnd.getX()==map.getLength()-1) {
			newGraph.delete(new Edge (new Point(fenceEnd.getX()-1,fenceEnd.getY()), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()-1), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()+1), new Point(fenceEnd.getX(),fenceEnd.getY())));
		}
		else {
			newGraph.delete(new Edge (new Point(fenceEnd.getX()-1,fenceEnd.getY()), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()-1), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()+1), new Point(fenceEnd.getX(),fenceEnd.getY())));
			newGraph.delete(new Edge (new Point(fenceEnd.getX(),fenceEnd.getY()), new Point(fenceEnd.getX()+1,fenceEnd.getY())));
		}
		return newGraph;
	}
	
	private boolean searchPaths(String colour,Point pawnLocation, Graph graph) {
		Point point;
		if (colour.equals("blue")) {
			point = new Point(0,map.getLength());
		}
		else {
			point = new Point(0,0);
			}
		visitedPoints= new ArrayList<Point>();
		boolean searchResult = searchPaths(point, pawnLocation,graph);
		return searchResult;
	}
	
	private boolean searchPaths(Point startPoint, Point finishPoint, Graph graph){
		List<Point> neighbours = graph.getNeihgbourVertexes(startPoint);
		
		for (int i=0;i<neighbours.size();i++){
			if (!visitedPoints.contains(neighbours.get(i))){
				if (neighbours.get(i).equals(finishPoint)){
					return true;
				}
				visitedPoints.add(neighbours.get(i));
				if (searchPaths(neighbours.get(i),finishPoint,graph)) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	
}
