package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.animation.AnimationHandler;
import me.gorgeousone.camerapaths.SessionHandler;
import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.cmdframework.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends BaseCommand {
	
	private final SessionHandler sessionHandler;
	private final AnimationHandler animationHandler;
	
	public StartCommand(SessionHandler sessionHandler, AnimationHandler animationHandler) {
		super("start");
		addAlias("s");
		
		this.sessionHandler = sessionHandler;
		this.animationHandler = animationHandler;
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		SplinePath path = sessionHandler.getCameraPath(player.getUniqueId());
		
		if (path.getPointCount() < 2) {
			sender.sendMessage("You need to add at least two keyframes to the path.");
			return;
		}
		
		animationHandler.startAnimation(player, path);
	}
}
