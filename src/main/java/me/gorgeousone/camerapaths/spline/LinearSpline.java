package me.gorgeousone.camerapaths.spline;

import org.bukkit.util.Vector;

public class LinearSpline implements Spline {
	
	private final Vector p0;
	private final Vector p1;
	
	public LinearSpline(Vector p0, Vector p1) {
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public double getLength() {
		return p0.distance(p1);
	}
	
	//TODO: use binary search :D
	public Vector lookup(double dist) {
		if (dist >= getLength() || dist < 0) {
			throw new IllegalArgumentException("Distance is greater than spline length." +
					" Distance: " + dist + ", length: " + getLength());
		}
		
		double t = dist / getLength();
		return p0.clone().multiply(1 - t).add(p1.clone().multiply(t));
	}
	
	
}
