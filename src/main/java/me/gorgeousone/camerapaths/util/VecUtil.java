package me.gorgeousone.camerapaths.util;

import org.bukkit.util.Vector;

public class VecUtil {
	
	public static Vector lerp(Vector a, Vector b, double t) {
		return a.clone().multiply(1 - t).add(b.clone().multiply(t));
	}
	
	public static String strVec(Vector vec) {
		return String.format("(%.2f, %.2f, %.2f)", vec.getX(), vec.getY(), vec.getZ());
	}
}
