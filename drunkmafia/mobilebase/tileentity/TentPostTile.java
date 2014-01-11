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
	public int woolType, tentID;
	public ForgeDirection direction;
	public int[][] blocks;
	public String itemName;
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
			tag.setInteger("direction", direction.ordinal());
			tag.setInteger("tentType", tentType.getTentID());
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
			direction = ForgeDirection.values()[tag.getInteger("direction")];
			woolType = tag.getInteger("woolType");
			tentType = Tent.getTentByID(tag.getInteger("tentType"));
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
