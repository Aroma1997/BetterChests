/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBChest extends ItemBlock {
	
	public ItemBlockBChest(int id) {
		super(id);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "Adjustable Chest";
	}
	
}
