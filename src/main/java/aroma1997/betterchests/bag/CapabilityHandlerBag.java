package aroma1997.betterchests.bag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityHandlerBag implements ICapabilityProvider {

	private final ItemStack stack;

	public CapabilityHandlerBag(ItemStack stack) {
		this.stack = stack;
	}

	protected ICapabilityProvider getActualProvider() {
		return ItemBBag.getInventoryFor(stack, null);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return getActualProvider().hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return getActualProvider().getCapability(capability, facing);
	}
}
