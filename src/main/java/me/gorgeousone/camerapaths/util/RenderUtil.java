package me.gorgeousone.camerapaths.util;

import me.gorgeousone.camerapaths.spline.SplinePath;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RenderUtil {
	
	public static void renderPath(Player player, SplinePath path) {
		double length = path.getLength();
		
		for (double dist = 0; dist < length; dist += 1) {
			Vector point = path.getPoint(dist);
			player.spawnParticle(Particle.VILLAGER_HAPPY, point.getX(), point.getY(), point.getZ(), 0, 0f, 0f, 0f);
		}
		for (Vector node : path.getPoints()) {
			player.spawnParticle(Particle.HEART, node.getX(), node.getY(), node.getZ(), 0, 0f, 0f, 0f, 0.5f);
		}
	}
}
