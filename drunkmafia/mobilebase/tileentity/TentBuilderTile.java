package drunkmafia.mobilebase.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
	public boolean wool, fence, ender, woolFinished, fenceFinished, enderFinished, assmebleTent, reset;
	public ItemStack tent, blueprint;
	
	public TentBuilderTile() {
		inventory = new ItemStack[getSizeInventory()];
		assmebleTent = false;
		reset = false;
		tick = 0;
		tickMax = 30;
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
		
		if(assmebleTent){
			tick++;
			if(tick <= tickMax){
				if(getStackInSlot(0) != null && getStackInSlot(0).getTagCompound().getInteger("tentY") == blueprint.getTagCompound().getInteger("tentY")){
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
				}
				
				if(woolFinished && fenceFinished && enderFinished){
					setInventorySlotContents(4, tent);
					reset();
					worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, blockType.blockID, 2);
				}
				
				if(getStackInSlot(0) == null){
					dropProgress(new ItemStack(Block.cloth, deltaWool, woolMeta));
					dropProgress(new ItemStack(Block.fence, deltaWool));
					dropProgress(new ItemStack(Item.enderPearl, deltaEnder));
					reset();
					worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, blockType.blockID, 2);
				}
				tick = 0;
			}
		}
	}
	
	public void reset(){
		tent = null;
		assmebleTent = false;
		woolFinished = false;
		fenceFinished = false;
		enderFinished = false;
		deltaWool = 0;
		deltaFence = 0;
		deltaEnder = 0;
		woolAmount = 0;
		fenceAmount = 0;
		enderAmount = 0;
		
		reset = true;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
							this.blueprint = blueprint;
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
				if(tag.hasKey("tentName")){
					tent.setItemName(tag.getString("tentName"));
				}
				int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
				for(int y = 0; y < temp.length; y++)
					for(int x = 0; x < temp[y].length; x++)
						temp[y][x] = tag.getIntArray("tentStructure:" + y + x);
				
				Tent tentTemp = new Tent(temp);
				woolAmount = tentTemp.getAmountWool();
				fenceAmount = tentTemp.getAmountFences();
				enderAmount = tentTemp.getStrucutureCount() / 10;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
			}else if(getStackInSlot(0) == null && !assmebleTent){
				woolAmount = 0;
				enderAmount = 0;
				enderAmount = 0;
				tent = null;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	 
	 public void dropProgress(ItemStack stack){
		 EntityItem entity = new EntityItem(worldObj, xCoord, yCoord + 0.5, zCoord, stack);
		 worldObj.spawnEntityInWorld(entity);
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
    public boolean isItemValidForSlot(int i, ItemStack stack) {
    	switch(i){
    		case 0:
    			return stack.getItem() instanceof ItemBlueprint;
    		case 1:
    			return stack.getItem().itemID == Block.cloth.blockID;
    		case 2:
    			return stack.getItem().itemID == Block.fence.blockID;
    		case 3:
    			return stack.getItem() instanceof ItemEnderPearl;
    		default:
    			return false;
    	}
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
    	if(assmebleTent || reset){
    		tag.setBoolean("assmebleTent",  assmebleTent);
    		tag.setBoolean("reset", reset);
    		tag.setInteger("deltaWool", deltaWool);
    		tag.setInteger("deltaFence", deltaFence);
    		tag.setInteger("deltaEnder", deltaEnder);
    		tag.setInteger("woolAmount", woolAmount);
    		tag.setInteger("fenceAmount", fenceAmount);
    		tag.setInteger("enderAmount", enderAmount);
    		tag.setBoolean("wool", wool);
    		tag.setBoolean("fence", fence);
    		tag.setBoolean("ender", ender);
    		tag.setBoolean("woolFinished", woolFinished);
    		tag.setBoolean("fenceFinished", fenceFinished);
    		tag.setBoolean("enderFinished", enderFinished);
    		if(tent != null && blueprint != null){
    			NBTTagCompound tentTag = new NBTTagCompound();
    			NBTTagCompound printTag = new NBTTagCompound();
    			tent.writeToNBT(tentTag);
    			blueprint.writeToNBT(printTag);
    			tag.setCompoundTag("tent", tentTag);
    			tag.setCompoundTag("blueprint", printTag);
    		}
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
    	reset = tag.getBoolean("reset");
    	if(assmebleTent || reset){
    		deltaWool = tag.getInteger("deltaWool");
    		deltaFence = tag.getInteger("deltaFence");
    		deltaEnder = tag.getInteger("deltaEnder");
    		woolAmount = tag.getInteger("woolAmount");
    		fenceAmount = tag.getInteger("fenceAmount");
    		enderAmount = tag.getInteger("enderAmount");
    		wool = tag.getBoolean("wool");
    		fence = tag.getBoolean("fence");
    		ender = tag.getBoolean("ender");
    		woolFinished = tag.getBoolean("woolFinished");
    		fenceFinished = tag.getBoolean("fenceFinished");
    		enderFinished = tag.getBoolean("enderFinished");    		
    		if(tag.hasKey("tent") && tag.hasKey("blueprint")){
    			tent = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("tent"));
    			blueprint = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("blueprint"));
    		}
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
