package drunkmafia.mobilebase.block;

import java.util.Random;

import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
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
	public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
        int l = par1IBlockAccess.getBlockId(par2, par3, par4);

        if (l != this.blockID && l != Block.fenceGate.blockID &&  l != Block.fence.blockID){
            Block block = Block.blocksList[l];
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }else{
            return true;
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
