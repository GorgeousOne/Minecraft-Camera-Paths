package me.gorgeousone.camerapaths;

import org.bukkit.util.Vector;

public class VecUtil {
	
	public static Vector lerp(Vector a, Vector b, double t) {
		return a.clone().multiply(1 - t).add(b.clone().multiply(t));
	}
}
