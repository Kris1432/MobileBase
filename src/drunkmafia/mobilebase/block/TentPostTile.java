package drunkmafia.mobilebase.block;

import net.minecraft.tileentity.TileEntity;
import drunkmafia.mobilebase.tents.Tent;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
		tick++;
		if(tick <= 10){
			tick = 0;
			tentType.isTentStable(worldObj, xCoord, yCoord, zCoord);
			System.out.println("tick");
		}
	}
}
