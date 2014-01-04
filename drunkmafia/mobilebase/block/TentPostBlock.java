package drunkmafia.mobilebase.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TentPostBlock extends BlockFence implements ITileEntityProvider{

	public TentPostBlock() {
		super(BlockInfo.post_ID, "planks_oak", Material.wood);
		setUnlocalizedName(BlockInfo.post_UnlocalizedName);
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y,int z, int meta) {
		if(world.isRemote) return;
		if(world.getBlockTileEntity(x, y, z) instanceof TentPostTile){
			TentPostTile tile = (TentPostTile)world.getBlockTileEntity(x, y, z);
			tile.destoryThis();
			world.removeBlockTileEntity(x, y, z);
		}
	}
	
	@Override
	public int idPicked(World world, int x, int y, int z) {
		return Block.fence.blockID;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentPostTileDummy();
	}
}
