package me.gorgeousone.camerapaths.spline;

import org.bukkit.util.Vector;

public interface Spline {
	
	Vector lookup(double t);
	double getLength();
	Vector interpolate(double t);
}
