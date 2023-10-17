package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.SessionHandler;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgType;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgValue;
import me.gorgeousone.camerapaths.cmdframework.argument.Argument;
import me.gorgeousone.camerapaths.cmdframework.command.ArgCommand;
import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.util.RenderUtil;
import me.gorgeousone.camerapaths.util.VecUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class VisitKeyframeCommand extends ArgCommand {
	
	private final SessionHandler sessionHandler;
	
	public VisitKeyframeCommand(SessionHandler sessionHandler) {
		super("visit");
		addAlias("v");
		addArg(new Argument("keyframe", ArgType.INTEGER).setDefault("0"));
		
		this.sessionHandler = sessionHandler;
	}
	
	@Override
	protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
		Player player = (Player) sender;
		
		SplinePath path = sessionHandler.getCameraPath(player.getUniqueId());
		int index = argValues.get(0).getInt();
		
		if (index < 1 || index > path.getPointCount()) {
			sender.sendMessage(ChatColor.RED + "Choose a keyframe between 1 and " + path.getPointCount() + ".");
		}
		Location pos = path.getPoints().get(index - 1).toLocation(player.getWorld());
		Vector view = path.getViews().get(index - 1);
		pos.setYaw((float) view.getX());
		pos.setPitch((float) view.getY());
		pos.subtract(0, RenderUtil.EYE_HEIGHT, 0);
		player.teleport(pos);
		
		player.sendMessage(VecUtil.strVec(view));
	}
}
