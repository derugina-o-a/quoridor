package model;

import model.game.Fence;

public class BuildFenceCommand extends Command {
 private Fence fence;
 


public Fence getFence() {
	return fence;
}

public BuildFenceCommand(String gameId,Fence fence) {
	super(gameId);
	this.fence = fence;
	this.type = "build_fence";
}

public void setFence(Fence fence) {
	this.fence = fence;
}
}
