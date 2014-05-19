/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

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
