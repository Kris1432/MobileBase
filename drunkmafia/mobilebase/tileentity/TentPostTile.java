package drunkmafia.mobilebase.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import drunkmafia.mobilebase.block.InfoBlock;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	public int woolType, direction;
	public InfoBlock[][] blocks;
	public String itemName, directionName;
	public boolean isDummyTile, isDestorying;
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	public void destoryThis(){
		if(!isDestorying){
			ItemStack stack = TentHelper.getItemVersionOfTent(worldObj, xCoord, yCoord, zCoord, woolType, tentType, direction);
			EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
			TentHelper.breakTent(worldObj, xCoord, yCoord, zCoord, tentType, direction, stack.getTagCompound());        
			worldObj.spawnEntityInWorld(item);
		}else{
			ItemStack stack = new ItemStack(ModItems.tent);
			NBTTagCompound tag = new NBTTagCompound();
			tentType.writeToNBT(tag);
			stack.setTagCompound(tag);
			stack.setItemName(itemName);
			TentHelper.destoryTentOutside(worldObj, xCoord, yCoord, zCoord, tentType, direction);
			TentHelper.rebuildFloor(worldObj, xCoord, yCoord, zCoord, tentType, direction);
			EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
			worldObj.spawnEntityInWorld(item);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setBoolean("isDummy", isDummyTile);
		if(!isDummyTile){
			tag.setByte("direction", (byte) direction);
			tag.setString("directionName", directionName);
			tentType.writeToNBT(tag);
			tag.setInteger("woolType", woolType);
			tag.setInteger("blocksLength", blocks.length);
			tag.setInteger("blocksLength0", blocks[0].length);
			tag.setString("itemName", itemName);
			for(int i = 0; i < blocks.length; i++){
				int[] id = new int[blocks[i].length];
				int[] meta = new int[blocks[i].length];
				for(int a = 0; a < blocks[0].length; a++){
					id[a] = blocks[i][a].id;
					meta[a] = blocks[i][a].meta;
				}
				tag.setIntArray("blocksID:" + i, id);
				tag.setIntArray("blocksMETA:" + i, meta);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		isDummyTile = tag.getBoolean("isDummyTile");
		if(!isDummyTile){
			direction = tag.getByte("direction");
			directionName = tag.getString("directionName");
			
			tentType = Tent.loadFromNBT(tag);
			woolType = tag.getInteger("woolType");
			blocks = new InfoBlock[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
			itemName = tag.getString("itemName");
			for(int i = 0; i < blocks.length; i++){
				int[] id = tag.getIntArray("blocksID:" + i);
				int[] meta = tag.getIntArray("blocksMETA:" + i);
				for(int a = 0; a < blocks[i].length; a++)
					blocks[i][a] = new InfoBlock(id[i], meta[i]);
			}
		}
	}
}
