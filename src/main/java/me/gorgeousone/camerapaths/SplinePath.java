package me.gorgeousone.camerapaths;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SplinePath {
	
	List<Vector> points;
	List<Vector> velocities;
	List<Spline> splines;
	List<Double> splineLengths;
	
	public SplinePath() {
		points = new ArrayList<>();
		velocities = new ArrayList<>();
		splines = new ArrayList<>();
		splineLengths = new ArrayList<>();
	}
	
	public int addPoint(int index, Vector point) {
		if (index == -1) {
			index = points.size();
		}
		points.add(index, point);
		calcVelocities();
		calcSplines();
		return index;
	}
	
	public void removePoint(int index) {
		points.remove(index);
		calcVelocities();
		calcSplines();
	}
	
	public double getLength() {
		if (splineLengths.isEmpty()) {
			return 0;
		}
		return splineLengths.get(splineLengths.size() - 1);
	}
	
	public Vector getPoint(double dist) {
		if (splines.isEmpty()) {
			return new Vector();
		}
		int index = 1;
		
		while (index < splines.size() && splineLengths.get(index) < dist) {
			++index;
		}
		double subDist = dist - splineLengths.get(index - 1);
		return splines.get(index - 1).lookup(subDist);
	}
	
	private void calcVelocities() {
		velocities.clear();
		velocities.add(new Vector());
		
		for (int i = 1; i < points.size() - 1; ++i) {
//			velocities.add(calcCatmullRomVelocity(points.get(i - 1), points.get(i + 1)));
			velocities.add(calcSmoothVelocity(points.get(i), points.get(i - 1), points.get(i + 1)));
		}
		if (points.size() > 1) {
			velocities.add(new Vector());
		}
	}
	
	private void calcSplines() {
		splines.clear();
		splineLengths.clear();
		splineLengths.add(0.0);
		
		double totalLength = 0;
		
		for (int i = 0; i < points.size() - 1; ++i) {
			Vector p0 = points.get(i);
			Vector p1 = points.get(i + 1);
			double dist = p0.distance(p1);
			double strength = dist;
			
			Vector v0 = velocities.get(i).clone().multiply(strength);
			Vector v1 = velocities.get(i + 1).clone().multiply(strength);
			Spline spline = new HermiteSpline(p0, p1, v0, v1, 100);
			
//			Spline spline = new HermiteSpline(points.get(i), points.get(i + 1), velocities.get(i), velocities.get(i + 1), 100);
//			Spline spline = new LinearSpline(points.get(i), points.get(i + 1));
			splines.add(spline);
			
			totalLength += spline.getLength();
			splineLengths.add(totalLength);
		}
	}
	
	private Vector calcCatmullRomVelocity(Vector p0, Vector p2) {
		return p2.clone().subtract(p0).multiply(0.5);
	}
	
	private Vector calcSmoothVelocity(Vector vertex, Vector previous, Vector next) {
		Vector directionBackwards = vertex.clone().subtract(previous).normalize();
		Vector directionForwards = next.clone().subtract(vertex).normalize();
		return directionBackwards.add(directionForwards).normalize();
	}
	
	private String strVec(Vector vec) {
		return String.format("(%.2f, %.2f, %.2f)", vec.getX(), vec.getY(), vec.getZ());
	}
}
