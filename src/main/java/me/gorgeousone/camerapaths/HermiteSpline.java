package me.gorgeousone.camerapaths;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

class HermitSpline {
	private final Vector p0;
	private final Vector p1;
	private final Vector v0;
	private final Vector v1;
	private List<Vector> lookupTable;
	private List<Double> lengthLookupTable;
	
//	public CatmullRomSpline(Vector v0, Vector v1, Vector v2, Vector v3, int segments) {
//		this.p1 = v1;
//		this.p2 = v2;
//		this.v1 = v2.clone().subtract(v0).multiply(0.5);
//		this.v2 = v3.clone().subtract(v1).multiply(0.5);
//		generateLookupTable(segments);
//	}
	
	public HermitSpline(Vector p0, Vector p1, Vector v0, Vector v1, int segments) {
		this.p0 = p0;
		this.p1 = p1;
		this.v0 = v0;
		this.v1 = v1;
		generateLookupTable(segments);
	}
	
	private void generateLookupTable(int segments) {
		lookupTable = new ArrayList<>();
		lengthLookupTable = new ArrayList<>();
		
		double step = 1.0 / (segments - 1);
		Vector lastPoint = null;
		double t = 0;
		double totalLength = 0;
		
		for (int i = 0; i < segments; i++) {
			Vector point = interpolate(t);
			lookupTable.add(point);
			
			if (i > 0) {
				double length = point.distance(lastPoint);
				totalLength += length;
				lengthLookupTable.add(totalLength);
			}
			lastPoint = point;
			t += step;
		}
	}
	
	public double getApproxLength() {
		return lengthLookupTable.get(lengthLookupTable.size() - 1);
	}
	
	//TODO: use binary search :D
	public Vector lookup(double dist) {
		if (dist <= 0) {
			return lookupTable.get(0);
		}
		if (dist >= getApproxLength()) {
			return lookupTable.get(lookupTable.size() - 1);
		}
		int index = 0;
		while (lengthLookupTable.get(index) < dist) {
			++index;
		}
		double lengthBefore = lengthLookupTable.get(index - 1);
		double segmentLength = lengthLookupTable.get(index) - lengthBefore;
		double segmentDist = dist - lengthBefore;
		double t = segmentDist / segmentLength;
		
		return interpolate(t);
	}
	
	public Vector interpolate(double t) {
		double t2 = t * t;
		double t3 = t2 * t;
		return new Vector(
				hermite(p0.getX(), p1.getX(), v0.getX(), v1.getX(), t, t2, t3),
				hermite(p0.getY(), p1.getY(), v0.getY(), v1.getY(), t, t2, t3),
				hermite(p0.getZ(), p1.getZ(), v0.getZ(), v1.getZ(), t, t2, t3)
		);
	}
	
	private double hermite(double p1, double p2, double v1, double v2, double t, double t2, double t3) {
		double h1 = 2 * t3 - 3 * t2 + 1;
		double h2 = -2 * t3 + 3 * t2;
		double h3 = t3 - 2 * t2 + t;
		double h4 = t3 - t2;
		return h1 * p1 + h2 * p2 + h3 * v1 + h4 * v2;
	}
}