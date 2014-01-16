package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class ItemTent extends Item{
	
	protected Tent tent;
	
	public ItemTent(){
		maxStackSize = 1;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;

		NBTTagCompound tag;
		if(stack.getTagCompound() == null){
			tag = new NBTTagCompound();
		}else
			tag = stack.getTagCompound();
		
		int direction;
		
		if(tag.hasKey("direction"))
			direction = tag.getByte("direction");
		else
			direction = TentHelper.convertForgeDirToTentDir(TentHelper.yawToForge(player.rotationYaw));
		
		tag.setString("directionName", TentHelper.yawToForge(player.rotationYaw).toString());
		
		if(TentHelper.buildTent(world, x, y, z, stack, direction, tent)){
			stack.stackSize--;
		}
		
		TentHelper.movePlayer(player, world, x, y, z, tent, direction);
		
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound tag;
		if(stack.getTagCompound() == null){
			tag = new NBTTagCompound();
		}else{
			tag = stack.getTagCompound();
		}
		
		if(GuiScreen.func_146272_n()){ //isShiftDown
			if(tag.hasKey("direction"))
				list.add("This tent is facing in the: " + tag.getString("directionName") + " direction");
			else{
				list.add("This tent does not have");
				list.add("a direction. When you place it");
				list.add("down the direction it is facing");
				list.add("will be it's default!");
			}
		}
	}
}
