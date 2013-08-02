package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;


public class TileEntityBChest extends TileEntityChest {
	
	private short stackLimit;
	private short slotLimit;
	private static final short SLOTLIMIT = 36;

	public TileEntityBChest() {
		stackLimit = 1;
		slotLimit = 0;
	}

	@Override
	public String getInvName() {
		return "Adjustable Chest";
	}
	
	@Override
	public int getSizeInventory() {
		return slotLimit;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}
	
	public boolean upgrade(EntityPlayer player, World world) {
		if (!(player.getHeldItem().getItem() instanceof ItemUpgrade)) return false;
		ItemStack item = player.getHeldItem();
		Upgrade upgrade = Upgrade.values()[item.getItemDamage()];
		switch (upgrade) {
			case SLOT:  {
				if (slotLimit + 9 > SLOTLIMIT) return false;
				slotLimit += 9;
				return true;
			}
			case STACK: {
				if (stackLimit + 1 > 64) return false;
				stackLimit += 1;
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.slotLimit = par1NBTTagCompound.getShort("slotLimit");
		this.stackLimit = par1NBTTagCompound.getShort("stackLimit");
        super.readFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("slotLimit", this.slotLimit);
        par1NBTTagCompound.setShort("stackLimit", this.stackLimit);
    }
	
}
