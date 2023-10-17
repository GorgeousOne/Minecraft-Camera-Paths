package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.SessionHandler;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgType;
import me.gorgeousone.camerapaths.cmdframework.argument.ArgValue;
import me.gorgeousone.camerapaths.cmdframework.argument.Argument;
import me.gorgeousone.camerapaths.cmdframework.command.ArgCommand;
import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.util.RenderUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class TestCommand extends ArgCommand {

	private final SessionHandler sessionHandler;
	
	public TestCommand(SessionHandler sessionHandler) {
		super("test");
		addAlias("t");
		addArg(new Argument("nodes", ArgType.INTEGER).setDefault("4"));
		addArg(new Argument("distance", ArgType.DECIMAL).setDefault("30"));
		addArg(new Argument("range", ArgType.DECIMAL).setDefault("5"));

		this.sessionHandler = sessionHandler;
	}
	
	@Override
	protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
		Player player = (Player) sender;
		int nodes = argValues.get(0).getInt();
		double distance = argValues.get(1).getDouble();
		double range = argValues.get(2).getDouble();
		
		double nodeDist = distance / (nodes - 1);
		Location loc = player.getLocation();
		Vector dir = loc.getDirection();
		Vector ortho = new Vector(dir.getZ(), 0, -dir.getX());
		Vector start = loc.toVector();
		
		dir.multiply(nodeDist);
		ortho.multiply(range);
		
		SplinePath path = sessionHandler.getCameraPath(player.getUniqueId());
		path.clear();
		
		for (int i = 0; i < nodes; ++i) {
			int sign = i % 2 == 0 ? 1 : -1;
			path.addPoint(-1, start);
			start.add(dir);
			start.add(ortho.clone().multiply(sign));
			
			if (i != 0 && i != nodes - 1) {
				start.add(ortho);
			}
		}
		RenderUtil.renderPath(player, path);
	}
}
