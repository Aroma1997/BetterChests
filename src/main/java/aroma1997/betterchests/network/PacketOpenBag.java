package aroma1997.betterchests.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import aroma1997.core.container.ContainerHelper;
import aroma1997.core.network.AutoEncode;
import aroma1997.core.network.packets.PacketAutoEncode;

public class PacketOpenBag extends PacketAutoEncode implements IMessageHandler<PacketOpenBag, IMessage> {

	@AutoEncode
	public int slot = -1;

	public PacketOpenBag() {}
	public PacketOpenBag(int slot) {
		this.slot = slot;
	}

	@Override
	public IMessage onMessage(PacketOpenBag message, MessageContext ctx) {
		ContainerHelper.openGui(ctx.getServerHandler().player, message.slot, (short) 0);
		return null;
	}
}
