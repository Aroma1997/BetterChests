package aroma1997.betterchests.bag;

import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.core.util.registry.TickRegistry;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.client.ClientProxy;
import aroma1997.betterchests.network.PacketBagInfo;

public class ItemBBag extends ItemBBagBase<InventoryBBag> {

	private static final Set<Integer> entitiesWithBags = new HashSet<>();
	private static final Set<Integer> prevEntitiesWithBags = new HashSet<>();

	public ItemBBag() {
		setUnlocalizedName("betterchests:betterbag");
		TickRegistry.SERVER.addContinuousCallback(() -> {
			if (!entitiesWithBags.equals(prevEntitiesWithBags)) {
				BetterChests.instance.ph.sendPacketToPlayers(new PacketBagInfo(entitiesWithBags));
				prevEntitiesWithBags.clear();
				prevEntitiesWithBags.addAll(entitiesWithBags);
			}
			entitiesWithBags.clear();
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);

		int keycode = ClientProxy.keyBind.getKeyCode();
		if (keycode >= 0 && keycode < Keyboard.KEYBOARD_SIZE) {
			tooltip.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.betterbag.keybind", Keyboard.getKeyName(keycode)));
		}
	}

	@Override
	protected InventoryBBag getNewInstance(Entity entity, ItemStack stack) {
		return new InventoryBBag(entity, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (!isSelected && !world.isRemote) {
			entitiesWithBags.add(entity.getEntityId());
		}
	}
}
