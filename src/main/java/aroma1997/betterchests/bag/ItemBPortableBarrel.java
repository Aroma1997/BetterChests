package aroma1997.betterchests.bag;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;

public class ItemBPortableBarrel extends ItemBBagBase<InventoryBPortableBarrel> {

	public ItemBPortableBarrel() {
		setUnlocalizedName("betterchests:betterportablebarrel");
	}

	@Override
	protected InventoryBPortableBarrel getNewInstance(Entity entity, ItemStack stack) {
		return new InventoryBPortableBarrel(entity, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		InventoryBPortableBarrel inv = getInventoryFor(stack, null);
		if (inv != null && inv.getChestPart().isItemSet()) {
			tooltip.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.portablebarrel.content", inv.getChestPart().getDummy().getDisplayName()));
			tooltip.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.portablebarrel.amount", inv.getChestPart().getAmountDescr()));
		}
	}
}
