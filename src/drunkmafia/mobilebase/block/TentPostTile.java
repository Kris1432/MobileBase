package drunkmafia.mobilebase.block;

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
	public String itemName, playersUsername;
	
	public TentPostTile() {
		tick = 0;
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
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
		TentHelper.breakTent(worldObj, xCoord, yCoord, zCoord, tentType, direction);
		
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 10), (double)(this.yCoord + 10), (double)(this.zCoord + 10));
        axisalignedbb.maxY = (double)this.worldObj.getHeight();
        List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        Iterator iterator = list.iterator();
        EntityPlayer player = null;

        while (iterator.hasNext()){
        	player = (EntityPlayer)iterator.next();
            if(player != null && player.username == playersUsername){
            	for(int i = 0; i < player.inventory.mainInventory.length; i++){
            		if(player.inventory.mainInventory[i] == null){
            			player.inventory.mainInventory[i] = stack;
            			return;
            		}
            	}
            }
            	
        }
        
		worldObj.spawnEntityInWorld(item);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("direction", direction.ordinal());
		tag.setInteger("tentType", tentType.getTentID());
		tag.setInteger("woolType", woolType);
		tag.setInteger("blocksLength", blocks.length);
		tag.setInteger("blocksLength0", blocks[0].length);
		tag.setInteger("tentID", tentID);
		tag.setString("itemName", itemName);
		tag.setString("playersName", playersUsername);
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
		tentID = tag.getInteger("tentID");
		blocks = new int[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
		itemName = tag.getString("itemName");
		playersUsername = tag.getString("playersUsername");
		for(int i = 0; i < blocks.length; i++){
			blocks[i] = tag.getIntArray("blocks:" + i);
		}
	}
}
