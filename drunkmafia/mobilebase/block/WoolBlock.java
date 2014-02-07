package drunkmafia.mobilebase.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;
import drunkmafia.mobilebase.tileentity.TentWoolTile;

public class WoolBlock extends BlockColored implements ITileEntityProvider{
	
	public WoolBlock() {
		super(BlockInfo.wool_ID, Material.cloth);
		setUnlocalizedName(BlockInfo.wool_UnlocalizedName);
		setCreativeTab(null);
		setStepSound(soundClothFootstep);
		setHardness(0.2F);
		setTextureName("wool_colored");
	}
	
	@Override
	public int idDropped(int var, Random rand, int var2) {
		return -1;
	}
		
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta){
		if(world.isRemote) return;
		TentWoolTile woolTile = (TentWoolTile)world.getBlockTileEntity(x, y, z);
		if(woolTile != null){
			TentPostTile postTile = woolTile.getPost();
			if(postTile != null && !postTile.isDestorying){
				postTile.isDestorying = true;
				world.setBlockToAir(postTile.xCoord, postTile.yCoord, postTile.zCoord);
			}
		}
		super.breakBlock(world, x, y, z, id, meta);
	}
	
	@Override
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return Block.cloth.blockID;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentWoolTile();
	}
}
