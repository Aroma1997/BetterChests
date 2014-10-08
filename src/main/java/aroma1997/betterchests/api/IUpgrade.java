/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Implement this Interface in your Item and it'll be accepted by BetterChest.
 * You can also have a Multi-Item, where the Upgrades have a different metadata.
 * 
 * @author Aroma1997
 * 
 */
public interface IUpgrade {

	/**
	 * If the Upgrade can be put on a Chest.
	 * 
	 * @param item
	 *            The Upgrade to Check.
	 * @return
	 */
	public boolean canChestTakeUpgrade(ItemStack item);

	/**
	 * If the Upgrade can be Put on a Bag.
	 * 
	 * @param item
	 *            The item to check
	 * @return
	 */
	public boolean canBagTakeUpgrade(ItemStack item);

	/**
	 * Get the list of Upgrades Required for this Upgrade.
	 * 
	 * @param item
	 *            The Upgrade to check.
	 * @return
	 */
	public List<ItemStack> getRequiredUpgrade(ItemStack item);

	/**
	 * Called, whenerever the Chest gets ticked
	 * 
	 * @param chest
	 *            The Adjustable Chest or the Bag
	 * @param tick
	 *            The tick (counts from 0 to 64)
	 * @param world
	 *            The world
	 * @param item
	 *            The Upgrade
	 */
	public void update(IBetterChest chest, int tick, World world, ItemStack item);

	/**
	 * The Max amount of Upgrades per Chest/Bag.
	 * 
	 * @param item
	 *            The Upgrade to Check.
	 * @return
	 */
	public int getMaxUpgrades(ItemStack item);

	// /**
	// * The name of The Upgrade.
	// *
	// * @param item
	// * The Upgrade to check
	// * @return The localized(!!) name of the Upgrade
	// */
	// public String getName(ItemStack item);

	/**
	 * Called when the Upgrade is installed.
	 * 
	 * @param item
	 *            The Upgrade
	 * @param chest
	 *            The Chest/Bag
	 */
	public void onUpgradeInstalled(ItemStack item, IBetterChest chest);

	/**
	 * The GUI's rendering
	 * 
	 * @param gui
	 *            The gui
	 * @param container
	 *            The Container
	 * @param item
	 *            The upgrade
	 */
	@SideOnly(Side.CLIENT)
	public void drawGuiContainerForegroundLayer(GuiContainer gui,
			Container container, int par1, int par2, ItemStack item);

	/**
	 * USed to check if the upgrade can be disabled in the {@link}
	 * ContainerUpgrades
	 * 
	 * @param stack
	 *            The upgrade
	 * @return if it can be disabled.
	 */
	public boolean canBeDisabled(ItemStack stack);

}
