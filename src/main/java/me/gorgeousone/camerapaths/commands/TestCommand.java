package me.gorgeousone.camerapaths.commands;

import me.gorgeousone.camerapaths.cmdframework.command.BaseCommand;
import me.gorgeousone.camerapaths.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TestCommand extends BaseCommand {
	
	private final JavaPlugin plugin;
	
	public TestCommand(JavaPlugin plugin) {
		super("test");
		addAlias("t");
		
		this.plugin = plugin;
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		sender.sendMessage("Test command executed!");
		
		Player player = (Player) sender;
		Location spawn = player.getLocation();
		Vector dir = spawn.getDirection();
		spawn.add(dir.getX(), 0, dir.getZ());
		
		LivingEntity entity = (LivingEntity) spawn.getWorld().spawnEntity(spawn, EntityType.CHICKEN);
		entity.setAI(false);
		entity.addPassenger(player);
		
		Vector entityDir = dir.crossProduct(new Vector(0, 1, 0)).multiply(0.5);
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				PacketUtil.sendEntityMove(player, entity, entityDir, true);
				
				if (ticks++ > 100) {
					System.out.println();
					entity.remove();
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
}
