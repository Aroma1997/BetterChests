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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.client.BetterChestsKeyBinding;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ISpecialGUIProvider;
import aroma1997.core.items.wrench.ItemWrench;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBag extends Item implements ISpecialGUIProvider {
	
	public ItemBag() {
		super();
		setUnlocalizedName("betterchests:betterBag");
		setMaxStackSize(1);
		setTextureName(Reference.MOD_ID + ":bag");
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
	        World par3World, int par4, int par5, int par6, int par7, float par8, float par9,
	        float par10) {
		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
	        EntityPlayer thePlayer) {
		if (par2World.isRemote) {
			BetterChests.ph.sendPacketToPlayers(new PacketOpenBag()
			        .setSlot(thePlayer.inventory.currentItem));
		}
		return par1ItemStack;
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
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("item.betterchests:bag.name");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
	        List par3List, boolean par4) {
		
		par3List.add(StatCollector.translateToLocalFormatted("info.betterchests:tooltip.openwith",
		        Keyboard.getKeyName(BetterChestsKeyBinding.getInstance().getKeyCode())));
		addInfo(par1ItemStack, par3List);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	static void addInfo(ItemStack item, List list) {
		BagInventory inv = getInventory(item);
		ArrayList<ItemStack> upgrades = inv.getUpgrades();
		for (ItemStack entry : upgrades) {
			if (! UpgradeHelper.isUpgrade(entry)) {
				continue;
			}
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
	
	@Override
	public boolean hasCustomEntity(ItemStack stack)
    {
		return true;
    }
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
		if (world.isRemote) return null;
		EntityBag e = new EntityBag(world, location.posX, location.posY, location.posZ, itemstack);
		e.delayBeforeCanPickup = ((EntityItem) location).delayBeforeCanPickup;
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
	
}
