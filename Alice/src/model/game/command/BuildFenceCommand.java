package model.game.command;

import org.json.simple.JSONObject;

import model.game.map.Fence;

public class BuildFenceCommand extends Command {
 private Fence fence;
 


public Fence getFence() {
	return fence;
}

public BuildFenceCommand(String colour,String gameId,Fence fence) {
	super(colour,gameId);
	this.fence = fence;
	this.type = "build_fence";
}

public void setFence(Fence fence) {
	this.fence = fence;
}
@Override
public String toJSONString() {
	JSONObject resultJson = new JSONObject();
    resultJson = new JSONObject();
    resultJson.put("type",type );
    resultJson.put("player_colour", playerColour);
    resultJson.put("coordinates", fence.toJSONString());
    return resultJson.toString();
        
}
}
