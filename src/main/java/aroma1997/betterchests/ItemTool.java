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

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTool extends Item {
	
	private IIcon[] itemIcons = new IIcon[Tool.values().length];
	
	public ItemTool() {
		super();
		setUnlocalizedName("betterchests:tool");
		setHasSubtypes(true);
		setMaxStackSize(1);
		setContainerItem(this);
		setFull3D();
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		return Tool.values()[par1ItemStack.getItemDamage()].getName();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int par1)
	{
		return itemIcons[par1];
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < Tool.values().length; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcons = new IIcon[Tool.values().length];
		for (int i = 0; i < itemIcons.length; i++) {
			itemIcons[i] = iconRegister.registerIcon(Tool.values()[i].getTexture());
		}
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y,
		int z, int side, float hitX, float hitY, float hitZ) {
		return Tool.values()[stack.getItemDamage()].getTool().onItemUseFirst(stack, player, world,
			x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return itemStack;
	}
	
}
