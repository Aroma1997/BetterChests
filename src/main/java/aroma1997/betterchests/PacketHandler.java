/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

public class PacketHandler{}

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//
//import aroma1997.core.inventories.Inventories;
//import aroma1997.core.log.LogHelper;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.network.INetworkManager;
//import net.minecraft.network.packet.Packet250CustomPayload;
//
//import cpw.mods.fml.common.network.IPacketHandler;
//import cpw.mods.fml.common.network.PacketDispatcher;
//import cpw.mods.fml.common.network.Player;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//public class PacketHandler implements IPacketHandler {
//	
//	public static final int BAG_ID = 0;
//	
//	public static final String CHANNEL = "BetterChests";
//	
//	@Override
//	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
//		if (! packet.channel.equals(CHANNEL)) {
//			return;
//		}
//		ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
//		DataInputStream stream = new DataInputStream(bis);
//		try {
//			int key = stream.readByte();
//			switch (key) {
//				case BAG_ID: {
//					openPlayerBagGUI(manager, packet, player, stream);
//					break;
//				}
//			}
//		}
//		catch (IOException e) {
//			LogHelper.logException("Failed to unbuild packet.", e);
//		}
//	}
//	
//	@SideOnly(Side.CLIENT)
//	public static void sendPacketBag(int i) {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		DataOutputStream stream = new DataOutputStream(bos);
//		try {
//			stream.writeByte(BAG_ID);
//			stream.writeByte(i);
//		}
//		catch (IOException e) {
//			LogHelper.logException("Failed to build packet.", e);
//		}
//		byte[] bytes = bos.toByteArray();
//		Packet250CustomPayload packet = new Packet250CustomPayload(CHANNEL, bytes);
//		packet.length = bytes.length;
//		PacketDispatcher.sendPacketToServer(packet);
//	}
//	
//	private void openPlayerBagGUI(INetworkManager manager, Packet250CustomPayload packet,
//		Player player, DataInputStream stream) {
//		EntityPlayer thePlayer = (EntityPlayer) player;
//		int c;
//		try {
//			c = stream.readByte();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//			c = - 1;
//		}
//		if (c == - 1) {
//			for (int i = thePlayer.inventory.getSizeInventory() - 1; i >= 0; i--) {
//				ItemStack item = thePlayer.inventory.getStackInSlot(i);
//				if (item == null) {
//					continue;
//				}
//				else if (item.getItem() instanceof ItemBag) {
//					Inventories.openContainerAtPlayer(thePlayer, i);
//					return;
//				}
//			}
//		}
//		else {
//			Inventories.openContainerAtPlayer(thePlayer, c);
//		}
//	}
//	
//}
