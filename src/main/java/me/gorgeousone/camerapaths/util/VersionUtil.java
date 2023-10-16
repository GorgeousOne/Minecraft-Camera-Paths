package me.gorgeousone.camerapaths.util;
import org.bukkit.Bukkit;

public final class VersionUtil {
	
	private VersionUtil() {}
	
	public static final String VERSION_STRING = Bukkit.getServer().getClass().getName().split("\\.")[3];
	private static final int[] CURRENT_VERSION_INTS = new int[3];
	
	static {
		String versionStringNumbersOnly = VERSION_STRING.replaceAll("[a-zA-Z]", "");
		System.arraycopy(getVersionAsIntArray(versionStringNumbersOnly, "_"), 0, CURRENT_VERSION_INTS, 0, 3);
	}

	public static boolean serverIsAtOrAbove(String requestedVersion) {
		int[] requestedVersionInts = getVersionAsIntArray(requestedVersion, "\\.");
		
		for (int i = 0; i < requestedVersionInts.length; i++) {
			int versionDiff = requestedVersionInts[i] - CURRENT_VERSION_INTS[i];
			
			if (versionDiff > 0) {
				return false;
			}else if (versionDiff < 0){
				return true;
			}
		}
		return true;
	}
	
	private static int[] getVersionAsIntArray(String version, String delimiter) {
		String[] split = version.split(delimiter);
		
		if (split.length > 3) {
			throw new IllegalArgumentException("Cannot process awfully long version string \"" + version + "\".");
		}
		int[] versionInts = new int[split.length];
		
		for (int i = 0; i < versionInts.length; i++) {
			versionInts[i] = Integer.parseInt(split[i]);
		}
		return versionInts;
	}
}