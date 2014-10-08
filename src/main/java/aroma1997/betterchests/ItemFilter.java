/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.api.ItemUpgradeBasic;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ISpecialGUIProvider;

public class ItemFilter extends ItemUpgradeBasic implements ISpecialGUIProvider {

	public ItemFilter() {
		super();
		setCreativeTab(BetterChests.creativeTabBC);
		setNameAndTexture("betterchests:filter");
		setHasSubtypes(true);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
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

	@Override
	public boolean canChestTakeUpgrade(ItemStack item) {
		return true;
	}

	@Override
	public boolean canBagTakeUpgrade(ItemStack item) {
		return true;
	}

	@Override
	public int getMaxUpgrades(ItemStack item) {
		return 10;
	}

	@Override
	public boolean canBeDisabled(ItemStack stack) {
		return false;
	}

	@Override
	public AromaContainer getContainer(EntityPlayer player, int i) {
		return null;
	}

}
