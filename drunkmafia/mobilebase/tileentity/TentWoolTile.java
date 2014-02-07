package drunkmafia.mobilebase.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TentWoolTile extends TileEntity{
	
	private int x, y, z;
	
	public void setPostPos(int xCoord, int yCoord, int zCoord){
		x = xCoord;
		y = yCoord;
		z = zCoord;
	}
	
	public TentPostTile getPost(){
		return (TentPostTile)worldObj.getBlockTileEntity(x, y, z);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("x", x);
		tag.setInteger("y", x);
		tag.setInteger("z", x);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		x = tag.getInteger("x");
		y = tag.getInteger("y");
		z = tag.getInteger("z");
	}
}
