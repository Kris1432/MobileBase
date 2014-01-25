package drunkmafia.mobilebase.block;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.network.ForgeNetworkHandler;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderBlock extends Block implements ITileEntityProvider{
	public TentBuilderBlock() {
		super(BlockInfo.tentBuilder_ID, Material.wood);
		setUnlocalizedName(BlockInfo.tentBuilder_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
	}
	/**
	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	**/
	
	@Override
	public void registerIcons(IconRegister register) {
		workbench.registerIcons(register);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		FMLNetworkHandler.openGui(player, MobileBase.instance, BlockInfo.tentBuilder_Gui_ID, world, x, y, z);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentBuilderTile();
	}
}
