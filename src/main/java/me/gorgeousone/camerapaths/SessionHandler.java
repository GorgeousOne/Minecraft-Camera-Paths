package me.gorgeousone.camerapaths;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SessionHandler {
	
	private final JavaPlugin plugin;
	
	private HashMap<UUID, SplinePath> playerCameraPaths;
	private BukkitRunnable pathRenderer;
	
	public SessionHandler(JavaPlugin plugin) {
		playerCameraPaths = new HashMap<>();
		this.plugin = plugin;
		startRendering();
	}
	
	public SplinePath getCameraPath(UUID player) {
		return playerCameraPaths.computeIfAbsent(player, id -> new SplinePath());
	}
	
	public void removeCameraPath(UUID player) {
		playerCameraPaths.remove(player);
	}
	
	private void startRendering() {
		pathRenderer = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (UUID id : playerCameraPaths.keySet()) {
					SplinePath path = playerCameraPaths.get(id);
					Player player = Bukkit.getPlayer(id);
					RenderUtil.renderPath(player, path);
				}
			}
		};
		pathRenderer.runTaskTimer(plugin, 0, 20);
	}
}
