package drunkmafia.mobilebase.client.gui;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.item.ModItems;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID){
			case 0:
				ItemStack stack = player.inventory.getCurrentItem();
				NBTTagCompound tag = stack.getTagCompound();
				if(stack != null && stack.getItem().itemID == ModItems.bluePrint.itemID && tag != null){
					return new BluePrintContainer();
				}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID){
			case 0:
				ItemStack stack = player.inventory.getCurrentItem();
				NBTTagCompound tag = stack.getTagCompound();
				if(stack != null && tag != null && stack.getItem().itemID == ModItems.bluePrint.itemID){
					return new BluePrintGui(tag);
				}
		}
		return null;
	}

}
