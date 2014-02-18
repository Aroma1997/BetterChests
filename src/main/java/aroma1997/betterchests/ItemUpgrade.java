/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.ArrayList;
import java.util.List;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.client.util.Colors;
import aroma1997.core.util.InvUtil;
import aroma1997.core.util.ItemUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item implements IUpgrade {
	
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
		if (upgrade.getRequirement() != null) {
			par3List.add(StatCollector.translateToLocalFormatted(
				"info.betterchests:tooltip.requires", Colors.YELLOW
				+ upgrade.getRequirement().getName()));
		}
		if (upgrade.getMaxAmount() != 0) {
			par3List.add(StatCollector.translateToLocalFormatted(
				"info.betterchests:tooltip.maxamount", upgrade.getMaxAmount()));
		}
		if (! upgrade.canBagTakeUpgrade() && ! upgrade.canChestTakeUpgrade()) {}
		else if (! upgrade.canChestTakeUpgrade()) {
			par3List.add(StatCollector.translateToLocal("info.betterchests:tooltip.nochest"));
		}
		else if (! upgrade.canBagTakeUpgrade()) {
			par3List.add(StatCollector.translateToLocal("info.betterchests:tooltip.nobag"));
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
	
	@Override
	public boolean canChestTakeUpgrade(ItemStack item) {
		if (item == null) {
			return false;
		}
		return Upgrade.values()[item.getItemDamage()].canChestTakeUpgrade();
	}
	
	@Override
	public boolean canBagTakeUpgrade(ItemStack item) {
		if (item == null) {
			return false;
		}
		Upgrade upgrade = Upgrade.values()[item.getItemDamage()];
		return upgrade.canBagTakeUpgrade();
	}
	
	@Override
	public List<ItemStack> getRequiredUpgrade(ItemStack item) {
		if (item == null) {
			return null;
		}
		Upgrade upgrade = Upgrade.values()[item.getItemDamage()];
		if (upgrade.getRequirement() == null) {
			return null;
		}
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(upgrade.getRequirement().getItem());
		return list;
	}
	
	@Override
	public void update(IBetterChest chest, int tick, World world, ItemStack item) {
		if (! UpgradeHelper.isUpgrade(item)) {
			return;
		}
		Upgrade.values()[item.getItemDamage()].getUpgrade().updateChest(chest, tick, world, item);
	}
	
	@Override
	public int getMaxUpgrades(ItemStack item) {
		if (item == null) {
			return 0;
		}
		return Upgrade.values()[item.getItemDamage()].getMaxAmount();
	}
	
	@Override
	public String getName(ItemStack item) {
		if (item == null || ! UpgradeHelper.isUpgrade(item)) {
			return "UNKNOWN NAME";
		}
		return Upgrade.values()[item.getItemDamage()].getName();
	}
	
	@Override
	public void onUpgradeInstalled(ItemStack item, IBetterChest chest) {
		if (ItemUtil.areItemsSame(item, Upgrade.VOID.getItem())) {
			InvUtil.clearInventory(chest);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawGuiContainerForegroundLayer(GuiContainer gui, Container container, int par1,
		int par2, ItemStack item) {
		
	}
	
}
