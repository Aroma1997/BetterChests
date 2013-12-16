/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.HashSet;
import java.util.List;

import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.inventories.ISpecialInventoryProvider;
import aroma1997.core.inventories.Inventories;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBag extends Item implements ISpecialInventoryProvider {
	
	public ItemBag(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
	public boolean
	onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World,
		int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
		EntityPlayer thePlayer)
	{
		if (par2World.isRemote) {
			PacketHandler.sendPacketBag(thePlayer.inventory.currentItem);
		}
		Inventories.openContainerAtPlayer(thePlayer, thePlayer.inventory.currentItem);
		return par1ItemStack;
	}
	
	@Override
	public ISpecialInventory getInventory(EntityPlayer player, int id) {
		return getInventory(player.inventory.getStackInSlot(id));
	}
	
	public static BagInventory getInventory(ItemStack item) {
		return BagInventory.getInvForItem(item);
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4,
		boolean par5) {
		if (par3Entity instanceof EntityPlayer) {
			BagInventory inv = getInventory(par1ItemStack);
			inv.onUpdate((EntityPlayer) par3Entity);
		}
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("item.betterchests:bag.name");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":bag");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {

		par3List.add(StatCollector.translateToLocalFormatted("info.betterchests:tooltip.openwith", Keyboard.getKeyName(BetterChestsKeyHandler.openBag.keyCode)));
		addInfo(par1ItemStack, par3List);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	static void addInfo(ItemStack item, List list) {
		BagInventory inv = getInventory(item);
		HashSet<ItemStack> upgrades = inv.getUpgrades();
		for (ItemStack entry : upgrades) {
			if (!UpgradeHelper.isUpgrade(entry)) continue;
			if (entry.stackSize > 0) {
				IUpgrade upgrade = (IUpgrade) entry.getItem();
				if (upgrade.getMaxUpgrades(entry) == 1) {
					list.add(upgrade.getName(entry));
				}
				else {
					list.add(upgrade.getName(entry) + " (" + entry.stackSize + ")");
				}
			}
		}
	}
	
}
