package game.command;

import game.map.Fence;

import org.json.simple.JSONObject;

public class BuildFenceCommand extends Command {
 private Fence fence;
 


public Fence getFence() {
	return fence;
}

public BuildFenceCommand(String gameId,String playerColour,Fence fence) {
	super(gameId,playerColour);
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
