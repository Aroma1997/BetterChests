/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import aroma1997.core.inventories.Inventories;
import aroma1997.core.util.WorldUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class ItemHammer extends ToolItem {
	
	public ItemHammer() {
		
	}

	@Override
	boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
		int side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (world.isRemote) return false;
		if (te != null && te instanceof TileEntityBChest) {
			TileEntityBChest chest = (TileEntityBChest) te;
			if (player.isSneaking()) {
				WorldUtil.dropItemsRandom(world, chest.getDroppedFullItem(), x, y, z);
				chest.pickedUp = true;
				world.setBlockToAir(x, y, z);
				return true;
			}
			else {
				Inventories.openContainerTileEntity(player, world.getBlockTileEntity(x, y, z), false);
				return true;
			}
		}
		return false;
	}
	
}
