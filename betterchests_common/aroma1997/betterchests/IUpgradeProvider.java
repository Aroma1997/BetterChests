package aroma1997.betterchests;


public interface IUpgradeProvider {
	public int getAmountUpgrade(Upgrade upgrade);
	public boolean isUpgradeInstalled(Upgrade upgrade);
	public void setAmountUpgrade(Upgrade upgrade, int amount);
	public boolean hasEnergy();
}
