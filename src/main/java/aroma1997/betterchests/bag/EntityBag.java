package aroma1997.betterchests.bag;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;

public class EntityBag extends EntityItem {
	public EntityBag(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
	}

	public EntityBag(World worldIn) {
		super(worldIn);
	}

	private boolean hasHardnessInstalled() {
		return UpgradeHelper.INSTANCE.booleanSum(getInv(), ChestModifier.HARDNESS, false);
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		return source != DamageSource.OUT_OF_WORLD && hasHardnessInstalled();
	}

	protected BasicBagInventory getInv() {
		return ((ItemBBagBase<?>)getItem().getItem()).getInventoryFor(getItem(), this);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		getInv().tick();
		if (hasHardnessInstalled()) {
			setAgeToCreativeDespawnTime();
		}
	}

}
