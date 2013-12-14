/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.List;
import java.util.Set;

import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class UpgradeHelper {
	
	@SuppressWarnings("unchecked")
	public static void updateChest(IBetterChest inv, int tick, World world) {
		if (world.isRemote) return;
		if (inv.isUpgradeInstalled(Upgrade.VOID.getItem())) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					continue;
				}
				inv.decrStackSize(i, inv.getStackInSlot(i).stackSize);
			}
		}
		
		if (inv.isUpgradeInstalled(Upgrade.COBBLEGEN.getItem()) && tick % 8 == 5) {
			int bucketLava = - 1;
			int bucketWater = - 1;
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
			}
			if (bucketLava == - 1 || bucketWater == - 1) {
				return;
			}
			InvUtil.putIntoFirstSlot(inv, new ItemStack(Block.cobblestone));
		}
		
		if (inv.isUpgradeInstalled(Upgrade.FURNACE.getItem()) && tick %32 == 5) {
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
		if (inv.isUpgradeInstalled(Upgrade.COLLECTOR.getItem())) {
		      float radius = inv.getAmountUpgrade(Upgrade.COLLECTOR.getItem());
		      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(inv.getXPos() + -0.5D, inv.getYPos() + 0.0D, inv.getZPos() + -0.5D, inv.getXPos() + 0.5D, inv.getYPos() + 0.0D, inv.getZPos() + 0.5D);
		      bounds = bounds.expand(radius, radius, radius);
			List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
		      for (EntityItem e : list) {
		    	  if (e.getEntityItem() == null) continue;
		        if (e.age >= 10 && e.isEntityAlive())
		        {
		          ItemStack itemBack = InvUtil.putIntoFirstSlot(inv, e.getEntityItem());
		          e.motionX = inv.getXPos() - e.posX;
		          e.motionY = inv.getYPos() - e.posY;
		          e.motionZ = inv.getZPos() - e.posZ;
		        	if (itemBack == null) {
		        		world.removeEntity(e);
//		        		e.setDead();
		        	}
		        	else {
		        		e.setEntityItemStack(itemBack);
		        	}
		          if (world.rand.nextInt(10) == 0)
		          {
		            float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
		            world.playSoundEffect(e.posX, e.posY, e.posZ, "mob.endermen.portal", 0.6F, pitch);
		          }
		        }
		      }
			}
		Set<ItemStack> upgrades= null;
		if (inv instanceof TileEntityBChest) {
			upgrades = ((TileEntityBChest)inv).getUpgrades();
		}
		else if (inv instanceof BagInventory) {
			upgrades = ((BagInventory)inv).getUpgradeList();
		}
		if (upgrades == null) return;
		
		for (ItemStack item : upgrades) {
			if (isUpgrade(item)) {
				((IUpgrade)item.getItem()).update(inv, tick, world);
			}
		}
	}
	
	public static boolean isUpgrade(ItemStack item) {
		return item != null && item.getItem() instanceof IUpgrade;
	}
	
	public static ItemStack getDefaultItem(ItemStack item) {
		if (!isUpgrade(item)) return null;
		item = item.copy();
		item.stackSize = 1;
		return item;
	}
	
	public static boolean areRequirementsInstalled(IBetterChest chest, ItemStack item) {
		if (chest == null || item == null || !isUpgrade(item)) return false;
		
		IUpgrade upgrade = (IUpgrade) item.getItem();
		if (upgrade == null) return false;
		if (upgrade.getRequiredUpgrade(item) == null) return true;
		for (ItemStack req : upgrade.getRequiredUpgrade(item)) {
			if (!chest.isUpgradeInstalled(req)) return false;
		}
		return true;
		
	}
	
}
