package me.gorgeousone.camerapaths.util;

import org.bukkit.util.Vector;

public class VecUtil {
	
	public static Vector lerp(Vector a, Vector b, double t) {
		return a.clone().multiply(1 - t).add(b.clone().multiply(t));
	}
	
	public static String strVec(Vector vec) {
		return String.format("(%.2f, %.2f, %.2f)", vec.getX(), vec.getY(), vec.getZ());
	}
	
	public static double getClosestYaw(double yaw, double otherYaw) {
		double delta = otherYaw - yaw;
		double absDelta = Math.abs(delta);
		int sign = (int) Math.signum(delta);
		absDelta %= 360;
		
		if (absDelta > 180) {
			absDelta = 360 - absDelta;
			sign *= -1;
		}
		return yaw + Math.copySign(absDelta, sign);
	}
}
