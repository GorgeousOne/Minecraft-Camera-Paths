package me.gorgeousone.camerapaths.spline;

import me.gorgeousone.camerapaths.util.VecUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SplinePath {
	
	List<Vector> points;
	List<Vector> views;
	
	List<Vector> pointVelocities;
	List<Vector> viewVelocities;

	List<Spline> pointSplines;
	List<Spline> viewSplines;
	
	List<Double> splineLengths;
	
	public SplinePath() {
		points = new ArrayList<>();
		views = new ArrayList<>();
		pointVelocities = new ArrayList<>();
		viewVelocities = new ArrayList<>();
		pointSplines = new ArrayList<>();
		viewSplines = new ArrayList<>();
		splineLengths = new ArrayList<>();
	}
	
	
	public int addPoint(int index, Vector point, float yaw, float pitch) {
		if (index == -1) {
			index = points.size();
		}
		points.add(index, point.clone());
		views.add(index, new Vector(yaw, pitch, 0));
		//TODO: only recalculate affected splines
		calcSplines();
		return index;
	}
	
	public void removePoint(int index) {
		points.remove(index);
		calcSplines();
	}
	
	public double getLength() {
		if (splineLengths.isEmpty()) {
			return 0;
		}
		return splineLengths.get(splineLengths.size() - 1);
	}
	
	public Location getPoint(double dist) {
		if (pointSplines.isEmpty()) {
			return new Location(null, 0, 0, 0);
		}
		int index = 1;
		
		while (index < pointSplines.size() && splineLengths.get(index) < dist) {
			++index;
		}
		double subDist = dist - splineLengths.get(index - 1);
		
		Location point = pointSplines.get(index - 1).lookup(subDist).toLocation(null);
		double t = subDist / pointSplines.get(index - 1).getLength();
		Vector view = viewSplines.get(index - 1).interpolate(t);
		
		point.setYaw((float) view.getX());
		point.setPitch((float) view.getY());
		return point;
	}
	
	private void calcSplines() {
		fixYaws();
		calcVelocities();
		
		pointSplines.clear();
		viewSplines.clear();
		splineLengths.clear();
		splineLengths.add(0.0);
		
		double totalLength = 0;
		
		for (int i = 0; i < points.size() - 1; ++i) {
			Spline pointSpline = calcHermiteSpline(i, points, pointVelocities);
			Spline viewSpline = calcHermiteSpline(i, views, viewVelocities);
			
			pointSplines.add(pointSpline);
			viewSplines.add(viewSpline);
			
			totalLength += pointSpline.getLength();
			splineLengths.add(totalLength);
		}
	}
	
	/**
	 * Make yaws between keyframes interpolate shortest angles.
	 * Example: keyframe1: yaw = -179, keyframe2: yaw2 = 179 -> yaw2 = -181
	 * (may lead to absurdly increasing yaw values, but it is what it is)
	 */
	private void fixYaws() {
		double yaw1 = views.get(0).getX();
		
		for (int i = 1; i < views.size(); ++i) {
			Vector view2 = views.get(i);
			yaw1 = VecUtil.getClosestYaw(yaw1, view2.getX());
			view2.setX(yaw1);
		}
	}
	
	private void calcVelocities() {
		pointVelocities.clear();
		viewVelocities.clear();
		pointVelocities.add(new Vector());
		viewVelocities.add(new Vector());
		
		for (int i = 1; i < points.size() - 1; ++i) {
			pointVelocities.add(calcSmoothVelocity(points.get(i), points.get(i - 1), points.get(i + 1)));
			viewVelocities.add(calcSmoothVelocity(views.get(i), views.get(i - 1), views.get(i + 1)));
		}
		if (points.size() > 1) {
			pointVelocities.add(new Vector());
			viewVelocities.add(new Vector());
		}
	}
	
	private HermiteSpline calcHermiteSpline(int i, List<Vector> nodes, List<Vector> velocities) {
		Vector p0 = nodes.get(i);
		Vector p1 = nodes.get(i + 1);
		double tension = p0.distance(p1);
		
		Vector v0 = velocities.get(i).clone().multiply(tension);
		Vector v1 = velocities.get(i + 1).clone().multiply(tension);
		return new HermiteSpline(p0, p1, v0, v1, 100);
	}
	
	private Vector calcSmoothVelocity(Vector vertex, Vector previous, Vector next) {
		Vector directionBackwards = vertex.clone().subtract(previous).normalize();
		Vector directionForwards = next.clone().subtract(vertex).normalize();
		return directionBackwards.add(directionForwards).normalize();
	}
	
	public int getPointCount() {
		return points.size();
	}
	
	public void clear() {
		points.clear();
		pointVelocities.clear();
		pointSplines.clear();
		splineLengths.clear();
	}
	
	public List<Vector> getPoints() {
		return points;
	}
	
	public List<Vector> getViews() {
		return views;
	}
}
