package drunkmafia.mobilebase.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import drunkmafia.mobilebase.client.gui.container.*;
import drunkmafia.mobilebase.tileentity.*;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID){
			case 0:
				if(tile != null && tile instanceof TentBluePrinterTile){
					return new TentBluePrinterContainer((TentBluePrinterTile) tile, player);
				}
				break;
			case 1:
				if(tile != null && tile instanceof TentBuilderTile){
					return new TentBuilderContainer((TentBuilderTile) tile, player);
				}
				break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID){
			case 0:
				if(tile != null && tile instanceof TentBluePrinterTile){
					return new TentBluePrinterGui((TentBluePrinterTile) tile, player);
				}
				break;
			case 1:
				if(tile != null && tile instanceof TentBuilderTile){
					return new TentBuilderGui((TentBuilderTile) tile, player);
				}
				break;
		}
		return null;
	}

}
