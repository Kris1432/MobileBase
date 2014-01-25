package drunkmafia.mobilebase.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import drunkmafia.mobilebase.item.ItemBlueprint;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.tents.Tent;

public class TentBuilderTile extends TileEntity implements IInventory{

	private ItemStack[] inventory;
	public int woolAmount, fenceAmount, enderAmount;
	public int deltaWool, deltaFence, deltaEnder;
	private int woolMeta, tick, tickMax;
	private boolean assmebleTent, wool, fence, ender, woolFinished, fenceFinished, enderFinished;
	public ItemStack tent;
	
	public TentBuilderTile() {
		inventory = new ItemStack[getSizeInventory()];
		assmebleTent = false;
		tick = 0;
		tickMax = 30;
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
		
		if(assmebleTent && getStackInSlot(0) != null){
			tick++;
			if(tick <= tickMax){
				if(wool || fence || ender){
					if(!woolFinished && decrStackSize(1, 1) != null){
						wool = true;
						deltaWool++;
						if(deltaWool == woolAmount){
							woolFinished = true;
						}
					}else if(!woolFinished)
						wool = false;
					if(!fenceFinished && decrStackSize(2, 1) != null){
						fence = true;
						deltaFence++;
						if(deltaFence == fenceAmount){
							fenceFinished = true;
						}
					}else if(!fenceFinished)
						fence = false;
					if(!enderFinished && decrStackSize(3, 1) != null){
						ender = true;
						deltaEnder++;
						if(deltaEnder == enderAmount){
							enderFinished = true;
						}
					}else if(!enderFinished)
						ender = false;
				}
				tick = 0;
			}
			
			if(woolFinished && fenceFinished && enderFinished){
				setInventorySlotContents(4, tent);
				assmebleTent = false;
				woolFinished = false;
				fenceFinished = false;
				enderFinished = false;
				deltaWool = 0;
				deltaFence = 0;
				deltaEnder = 0;
			}
		}
	}
	
	public void assembleTent(){
		if(!assmebleTent){
			ItemStack blueprint = getStackInSlot(0);
			if(blueprint != null && blueprint.getItem() instanceof ItemBlueprint && blueprint.getTagCompound().hasKey("tentY")){
				ItemStack wool = getStackInSlot(1);
				if(wool != null && wool.getItem().itemID == Block.cloth.blockID){
					woolMeta = wool.getItemDamage();
					ItemStack fence = getStackInSlot(2);
					if(fence != null && fence.getItem().itemID == Block.fence.blockID){
						ItemStack ender = getStackInSlot(3);
						if(ender != null && ender.getItem() instanceof ItemEnderPearl){
							assmebleTent = true;
							deltaWool = 0;
							deltaFence = 0;
							deltaEnder = 0;
							this.wool = true;
							this.fence = true;
							this.ender = true;
						}
					}
				}
			}
		}
	}
	
	@Override
    public int getSizeInventory() {
		return 5;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
    	return inventory[i];
    }

    @Override
 	public ItemStack decrStackSize(int par1, int par2) {
	     if (this.inventory[par1] != null) {
	         ItemStack ItemStack;
	         
	         if (this.inventory[par1].stackSize <= par2) {
	             ItemStack = this.inventory[par1];
	             this.inventory[par1] = null;
	             this.onInventoryChanged();
	             return ItemStack;
	         } else {
	             ItemStack = this.inventory[par1].splitStack(par2);
	             
	             if (this.inventory[par1].stackSize == 0) {
	                 this.inventory[par1] = null;
	             }
	             
	             this.onInventoryChanged();
	             return ItemStack;
	         }
	     } else {
	         return null;
	     }
    }

	 @Override
	 public ItemStack getStackInSlotOnClosing(int par1) {
	     if (this.inventory[par1] != null) {
	         ItemStack ItemStack = this.inventory[par1];
	         this.inventory[par1] = null;
	         return ItemStack;
	     } else {
	         return null;
	     }
	 }

	 @Override
	 public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
	     this.inventory[par1] = par2ItemStack;
	     
	     if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
	         par2ItemStack.stackSize = this.getInventoryStackLimit();
	     }
	     
	     this.onInventoryChanged();
	 }
	 
	 @Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		if(!assmebleTent){
			if(getStackInSlot(0) != null && getStackInSlot(0).getTagCompound() != null && getStackInSlot(0).getTagCompound().hasKey("tentY")){
				NBTTagCompound tag = getStackInSlot(0).getTagCompound();
				tent = new ItemStack(ModItems.tent);
				tent.setTagCompound(tag);
				tent.setItemDamage(getStackInSlot(1) != null ? getStackInSlot(1).getItemDamage() : 0);
				
				int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
				for(int y = 0; y < temp.length; y++)
					for(int x = 0; x < temp[y].length; x++)
						temp[y][x] = tag.getIntArray("tentStructure:" + y + x);
				
				Tent tentTemp = new Tent(temp);
				woolAmount = tentTemp.getAmountWool();
				fenceAmount = tentTemp.getAmountFences();
				enderAmount = tentTemp.getStrucutureCount() / 10;
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

     @Override
     public String getInvName() {
             return null;
     }

     @Override
     public boolean isInvNameLocalized() {
             return false;
     }

     @Override
     public int getInventoryStackLimit() {
             return 4096;
     }

     @Override
     public boolean isUseableByPlayer(EntityPlayer entityplayer) {
             return entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
     }

     @Override
     public void openChest() {}

     @Override
     public void closeChest() {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack ItemStack) {
    	return false;
    }
     
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    	super.writeToNBT(tag);
    	for(int i = 0; i < getSizeInventory(); i++){
    		ItemStack stack = getStackInSlot(i);
    		if(stack != null){
    			NBTTagCompound itemTag = new NBTTagCompound();
    			stack.writeToNBT(itemTag);
    			tag.setCompoundTag("item" + i, itemTag);
    		}
    	}
    	if(assmebleTent){
    		tag.setBoolean("assmebleTent",  assmebleTent);
    		tag.setInteger("deltaWool", deltaWool);
    		tag.setInteger("deltaFence", deltaFence);
    		tag.setInteger("deltaEnder", deltaEnder);
    		if(tent != null)
    			tag.setCompoundTag("tent",  tent.getTagCompound());
    	}
    }
     
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    	super.readFromNBT(tag);
    	for(int i = 0; i < getSizeInventory(); i++){
    		if(tag.hasKey("item" + i)){
    			inventory[i] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item" + i));
    		}
    	}
    	assmebleTent = tag.getBoolean("assmebleTent");
    	if(assmebleTent){
    		deltaWool = tag.getInteger("deltaWool");
    		deltaFence = tag.getInteger("deltaFence");
    		deltaEnder = tag.getInteger("deltaEnder");
    		if(tag.hasKey("tent"))
    			tent = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("tent"));
    	}
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        NBTTagCompound tag = pkt.data;
        readFromNBT(tag);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, blockMetadata, tag);
    }
}
