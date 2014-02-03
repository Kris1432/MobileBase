package drunkmafia.mobilebase.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import drunkmafia.mobilebase.block.InfoBlock;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	public int woolType, tentID, direction;
	public InfoBlock[][] blocks;
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
			
			tentType.writeToNBT(tag);
			
			tag.setInteger("woolType", woolType);
			tag.setInteger("blocksLength", blocks.length);
			tag.setInteger("blocksLength0", blocks[0].length);
			tag.setInteger("tentID", tentID);
			tag.setString("itemName", itemName);
			for(int i = 0; i < blocks.length; i++){
				int[] id = new int[blocks[i].length];
				int[] meta = new int[blocks[i].length];
				for(int a = 0; a < blocks.length; a++){
					id[a] = blocks[i][a].id;
					meta[a] = blocks[i][a].meta;
				}
				tag.setIntArray("blocksID:" + i, id);
				tag.setIntArray("blocksMETA:" + i, meta);
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
			
			tentType = Tent.loadFromNBT(tag);
			woolType = tag.getInteger("woolType");
			tentID = tag.getInteger("tentID");
			blocks = new InfoBlock[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
			itemName = tag.getString("itemName");
			for(int i = 0; i < blocks.length; i++){
				int[] id = tag.getIntArray("blocksID:" + i);
				int[] meta = tag.getIntArray("blocksMETA:" + i);
				for(int a = 0; a < blocks[i].length; a++)
					blocks[i][a] = new InfoBlock(id[i], meta[i]);
			}
		}
		isDummyTile = tag.getBoolean("isDummyTile");
	}
}
