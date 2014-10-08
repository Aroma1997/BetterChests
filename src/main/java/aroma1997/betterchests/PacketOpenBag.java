/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import io.netty.buffer.ByteBuf;
import aroma1997.core.inventories.Inventories;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenBag implements IMessage,
		IMessageHandler<PacketOpenBag, IMessage> {

	public PacketOpenBag setSlot(int slot) {
		this.slot = slot;
		return this;
	}

	private int slot;

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(slot);
	}

	@Override
	public IMessage onMessage(PacketOpenBag message, MessageContext ctx) {
		Inventories.openContainerAtPlayer(ctx.getServerHandler().playerEntity,
				message.slot);
		return null;
	}

}
