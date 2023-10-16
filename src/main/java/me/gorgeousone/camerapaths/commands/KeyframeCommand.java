package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.SessionHandler;
import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgType;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgValue;
import me.gorgeousone.camerapaths.cmdframework.argument.Argument;
import me.gorgeousone.camerapaths.cmdframework.command.ArgCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class KeyframeCommand extends ArgCommand {
	
	private final SessionHandler sessionHandler;
	
	public KeyframeCommand(SessionHandler sessionHandler) {
		super("keyframe");
		addAlias("kf");
		
		addArg(new Argument("index", ArgType.INTEGER).setDefault("-1"));
		
		this.sessionHandler = sessionHandler;
	}
	
	@Override
	protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
		Player player = (Player) sender;
		
		SplinePath path = sessionHandler.getCameraPath(player.getUniqueId());
		Vector pos = player.getEyeLocation().toVector();
		int index = path.addPoint(argValues.get(0).getInt(), pos);
		
		sender.sendMessage(ChatColor.GRAY + "Added keyframe at index " + index + ".");
	}
}
