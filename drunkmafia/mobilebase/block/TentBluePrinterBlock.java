package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentBluePrinterTile;

public class TentBluePrinterBlock extends Block implements ITileEntityProvider{
	
	public TentBluePrinterBlock() {
		super(BlockInfo.tentBluePrinter_ID, Material.wood);
		setUnlocalizedName(BlockInfo.tentBluePrinter_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		setTextureName("crafting_table");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		FMLNetworkHandler.openGui(player, MobileBase.instance, BlockInfo.tentBluePrinter_Gui_ID, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentBluePrinterTile();
	}
}
