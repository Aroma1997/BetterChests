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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockBChest extends ItemBlock {
	
	public ItemBlockBChest(Block block) {
		super(block);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("tile.betterchests:chest.name");
	}
	
	@SuppressWarnings({"rawtypes"})
	@Override
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean par4) {
		if (isTagChest(item)) {
			ItemBag.addInfo(item, list);
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(BetterChests.getHelpBook());
		super.getSubItems(par1, par2CreativeTabs, par3List);
	}
	
	static boolean isTagChest(ItemStack item) {
		return item != null && item.hasTagCompound() && item.getTagCompound().hasKey("player");
	}
	
}
