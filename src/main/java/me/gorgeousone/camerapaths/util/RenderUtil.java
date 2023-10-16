package me.gorgeousone.camerapaths.util;

import me.gorgeousone.camerapaths.spline.SplinePath;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RenderUtil {
	
	public static void renderPath(Player player, SplinePath path) {
		double length = path.getLength();
		
		for (double dist = 0; dist < length; dist += 0.5) {
			Vector point = path.getPoint(dist);
			player.spawnParticle(Particle.VILLAGER_HAPPY, point.getX(), point.getY(), point.getZ(), 0, 0f, 0f, 0f);
		}
	}
}
