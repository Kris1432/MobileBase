package drunkmafia.mobilebase.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.tents.Tent;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	public int woolType;
	public ForgeDirection direction;
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
		tick++;
		if(tick <= 30){
			tick = 0;
			if(!tentType.isTentStable(worldObj, xCoord, yCoord, zCoord, woolType)){
				tentType.breakTent(worldObj, xCoord, yCoord, zCoord);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("direction", direction.ordinal());

	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = ForgeDirection.values()[tag.getInteger("direction")];
		tentType.direction = direction;
	}
}
