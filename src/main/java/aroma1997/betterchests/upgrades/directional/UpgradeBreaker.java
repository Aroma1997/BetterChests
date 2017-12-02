package aroma1997.betterchests.upgrades.directional;

import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.event.ForgeEventFactory;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.DummyUpgradeType;

public class UpgradeBreaker extends DirectionalUpgrade {
	public UpgradeBreaker() {
		super(true, 1, UpgradableBlockType.CHEST.array, () -> Collections.singletonList(DummyUpgradeType.AI.getStack()));
		tickTime = 64;
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (getTickTime(chest, stack) != 0 || !hasUpgradeOperationCost(chest)) {
			return;
		}

		IBetterChest inv = (IBetterChest) chest;

		IFilter filter = ((IBetterChest) chest).getFilterFor(stack);

		int slot = InvUtil.findInInvInternal(inv, null, filter::matchesStack);

		ItemStack targetItem;
		if (slot != -1) {
			targetItem = inv.getStackInSlot(slot);
		} else if (!filter.hasStackFilter()) {
			targetItem = ItemStack.EMPTY;
		} else {
			return;
		}

		EntityPlayerMP player = chest.getFakePlayer();
		if (player == null) {
			return;
		}

		BlockPos targetPos = getPos(chest, stack);

		player.inventory.clear();
		player.setHeldItem(EnumHand.MAIN_HAND, targetItem);
		if (breakBlock(player, targetPos)) {
			drawUpgradeOperationCode(chest);
		}
		if (slot != -1) {
			inv.setInventorySlotContents(slot, player.getHeldItem(EnumHand.MAIN_HAND));
			player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			for (ItemStack current : InvUtil.getFromInv(player.inventory)) {
				current.setCount(InvUtil.putStackInInventoryInternal(current, inv, false).getCount());
			}
			player.inventory.dropAllItems();
		}
	}

	/**
	 * see PlayerInteractionManager.tryHarvestBlock
	 */
	public boolean breakBlock(EntityPlayerMP player, BlockPos pos) {
		World world = player.world;
		IBlockState state = world.getBlockState(pos);
		TileEntity te = world.getTileEntity(pos);

		if (state.getBlockHardness(world, pos) < 0 || !state.getBlock().canHarvestBlock(world, pos, player))
		{
			return false;
		}
		else {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem().onBlockStartBreak(stack, pos, player)) return false;

			world.playEvent(player, 2001, pos, Block.getStateId(state));
			ItemStack itemHand = player.getHeldItemMainhand();
			ItemStack itemHandCopy = itemHand.isEmpty() ? ItemStack.EMPTY : itemHand.copy();
			boolean canHarvest = state.getBlock().canHarvestBlock(world, pos, player);

			if (!itemHand.isEmpty()) {
				itemHand.onBlockDestroyed(world, state, pos, player);
				if (itemHand.isEmpty()) {
					ForgeEventFactory.onPlayerDestroyItem(player, itemHandCopy, EnumHand.MAIN_HAND);
				}
			}

			boolean didRemove = removeBlock(player, pos, canHarvest);
			if (didRemove && canHarvest) {
				state.getBlock().harvestBlock(world, player, pos, state, te, itemHandCopy);
			}
			return didRemove;
		}
	}

	private boolean removeBlock(EntityPlayerMP player, BlockPos pos, boolean canHarvest) {
		World world = player.world;
		IBlockState iblockstate = world.getBlockState(pos);
		boolean didRemove = iblockstate.getBlock().removedByPlayer(iblockstate, world, pos, player, canHarvest);

		if (didRemove)
		{
			iblockstate.getBlock().onBlockDestroyedByPlayer(world, pos, iblockstate);
		}

		return didRemove;
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyBreaker;
	}
}
