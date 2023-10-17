package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.animation.AnimationHandler;
import me.gorgeousone.camerapaths.SessionHandler;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgType;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgValue;
import me.gorgeousone.camerapaths.cmdframework.argument.Argument;
import me.gorgeousone.camerapaths.cmdframework.command.ArgCommand;
import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.cmdframework.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class StartCommand extends ArgCommand {
	
	private final SessionHandler sessionHandler;
	private final AnimationHandler animationHandler;
	
	public StartCommand(SessionHandler sessionHandler, AnimationHandler animationHandler) {
		super("start");
		addAlias("s");
		addArg(new Argument("duration", ArgType.DECIMAL).setDefault("10"));
		
		this.sessionHandler = sessionHandler;
		this.animationHandler = animationHandler;
	}
	
	@Override
	protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
		Player player = (Player) sender;
		SplinePath path = sessionHandler.getCameraPath(player.getUniqueId());
		
		double duration = argValues.get(0).getDouble();
		
		if (path.getPointCount() < 2) {
			sender.sendMessage("You need to add at least two keyframes to the path.");
			return;
		}
		animationHandler.startAnimation(player, path, duration);
	}
}
