/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import aroma1997.betterchests.InventoryFilter.BCFilterFilter;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Mining extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick != 38) {
			return;
		}
		int block = InvUtil.getFirstItem(chest, ItemBlock.class, null,
				new BCFilterFilter(chest.getFiltersForUpgrade(item)));
		if (block != -1) {
			int tool = InvUtil.getFirstItem(chest, ItemTool.class);
			if (tool != -1) {
				ItemStack blS = chest.getStackInSlot(block);
				ItemStack tS = chest.getStackInSlot(tool);
				Block b = Block.getBlockFromItem(blS.getItem());
				ItemTool t = (ItemTool) tS.getItem();
				if (t.canHarvestBlock(b, tS)) {
					int fortune = EnchantmentHelper.getEnchantmentLevel(
							Enchantment.fortune.effectId, tS);
					ItemStack drop = new ItemStack(b.getItemDropped(
							b.getDefaultState(), new Random(), fortune),
							b.quantityDroppedWithBonus(fortune, new Random()),
							b.damageDropped(b.getDefaultState()));
					if (InvUtil.putIntoFirstSlot(chest, drop, true) == null) {
						InvUtil.putIntoFirstSlot(chest, drop, false);

						if (tS.attemptDamageItem(1, new Random())) {
							chest.setInventorySlotContents(tool, null);
						}
						chest.decrStackSize(block, 1);
					}
				}
			}
		}
	}

}
