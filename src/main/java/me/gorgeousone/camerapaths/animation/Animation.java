package me.gorgeousone.camerapaths.animation;

import me.gorgeousone.camerapaths.spline.SplinePath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Animation {
	
	private SplinePath path;
	private long start;
	private long duration;
	
	public Animation(SplinePath path, long start, long duration) {
		this.path = path;
		this.duration = duration;
		this.start = start;
	}
	
	public void setStart(long time) {
		start = time;
	}
	
	public SplinePath getPath() {
		return path;
	}
	
	public long getStart() {
		return start;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public double getProgress(long time) {
		double progress = (time - start) / (double) duration;
		return Math.min(1, Math.max(0, progress));
	}
	
	public Location getPoint(long time, World world) {
		Location point = path.getPoint(getProgress(time) * path.getLength());
		point.setWorld(world);
		return point;
	}
}
