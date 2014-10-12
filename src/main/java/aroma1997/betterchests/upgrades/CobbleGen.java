package aroma1997.betterchests.upgrades;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.InventoryFilter;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;

public class CobbleGen extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest inv, int tick, World world,
			ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.COBBLEGEN.getItem())
				&& tick % 8 == 5) {
			int bucketLava = -1;
			int bucketWater = -1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) != null
						&& ItemUtil.areItemsSameMatching(inv.getStackInSlot(i),
								new ItemStack(Items.water_bucket),
								ItemMatchCriteria.ID) && bucketWater == -1) {
					bucketWater = i;
					continue;
				}
				if (inv.getStackInSlot(i) != null
						&& bucketLava == -1
						&& ItemUtil.areItemsSameMatching(inv.getStackInSlot(i),
								new ItemStack(Items.lava_bucket),
								ItemMatchCriteria.ID)) {
					bucketLava = i;
					continue;
				}
			}
			if (bucketLava == -1 || bucketWater == -1) {
				return;
			}
			List<InventoryFilter> list = inv.getFiltersForUpgrade(item);
			if (InventoryFilter.isItemAllowed(new ItemStack(Blocks.cobblestone), list)) {
				InvUtil.putIntoFirstSlot(inv, new ItemStack(Blocks.cobblestone),
					false);
			}
			else if (InventoryFilter.isItemAllowed(new ItemStack(Blocks.stone), list)) {
				InvUtil.putIntoFirstSlot(inv, new ItemStack(Blocks.stone),
						false);
			}
		}
	}

}
