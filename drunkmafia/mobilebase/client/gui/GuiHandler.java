package drunkmafia.mobilebase.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.client.gui.container.TentBluePrinterContainer;
import drunkmafia.mobilebase.client.gui.container.TentBuilderContainer;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.tileentity.TentBluePrinterTile;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID){
			case 0:
				ItemStack stack = player.inventory.getCurrentItem();
				if(stack != null && stack.getItem().itemID == ModItems.bluePrint.itemID){
					System.out.println("Open Container");
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
				if(stack != null && stack.getItem().itemID == ModItems.bluePrint.itemID){
					System.out.println("Open Gui");
					return new BluePrintGui();
				}
		}
		return null;
	}

}
