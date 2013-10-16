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

import aroma1997.core.client.util.Colors;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {
	
	public ItemUpgrade(int id) {
		super(id);
		setCreativeTab(BetterChests.creativeTabBC);
		setHasSubtypes(true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		Upgrade upgrade = Upgrade.values()[par1ItemStack.getItemDamage()];
		if (Upgrade.values()[par1ItemStack.getItemDamage()].getRequirement() != null) {
			par3List.add("Requires " + Colors.YELLOW
				+ Upgrade.values()[par1ItemStack.getItemDamage()].getRequirement().getName());
		}
		if (upgrade.getMaxAmount() != 0) {
			par3List.add("Max Upgrades per Chest: " + upgrade.getMaxAmount() + ".");
		}
		if (!upgrade.canBag) {
			par3List.add("Can not be put on a Bag.");
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < Upgrade.values().length; i++) {
			par3List.add(new ItemStack(itemID, 1, i));
		}
	}
	
	private Icon[] itemIcons;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		itemIcons = new Icon[Upgrade.values().length];
		for (int i = 0; i < itemIcons.length; i++) {
			itemIcons[i] = iconRegister.registerIcon(Upgrade.values()[i].getTexture());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int par1)
	{
		return itemIcons[par1];
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return Upgrade.values()[par1ItemStack.getItemDamage()].getName();
	}
	
}
