package aroma1997.betterchests.api;


import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This represents a Upgradable Block
 * @author Aroma1997
 */
public interface IUpgradableBlock extends ICapabilityProvider {

	/**
	 * Returns the UpgradableBlockType of this Upgradable Block.
	 * See {@link UpgradableBlockType} for more info.
	 * @return The UpgradableBlockType of this Block.
	 */
	UpgradableBlockType getUpgradableBlockType();

	/**
	 * Returns the current position of this block as a {@link Vec3d}.
	 * @return the current position of this block.
	 */
	Vec3d getPositionPrecise();
	/**
	 * Returns the current position of this block as a {@link BlockPos}.
	 * @return the current position of this block.
	 */
	BlockPos getPosition();

	/**
	 * Returns the world this Upgradable Block is in.
	 * @return the world of this Upgradable Block.
	 */
	World getWorldObj();

	/**
	 * Returns all Upgrades currently installed in this UpgradableBlock
	 * @return all Upgrades currently installed.
	 */
	Iterable<ItemStack> getUpgrades();

	/**
	 * Returns all Upgrades currently installed and active in this UpgradableBlock
	 * @return all Upgrades currently installed and active.
	 */
	Iterable<ItemStack> getActiveUpgrades();

	/**
	 * Returns the amount of upgrades of the given type currently installed in this UpgradableBlock
	 * @param stack the upgrade to get the amount from.
	 * @return Tha amount of upgrades of that type currently installed.
	 */
	int getAmountUpgrades(ItemStack stack);

	/**
	 * Returns whether the given upgrade is currently installed in this Upgradable Block
	 * @param stack the upgrade to check.
	 * @return Whether this upgrade is installed in this chest
	 */
	default boolean isUpgradeInstalled(ItemStack stack) {
		return getAmountUpgrades(stack) > 0;
	}

	/**
	 * Checks whether the given upgrade is currently enabled or disabled in this Upgradable Block
	 * @param stack The upgrade to check.
	 * @return Whether the given upgrade is currently disabled in this Upgradable Block.
	 * @throws IllegalArgumentException if the upgrade is not installed in this Upgradable Block
	 */
	boolean isUpgradeDisabled(ItemStack stack);
	/**
	 * Enables or disables the given upgrade in this Upgradable Block.
	 * @param stack The upgrade to enable or disable
	 * @param targetVal true, if the upgrafde should be disabled, false if it should be enabled.
	 * @throws IllegalArgumentException if the upgrade is not installed in this Upgradable Block
	 */
	void setUpgradeDisabled(ItemStack stack, boolean targetVal);

	/**
	 * Returns the Energy Storage part of this Upgradable Block.
	 * This is for internal use, so there is no transfer limit and it will fill/drain if there is space/energy
	 * available.
	 * @return The _internal EnergyStorage of this UpgradableBlock
	 */
	IEnergyStorage getEnergyStorage();

	/**
	 * The UpgradableBlock's internal tick count.
	 * This value will increase by one every tick. It might also be negative.
	 * @return The UpgradableBlock's internal tick count.
	 */
	int getTickCount();

	/**
	 * Returns the UpgradableBlock's internal FakePlayer.
	 * Not all UpgradableBlocks have a FakePlayer. If they don't, null is returned here.
	 * @return The UpgradableBlock's FakePlayer or null if it doesn't have one.
	 */
	@Nullable EntityPlayerMP getFakePlayer();
}
