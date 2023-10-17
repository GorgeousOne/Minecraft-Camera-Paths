package main.java.me.gorgeousone.camerapaths.spline;

import me.gorgeousone.camerapaths.spline.SplinePath;
import org.bukkit.util.Vector;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SplinePathTest {
	
	@Test
	public void testYawCorrection() {
		SplinePath path = new SplinePath();
		path.addPoint(-1, new Vector(0, 0, 0), 120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), -120, 0);
		assertEquals(240, path.getViews().get(1).getX());
	}
	
	@Test
	public void testNegativeYawCorrection() {
		SplinePath path = new SplinePath();
		path.addPoint(-1, new Vector(0, 0, 0), -120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 120, 0);
		assertEquals(-240, path.getViews().get(1).getX());
	}
	
	@Test
	public void testMultipleYawCorrections() {
		SplinePath path = new SplinePath();
		path.addPoint(-1, new Vector(0, 0, 0), 120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), -120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 0, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), -120, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 0, 0);
		assertEquals(240, path.getViews().get(1).getX());
		assertEquals(360, path.getViews().get(2).getX());
		assertEquals(480, path.getViews().get(3).getX());
		assertEquals(600, path.getViews().get(4).getX());
		assertEquals(720, path.getViews().get(5).getX());
	}
	
	@Test
	public void testNoYawCorrection() {
		SplinePath path = new SplinePath();
		path.addPoint(-1, new Vector(0, 0, 0), -90, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 0, 0);
		path.addPoint(-1, new Vector(0, 0, 0), 90, 0);
		assertEquals(0, path.getViews().get(1).getX());
		assertEquals(90, path.getViews().get(2).getX());
	}
}
