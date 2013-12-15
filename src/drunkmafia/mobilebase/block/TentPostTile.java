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
	public int[][] blocks;
	
	public TentPostTile() {
		tick = 0;
	}
	
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
		tag.setInteger("tentType", tentType.getTentID());
		tag.setInteger("woolType", woolType);
		tag.setInteger("blocksLength", blocks.length);
		tag.setInteger("blocksLength0", blocks[0].length);
		for(int i = 0; i < blocks.length; i++){
			tag.setIntArray("blocks:" + i, blocks[i]);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = ForgeDirection.values()[tag.getInteger("direction")];
		woolType = tag.getInteger("woolType");
		tentType = Tent.getTentByID(tag.getInteger("tentType"));
		tentType.direction = direction;
		blocks = new int[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
		for(int i = 0; i < blocks.length; i++){
			blocks[i] = tag.getIntArray("blocks:" + i);
		}
	}
}
