package aroma1997.betterchests;

import aroma1997.core.inventories.Inventories;
import aroma1997.core.network.IBasePacket;

import net.minecraft.entity.player.EntityPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketOpenBag implements IBasePacket {
	
	public PacketOpenBag setSlot(int slot) {
		this.slot = slot;
		return this;
	}
	
	private int slot;
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(slot);
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf slice) {
		slot = slice.readByte();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		Inventories.openContainerAtPlayer(player, slot);
	}
	
}
