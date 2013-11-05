/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class UpgradeHelper {
	
	public static void updateChest(IBetterChest inv, int tick) {
		
		if (inv.isUpgradeInstalled(Upgrade.VOID)) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					continue;
				}
				inv.decrStackSize(i, inv.getStackInSlot(i).stackSize);
			}
		}
		
		if (inv.isUpgradeInstalled(Upgrade.COBBLEGEN) && tick == 60) {
			int bucketLava = - 1;
			int bucketWater = - 1;
			int empty = - 1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) != null
					&& inv.getStackInSlot(i).itemID == Item.bucketWater.itemID
					&& bucketWater == - 1) {
					bucketWater = i;
					continue;
				}
				if (inv.getStackInSlot(i) != null && bucketLava == - 1
					&& inv.getStackInSlot(i).itemID == Item.bucketLava.itemID) {
					bucketLava = i;
					continue;
				}
				if (empty == - 1
					&& (inv.getStackInSlot(i) == null || inv.getStackInSlot(i) != null
					&& inv.getStackInSlot(i).itemID == Block.cobblestone.blockID
					&& inv.getStackInSlot(i).stackSize < inv.getStackInSlot(
						i).getMaxStackSize())) {
					empty = i;
					continue;
				}
			}
			if (bucketLava == - 1 || bucketWater == - 1 || empty == - 1) {
				return;
			}
			int amount;
			
			if (inv.getStackInSlot(empty) == null) {
				amount = 1;
			}
			else {
				amount = 1 + inv.getStackInSlot(empty).stackSize;
			}
			
			inv.setInventorySlotContents(empty, new ItemStack(Block.cobblestone, amount));
		}
		
		if (inv.isUpgradeInstalled(Upgrade.FURNACE) && tick == 59) {
			int cooking = - 1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				if (FurnaceRecipes.smelting().getSmeltingResult(stack) == null) {
					continue;
				}
				cooking = i;
				break;
			}
			if (cooking != - 1) {
				ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(
					inv.getStackInSlot(cooking)).copy();
				if (smelted.stackSize <= 0) {
					smelted.stackSize = 1;
				}
				int result = - 1;
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					if (inv.getStackInSlot(i) == null
						|| smelted.isItemEqual(inv.getStackInSlot(i)) && smelted.stackSize
						+ inv.getStackInSlot(i).stackSize <= 64) {
						result = i;
						break;
					}
				}
				if (result != - 1) {
					inv.decrStackSize(cooking, 1);
					ItemStack put = inv.getStackInSlot(result);
					if (put != null) {
						put.stackSize += smelted.stackSize;
					}
					else {
						put = smelted;
					}
					inv.setInventorySlotContents(result, put);
				}
			}
		}
	}
	
}
