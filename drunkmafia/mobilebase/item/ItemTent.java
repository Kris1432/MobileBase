package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;
import drunkmafia.mobilebase.tileentity.TentPostTile;

public class ItemTent extends Item{
		
	public ItemTent(){
		super(ItemInfo.tent_ID);
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);	
		setCreativeTab(MobileBase.tab);
		maxStackSize = 1;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;

		NBTTagCompound tag;
		if(stack.getTagCompound() == null){
			tag = new NBTTagCompound();
		}else
			tag = stack.getTagCompound();
		
		int direction = TentHelper.convertForgeDirToTentDir(TentHelper.yawToForge(player.rotationYaw));
		
		tag.setString("directionName", TentHelper.yawToForge(player.rotationYaw).toString());
		
		int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
		for(int y1 = 0; y1 < temp.length; y1++)
			for(int x1 = 0; x1 < temp[y1].length; x1++)
				temp[y1][x1] = tag.getIntArray("tentStructure:" + y1 + x1);
		
		Tent tent = new Tent(temp);
		
		if(TentHelper.buildTent(world, x, y, z, stack, direction, tent)){
			stack.stackSize--;
		}else{
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Area is not clear, please clear any nearby blocks or try another place"));
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
		
		if(GuiScreen.isShiftKeyDown()){
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
