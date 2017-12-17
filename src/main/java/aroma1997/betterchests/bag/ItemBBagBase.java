package aroma1997.betterchests.bag;

import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.ContainerHelper;
import aroma1997.core.inventory.ItemInventory;
import aroma1997.core.item.AromicItemInventory;
import aroma1997.betterchests.BetterChests;

public abstract class ItemBBagBase<T extends BasicBagInventory> extends AromicItemInventory {

	private final Map<ItemStack, T> lookup = new WeakHashMap<>();

	public ItemBBagBase() {
		setCreativeTab(BetterChests.creativeTab);
	}

	public synchronized T getInventoryFor(ItemStack stack, Entity entity) {

		if (entity instanceof EntityPlayer && ((EntityPlayer)entity).openContainer instanceof ContainerBase &&
				((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory instanceof BasicBagInventory &&
				((T)((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory).item.get() == stack) {
			lookup.remove(stack);
			return (T)((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory;
		}

		T bag = lookup.get(stack);
		if (bag != null && (bag.getEntity() != entity && entity != null || bag.item.get() != stack)) {
			bag = null;
		}

		if (bag == null) {
			bag = getNewInstance(entity, stack);
			lookup.put(stack, bag);
		}
		return bag;
	}

	protected abstract T getNewInstance(Entity entity, ItemStack stack);

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.isRemote) {
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) == stack) {
				ContainerHelper.openGui(player, i, (short) (player.isSneaking() ? 1 : 0));
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public ItemInventory getGuiProvider(EntityPlayer player, ItemStack stack) {
		//We cannot use the cached object here.
		return getNewInstance(player, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		T inv = getInventoryFor(stack, entity);
		inv.tick();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return oldStack.getItem() != newStack.getItem() || oldStack.getMetadata() != newStack.getMetadata();
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		EntityBag entity = new EntityBag(world);
		entity.readFromNBT(location.writeToNBT(new NBTTagCompound()));
		return entity;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		//Added indirection layer, so we can use a BagInventory where the entity is set.
		return new CapabilityHandlerBag(stack);
	}
}
