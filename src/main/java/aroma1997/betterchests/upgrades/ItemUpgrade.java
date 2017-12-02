package aroma1997.betterchests.upgrades;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.item.AromicItemMulti;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.api.UpgradableBlockType;

public class ItemUpgrade<T extends Enum<?> & Supplier<BasicUpgrade>> extends AromicItemMulti<T> implements IUpgrade {
	public ItemUpgrade(Class<T> clazz) {
		super(clazz);
		setCreativeTab(BetterChests.creativeTab);
	}

	@Override
	public Collection<ItemStack> getRequiredUpgrades(ItemStack stack) {
		return getType(stack).get().getRequiredUpgrades(stack);
	}

	@Override
	public boolean areRequirementsMet(IUpgradableBlock chest, ItemStack stack) {
		return getType(stack).get().areRequirementsMet(chest, stack);
	}

	@Override
	public boolean canBePutInChest(IUpgradableBlock chest, ItemStack stack) {
		return getType(stack).get().canBePutInChest(chest, stack);
	}

	@Override
	public Collection<UpgradableBlockType> getCompatibleTypes(ItemStack stack) {
		return getType(stack).get().getCompatibleTypes(stack);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		getType(stack).get().update(chest, stack);
	}

	@Override
	public boolean canBeDisabled(ItemStack stack) {
		return getType(stack).get().canBeDisabled(stack);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		return getType(stack).get().getChestModifier(chest, modifier, stack);
	}

	@Override
	public int getMaxAmountUpgrades(ItemStack stack) {
		return getType(stack).get().getMaxAmountUpgrades(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		getType(stack).get().addTooltips(stack, tooltip);
	}

	public boolean isUpgradeInstalled(IUpgradableBlock block, T upgrade) {
		return block.isUpgradeInstalled(getStack(upgrade));
	}

}
