package game.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


public class FenceList {

	private List<Fence> fences = new ArrayList();
	
	public void add(Fence fence) {
		fences.add(fence);
	}
	
	
	public boolean contains(Fence fence) {
		if (fences.contains(fence) ) {
			return true;
		}
		else if (fences.contains(new Fence(fence.getSecondEnd(),fence.getFirstEnd()))) {
			return true;
		}
		else return false;
	}
	public boolean fenceExists(Point point) {
	
		for (int i =0; i < fences.size();i++) {
			Fence fence = fences.get(i);
			if (fence.firstEnd.getX() == point.getX() && fence.firstEnd.getY()==point.getY()) {
				return true;
			}
			else if (fence.secondEnd.getX() == point.getX() && fence.secondEnd.getY()==point.getY()) {
				return true;
			}
			
		}
		return false;
	}
	
	public int getSize() {
		return fences.size();
	}
	
	public Fence getFence(int index) {
		return fences.get(index);
	}
}
