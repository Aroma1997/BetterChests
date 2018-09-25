package aroma1997.betterchests.network;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import aroma1997.core.network.AutoEncode;
import aroma1997.core.network.packets.PacketAutoEncode;
import aroma1997.betterchests.BetterChests;

public class PacketBagInfo extends PacketAutoEncode implements IMessageHandler<PacketBagInfo, IMessage> {

	@AutoEncode
	private final Set<Integer> entitiesWithBag;

	public PacketBagInfo(){
		entitiesWithBag = new HashSet<>();
	}

	public PacketBagInfo(Set<Integer> set) {
		this.entitiesWithBag = set;
	}

	@Override
	public IMessage onMessage(PacketBagInfo message, MessageContext ctx) {
		BetterChests.proxy.assignEntitiesWithBag(message.entitiesWithBag);
		return null;
	}
}
