package aroma1997.betterchests.bag;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.ContainerHelper;
import aroma1997.core.inventory.ItemInventory;
import aroma1997.core.item.AromicItemInventory;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.client.ClientProxy;

public class ItemBBag extends AromicItemInventory {

	private static final Map<ItemStack, InventoryBBag> lookup = new WeakHashMap<>();

	public ItemBBag() {
		setCreativeTab(BetterChests.creativeTab);
		setUnlocalizedName("betterchests:betterbag");
	}

	public static synchronized InventoryBBag getInventoryFor(ItemStack stack, Entity entity) {

		if (entity instanceof EntityPlayer && ((EntityPlayer)entity).openContainer instanceof ContainerBase &&
				((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory instanceof InventoryBBag &&
				((InventoryBBag)((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory).item.get() == stack) {
			lookup.remove(stack);
			return (InventoryBBag)((ContainerBase<?>)((EntityPlayer)entity).openContainer).inventory;
		}

		InventoryBBag bag = lookup.get(stack);
		if (bag != null && (bag.entity != entity && entity != null || bag.item.get() != stack)) {
			bag = null;
		}

		if (bag == null) {
			bag = new InventoryBBag(entity, stack);
			lookup.put(stack, bag);
		}
		return bag;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
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
		return new InventoryBBag(player, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(LocalizationHelper.localizeFormatted("betterchests:keybind.openbag.tooltip", Keyboard.getKeyName(ClientProxy.keyBind.getKeyCode())));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		InventoryBBag inv = getInventoryFor(stack, entity);
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
