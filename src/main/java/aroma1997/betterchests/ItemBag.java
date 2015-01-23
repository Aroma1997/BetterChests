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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.client.ClientProxy;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ISpecialGUIProvider;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.items.inventory.ItemInventory;
import aroma1997.core.items.wrench.ItemWrench;
import aroma1997.core.log.LogHelper;

public class ItemBag extends ItemInventory<BagInventory> implements
		ISpecialGUIProvider {

	public ItemBag() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName("betterchests:bag");
		setCreativeTab(BetterChests.creativeTabBC);
		registerModels();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean onItemUseFirst(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, BlockPos pos,
			EnumFacing side, float par8, float par9, float par10) {

		if (par3World.isRemote) {
			Inventories
					.sendItemInventoryOpen(par2EntityPlayer.inventory.currentItem);
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer thePlayer) {
		// This is disabled, because it causes the server to change the
		// ItemStack after the Inventory is opened, which basically messes up
		// the Inventory.
		return par1ItemStack;
	}

	public static BagInventory getBagInventory(ItemStack stack) {
		return BetterChestsItems.bag.getInventory(stack);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		if (par3Entity instanceof EntityPlayer) {
			BagInventory inv = getInventory(par1ItemStack);
			inv.onUpdate((EntityPlayer) par3Entity);
		}
		if (par2World.isRemote)
			return;
		LogHelper.log(Level.INFO, System.identityHashCode(par1ItemStack) + " "
				+ par1ItemStack.stackSize);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

		par3List.add(StatCollector.translateToLocalFormatted(
				"info.betterchests:tooltip.openwith",
				Keyboard.getKeyName(ClientProxy.openBag.getKeyCode())));
		addInfo(par1ItemStack, par3List);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void addInfo(ItemStack item, List list) {
		BagInventory inv = getBagInventory(item);
		ArrayList<ItemStack> upgrades = inv.getUpgrades();
		if (upgrades.size() > 0) {
			list.add(StatCollector
					.translateToLocal("info.betterchests:tooltip.upgradesinstalled"));
		}
		for (ItemStack entry : upgrades) {
			if (!UpgradeHelper.isUpgrade(entry)) {
				continue;
			}
			if (entry.stackSize > 0) {
				IUpgrade upgrade = (IUpgrade) entry.getItem();
				if (upgrade.getMaxUpgrades(entry) == 1) {
					list.add(entry.getItem().getItemStackDisplayName(entry));
				} else {
					list.add(entry.getItem().getItemStackDisplayName(entry)
							+ " (" + entry.stackSize + ")");
				}
			}
		}
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		if (world.isRemote)
			return null;
		EntityBag e = new EntityBag(world, location.posX, location.posY,
				location.posZ, itemstack);
		e.setDefaultPickupDelay();
		e.motionX = location.motionX;
		e.motionY = location.motionY;
		e.motionZ = location.motionZ;
		return e;
	}

	@Override
	public AromaContainer getContainer(EntityPlayer player, int i) {
		BagInventory inv = getInventory(player.inventory.getStackInSlot(i));
		if (player.isSneaking() && ItemWrench.hasPlayerWrench(player)) {
			return new ContainerUpgrades(inv, player);
		}
		return inv.getContainer(player, i);
	}

	@Override
	protected BagInventory generateInventory(ItemStack item) {
		return new BagInventory(item);
	}

}
