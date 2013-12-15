package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.tents.ModTents;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;
import drunkmafia.mobilebase.util.RotationHelper;

public class ItemTent extends Item{
	
	public ItemTent(int id){
		super(id);
	}
	
	
	public boolean placeTentDown(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Tent tent) {
		if(world.isRemote) return false;
		
		NBTTagCompound tag;
		if(stack.getTagCompound() == null){
			tag = new NBTTagCompound();
		}else
			tag = stack.getTagCompound();
		
		ForgeDirection direction;
		
		if(tag.hasKey("direction"))
			direction = ForgeDirection.values()[tag.getInteger("direction")];
		else
			direction = TentHelper.yawToForge(player.rotationYaw);
		
		if(TentHelper.buildTent(world, x, y, z, stack, direction, tent)){
			stack.stackSize--;
		}
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
		
		if(GuiScreen.isShiftKeyDown()){
			if(tag.hasKey("direction"))
				list.add("This tent is facing in the: " + ForgeDirection.values()[ForgeDirection.OPPOSITES[tag.getInteger("direction")]] + " direction");
			else{
				list.add("This tent does not have");
				list.add("a direction. When you place it");
				list.add("down the direction it is facing");
				list.add("will be it's default!");
			}
		}
	}
}
