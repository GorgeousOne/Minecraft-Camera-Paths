package me.gorgeousone.camerapaths;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

public final class PacketUtil {
	
	private static final boolean useMovementPacket1_14 = VersionUtil.serverIsAtOrAbove("1.14");
	private static final boolean useEquipmentPacket1_9 = VersionUtil.serverIsAtOrAbove("1.9");
	private static final boolean usePositionPacket1_9 = useEquipmentPacket1_9;
	
	
	private static void sendPacket(Player player, PacketContainer packet) {
		if (packet == null) {
			return;
		}
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Failed to send packet " + packet, e);
		}
	}
	
	public static void sendPlayerMove(Player player,
			Vector relMove,
			double newYaw,
			double newPitch,
			boolean isOnGround) {
		
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		PacketContainer moveLookPacket = protocolManager.createPacket(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
		
		moveLookPacket.getIntegers().write(0, player.getEntityId());
		
		if (useMovementPacket1_14) {
			
			moveLookPacket.getShorts()
					.write(0, (short) (relMove.getX() * 4096))
					.write(1, (short) (relMove.getY() * 4096))
					.write(2, (short) (relMove.getZ() * 4096));
			
		} else if (usePositionPacket1_9) {
			
			moveLookPacket.getIntegers()
					.write(1, (int) (relMove.getX() * 4096))
					.write(2, (int) (relMove.getY() * 4096))
					.write(3, (int) (relMove.getZ() * 4096));
		} else {
			
			moveLookPacket.getBytes()
					.write(1, (byte) (relMove.getX() * 4096))
					.write(2, (byte) (relMove.getY() * 4096))
					.write(3, (byte) (relMove.getZ() * 4096));
		}
		
		moveLookPacket.getBytes()
				.write(0, (byte) (newYaw * 256 / 360))
				.write(1, (byte) (newPitch * 256 / 360));
		
		//no idea what field 1 does, seems to be always true
		moveLookPacket.getBooleans()
				.write(0, isOnGround)
				.write(1, true);
		
		PacketContainer headRotPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
		headRotPacket.getIntegers().write(0, player.getEntityId());
		headRotPacket.getBytes().write(0, (byte) (int) (newYaw * 265 / 360));
		
		sendPacket(player, moveLookPacket);
		sendPacket(player, headRotPacket);
	}
}
