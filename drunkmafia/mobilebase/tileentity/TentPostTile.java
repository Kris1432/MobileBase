package drunkmafia.mobilebase.tileentity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	public int woolType, tentID, direction;
	public int[][] blocks;
	public String itemName, directionName;
	public boolean isDummyTile;
	
	public TentPostTile() {
		tick = 0;
	}
	
	 @Override
     public void updateEntity() {
             if(worldObj.isRemote || isDummyTile) return;
             tick++;
             if(tick <= 30){
                     tick = 0;
                     if(!TentHelper.isTentStable(worldObj, xCoord, yCoord, zCoord, woolType, tentType, direction)){
                             worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                     }
             }
     }
	
	public void destoryThis(){
		ItemStack stack = TentHelper.getItemVersionOfTent(worldObj, xCoord, yCoord, zCoord, woolType, tentType, direction);
		EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
		TentHelper.breakTent(worldObj, xCoord, yCoord, zCoord, tentType, direction, stack.getTagCompound());        
		worldObj.spawnEntityInWorld(item);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(!isDummyTile){
			tag.setByte("direction", (byte) direction);
			tag.setString("directionName", directionName);
			
			tag.setInteger("tentY", tentType.getTentY());
			tag.setInteger("tentX", tentType.getTentX());
			tag.setInteger("tentZ", tentType.getTentZ());
			for(int y = 0; y < tentType.getTentY(); y++)
				for(int x = 0; x < tentType.getTentX(); x++)
					tag.setIntArray("tentStructure:" + y + x, tentType.getStructure()[0][y][x]);
			
			tag.setInteger("woolType", woolType);
			tag.setInteger("blocksLength", blocks.length);
			tag.setInteger("blocksLength0", blocks[0].length);
			tag.setInteger("tentID", tentID);
			tag.setString("itemName", itemName);
			for(int i = 0; i < blocks.length; i++){
				tag.setIntArray("blocks:" + i, blocks[i]);
			}
		}
		tag.setBoolean("isDummy", isDummyTile);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(!isDummyTile){
			direction = tag.getByte("direction");
			directionName = tag.getString("directionName");
			
			int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
			for(int y = 0; y < temp.length; y++)
				for(int x = 0; x < temp[y].length; x++)
					temp[y][x] = tag.getIntArray("tentStructure:" + y + x);
			
			tentType = new Tent(temp);
			woolType = tag.getInteger("woolType");
			tentID = tag.getInteger("tentID");
			blocks = new int[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
			itemName = tag.getString("itemName");
			for(int i = 0; i < blocks.length; i++){
				blocks[i] = tag.getIntArray("blocks:" + i);
			}
		}
		isDummyTile = tag.getBoolean("isDummyTile");
	}
}
