/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityBag extends EntityItem {

	public EntityBag(World par1World) {
		super(par1World);
	}

	public EntityBag(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	public EntityBag(World par1World, double par2, double par4, double par6,
			ItemStack par8ItemStack) {
		super(par1World, par2, par4, par6, par8ItemStack);
	}

	@Override
	// Is invulnerable
    public boolean func_180431_b(DamageSource p_180431_1_)
    {
		BagInventory inv = ItemBag.getInventory(getEntityItem());
		if (inv == null
				|| !inv.isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())) {
			return false;
		}
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		BagInventory inv = new BagInventory(getEntityItem());
		if (inv != null
				&& inv.isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())) {
			setAgeToCreativeDespawnTime();
		}
	}

}
