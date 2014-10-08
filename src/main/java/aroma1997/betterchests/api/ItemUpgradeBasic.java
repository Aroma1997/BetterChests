/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.core.client.util.Colors;
import aroma1997.core.items.AromicItem;

public abstract class ItemUpgradeBasic extends AromicItem implements IUpgrade {

	@Override
	public List<ItemStack> getRequiredUpgrade(ItemStack item) {
		return null;
	}

	@Override
	public void update(IBetterChest chest, int tick, World world, ItemStack item) {

	}

	@Override
	public void onUpgradeInstalled(ItemStack item, IBetterChest chest) {

	}

	@Override
	public void drawGuiContainerForegroundLayer(GuiContainer gui,
			Container container, int par1, int par2, ItemStack item) {

	}

	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (par1ItemStack == null || !UpgradeHelper.isUpgrade(par1ItemStack))
			return;
		List<ItemStack> requirements = getRequiredUpgrade(par1ItemStack);
		if (requirements != null && !requirements.isEmpty()) {
			for (ItemStack req : requirements) {
				par3List.add(StatCollector.translateToLocalFormatted(
						"info.betterchests:tooltip.requires", Colors.YELLOW
								+ req.getItem().getItemStackDisplayName(req)));
			}
		}
		IUpgrade upgrade = (IUpgrade) par1ItemStack.getItem();
		if (upgrade.getMaxUpgrades(par1ItemStack) != 0) {
			par3List.add(StatCollector.translateToLocalFormatted(
					"info.betterchests:tooltip.maxamount",
					upgrade.getMaxUpgrades(par1ItemStack)));
		}
		if (!upgrade.canBagTakeUpgrade(par1ItemStack)
				&& !upgrade.canChestTakeUpgrade(par1ItemStack)) {
		} else if (!upgrade.canChestTakeUpgrade(par1ItemStack)) {
			par3List.add(StatCollector
					.translateToLocal("info.betterchests:tooltip.nochest"));
		} else if (!upgrade.canBagTakeUpgrade(par1ItemStack)) {
			par3List.add(StatCollector
					.translateToLocal("info.betterchests:tooltip.nobag"));
		}
	}

}
