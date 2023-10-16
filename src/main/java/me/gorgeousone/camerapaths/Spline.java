package me.gorgeousone.camerapaths;

import org.bukkit.util.Vector;

public interface Spline {
	
	Vector lookup(double t);
	double getLength();
}
