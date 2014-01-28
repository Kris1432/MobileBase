package drunkmafia.mobilebase.tileentity;

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
}
