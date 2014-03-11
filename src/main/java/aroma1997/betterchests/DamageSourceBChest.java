package aroma1997.betterchests;

import net.minecraft.util.DamageSource;

public class DamageSourceBChest extends DamageSource {

	public static final DamageSourceBChest INSTANCE = new DamageSourceBChest();

	private DamageSourceBChest() {
		super("betterchests:interact.player");
		setDamageBypassesArmor();
		setDamageAllowedInCreativeMode();
	}

}
