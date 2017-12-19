package aroma1997.betterchests.integration.storagedrawers;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.chest.TileEntityBBarrel;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class BlockHandler {

	private static final ResourceLocation rl = new ResourceLocation("betterchests:storagedrawerscaps");

	@CapabilityInject(IDrawerGroup.class)
	static Capability<IDrawerGroup> DRAWER_GROUP;

	BlockHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void initTe(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof TileEntityBBarrel) {
			TileEntityBBarrel chest = (TileEntityBBarrel) event.getObject();
			event.addCapability(rl, new CapabilityHandler(chest));
		}
	}

	private static class CapabilityHandler implements ICapabilityProvider, IDrawerGroup, IDrawer {

		private final TileEntityBBarrel barrel;
		private ItemStack buffer;

		public CapabilityHandler(TileEntityBBarrel barrel) {
			this.barrel = barrel;
		}

		@Override
		public int getDrawerCount() {
			return 1;
		}

		@Nonnull
		@Override
		public IDrawer getDrawer(int slot) {
			return this;
		}

		@Nonnull
		@Override
		public int[] getAccessibleDrawerSlots() {
			return new int[] {0};
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == DRAWER_GROUP;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return (capability == DRAWER_GROUP) ? (T)this : null;
		}

		@Nonnull
		@Override
		public ItemStack getStoredItemPrototype() {
			return barrel.getChestPart().isItemSet() ? barrel.getChestPart().getDummy() : ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public IDrawer setStoredItem(@Nonnull ItemStack itemPrototype) {
			buffer = itemPrototype.copy();
			return this;
		}

		@Override
		public int getStoredItemCount() {
			return barrel.getChestPart().getTotalAmountOfItems();
		}

		@Override
		public void setStoredItemCount(int amount) {
			int totalAmount = getStoredItemCount();
			InventoryPartBarrel part = barrel.getChestPart();
			int size = part.getActualSize();
			if (totalAmount >= amount) {
				int diff = totalAmount - amount;
				for (int i = 0; i < size && diff > 0; i++) {
					diff -= part.decrStackSize(i, diff).getCount();
				}
			} else {
				int diff = amount - totalAmount;
				ItemStack stack = buffer.copy();
				stack.setCount(diff);
				InvUtil.putStackInInventoryFirst(stack, part, true, false, false, null);
			}
			barrel.getChestPart().markDirty();
		}

		@Override
		public int getMaxCapacity(@Nonnull ItemStack itemPrototype) {
			return barrel.getChestPart().getActualSize() * itemPrototype.getMaxStackSize();
		}

		@Override
		public int getRemainingCapacity() {
			return barrel.getChestPart().isEmpty() ? barrel.getChestPart().getActualSize() * 64 : getMaxCapacity(barrel.getChestPart().getDummy()) - getStoredItemCount();
		}

		@Override
		public boolean canItemBeStored(@Nonnull ItemStack itemPrototype, Predicate<ItemStack> matchPredicate) {
			return barrel.getChestPart().isItemValidForSlot(0, itemPrototype);
		}

		@Override
		public boolean canItemBeExtracted(@Nonnull ItemStack itemPrototype, Predicate<ItemStack> matchPredicate) {
			return true;
		}

		@Override
		public boolean isEmpty() {
			return barrel.getChestPart().isEmpty();
		}
	}
}
