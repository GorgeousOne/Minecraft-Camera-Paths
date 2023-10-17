package me.gorgeousone.camerapaths.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NmsUtil {
	
	public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	private static final Class<?> craftEntityClass;
	private static final Method getHandleMethod;
	private static final Class<?> nsmEntityClass;
	private static final Method setPositionRotationMethod;
	
	
	static {
		try {
			craftEntityClass = getCraftClass("entity.CraftEntity");
			getHandleMethod = craftEntityClass.getMethod("getHandle");
			
			nsmEntityClass = getNMSClass("Entity");
			setPositionRotationMethod = nsmEntityClass.getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static Class<?> getCraftClass(String craftClassString) throws ClassNotFoundException {
		String name = "org.bukkit.craftbukkit." + NMS_VERSION + craftClassString;
		return Class.forName(name);
	}
	
	public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
		String name = "net.minecraft.server." + NMS_VERSION + nmsClassString;
		return Class.forName(name);
	}
	
	public static void smoothMoveEntity(Entity entity, Vector distance) throws InvocationTargetException, IllegalAccessException {
		smoothTpEntity(entity, entity.getLocation().add(distance));
	}
	
	public static void smoothTpEntity(Entity entity, Location location) throws InvocationTargetException, IllegalAccessException {
		Object craftEntity = craftEntityClass.cast(entity);
		Object handle = getHandleMethod.invoke(craftEntity);
		setPositionRotationMethod.invoke(handle, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
}
