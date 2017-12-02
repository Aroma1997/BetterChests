package aroma1997.betterchests.upgrades.impl.plant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;
import aroma1997.betterchests.api.planter.PlantHarvestRegistry;
import aroma1997.betterchests.api.planter.PlantHarvestRegistry.IPlantHarvestRegistry;

public enum PlantHarvestHelper implements IPlantHarvestRegistry {
	INSTANCE;

	private List<IPlantHandler> plantHandlers = new ArrayList<>();
	private List<IHarvestHandler> harvestHandlers = new ArrayList<>();

	@Override
	public void registerPlantingHandler(IPlantHandler handler) {
		plantHandlers.add(handler);
	}

	@Override
	public void registerHarvestHandler(IHarvestHandler handler) {
		harvestHandlers.add(handler);
	}

	@Override
	public <T extends IPlantHandler & IHarvestHandler> void register(T handler) {
		registerPlantingHandler((IPlantHandler)handler);
		registerHarvestHandler((IHarvestHandler)handler);
	}

	@Override
	public List<IPlantHandler> getPlantHandlers() {
		return plantHandlers;
	}

	@Override
	public List<IHarvestHandler> getHarvestHandlers() {
		return harvestHandlers;
	}

	public void prepareLoading() {
		PlantHarvestRegistry.INSTANCE = this;
		register(new CropHandler());
		registerHarvestHandler(new ReedHandler());
		register(new PumpkinBlockHandler());
		register(new PumpkinHandler());
		registerHarvestHandler(new NetherWartHandler());
		register(new TreeHandler());
		register(new CocoaHandler());

		//TODO: flowers, mushrooms, chorus, IC2 rubber trees, Pam's, Mystical Crops, ...
	}

	public void finishLoading() {
		Collections.sort(plantHandlers, new Comparator<IPlantHandler>() {
			@Override
			public int compare(IPlantHandler o1, IPlantHandler o2) {
				return -Integer.compare(o1.getPlantPriority(), o2.getPlantPriority());
			}
		});

		Collections.sort(harvestHandlers, new Comparator<IHarvestHandler>() {
			@Override
			public int compare(IHarvestHandler o1, IHarvestHandler o2) {
				return -Integer.compare(o1.getHarvestPriority(), o2.getHarvestPriority());
			}
		});

		plantHandlers = Collections.unmodifiableList(plantHandlers);
		harvestHandlers = Collections.unmodifiableList(harvestHandlers);
	}

	public static void breakBlockHandleDrop(World world, BlockPos pos, IBlockState state, IBetterChest chest) {
		NonNullList<ItemStack> list = NonNullList.create();
		state.getBlock().getDrops(list, world, pos, state, 0);
		boolean change = list.isEmpty();
		for (ItemStack stack : list) {
			if (InvUtil.putStackInInventoryInternal(stack, chest, false).getCount() != stack.getCount()) {
				change = true;
			}
		}
		if (change) {
			world.setBlockToAir(pos);
		}
	}
}
