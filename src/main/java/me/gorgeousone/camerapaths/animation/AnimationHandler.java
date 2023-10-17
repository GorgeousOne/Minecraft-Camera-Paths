package me.gorgeousone.camerapaths.animation;

import me.gorgeousone.camerapaths.spline.SplinePath;
import me.gorgeousone.camerapaths.util.NmsUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class AnimationHandler {
	
	private final static double EYE_HEIGHT = 1.62;
	
	private final JavaPlugin plugin;
	private BukkitRunnable animator;
	private final HashMap<UUID, Animation> playerAnimations;
	private final HashMap<UUID, UUID> playerGizmos;
	private final HashMap<UUID, Vector> lastPlayerPositions;
	
	public AnimationHandler(JavaPlugin plugin) {
		this.plugin = plugin;
		playerAnimations = new HashMap<>();
		playerGizmos = new HashMap<>();
		lastPlayerPositions = new HashMap<>();
		startAnimating();
	}
	
	public void startAnimation(Player player, SplinePath path) {
		UUID playerId = player.getUniqueId();
		playerAnimations.put(playerId, new Animation(path, System.currentTimeMillis(), 5 * 1000));
		Location startLoc = path.getPoint(0).toLocation(player.getWorld());
		
		ArmorStand gizmo = player.getWorld().spawn(startLoc, ArmorStand.class);
		gizmo.setGravity(false);
		gizmo.setVisible(false);
		gizmo.setInvulnerable(true);
		gizmo.setCollidable(false);
		
		player.setGameMode(GameMode.SPECTATOR);
		player.setSpectatorTarget(gizmo);
		
		playerGizmos.put(playerId, gizmo.getUniqueId());
		lastPlayerPositions.put(playerId, startLoc.toVector());
		player.sendMessage("Animation started.");
	}
	
	private void startAnimating() {
		animator = new BukkitRunnable() {
			
			@Override
			public void run() {
				Iterator<Map.Entry<UUID, Animation>> iterator = playerAnimations.entrySet().iterator();
				
				while (iterator.hasNext()) {
					Map.Entry<UUID, Animation> entry = iterator.next();
					UUID playerId = entry.getKey();
					Player player = Bukkit.getPlayer(playerId);
					
					if (!animatePlayer(player, entry.getValue())) {
						iterator.remove();
						playerGizmos.remove(playerId);
						lastPlayerPositions.remove(playerId);
						player.sendMessage("Animation ended.");
					}
				}
			}
		};
		animator.runTaskTimer(plugin, 0, 1);
	}
	
	private boolean animatePlayer(Player player, Animation animation) {
		UUID playerId = player.getUniqueId();
		Vector lastPos = lastPlayerPositions.get(playerId);
		Vector nextPos = animation.getPoint(System.currentTimeMillis());
		Vector relativePos = nextPos.clone().subtract(lastPos);
		
		Entity gizmo = Bukkit.getEntity(playerGizmos.get(playerId));
		
		try {
			NmsUtil.smoothMoveEntity(gizmo, relativePos);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
			player.sendMessage("DEBUG error while animating");
			return false;
		}
		lastPlayerPositions.put(playerId, nextPos);
		
		if (player.getSpectatorTarget() != gizmo || animation.getProgress(System.currentTimeMillis()) == 1) {
			gizmo.remove();
			player.setGameMode(GameMode.CREATIVE);
			return false;
		}
		return true;
	}
}
