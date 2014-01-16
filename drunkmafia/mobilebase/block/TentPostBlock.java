package drunkmafia.mobilebase.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameData;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TentPostBlock extends BlockFence implements ITileEntityProvider{

	public TentPostBlock() {
		super("planks_oak", Material.field_151575_d);
		func_149663_c(BlockInfo.post_UnlocalizedName);
	}

	
	//onBlockPreDestroy
	@Override
	public void func_149725_f(World world, int x, int y,int z, int meta) {
		if(world.isRemote) return;
		if(world.func_147438_o(x, y, z) instanceof TentPostTile){
			TentPostTile tile = (TentPostTile)world.func_147438_o(x, y, z);
			tile.destoryThis();
			world.func_147475_p(x, y, z); //removeBlockTileEntity
		}
	}
	
	//canConnectFenceTo
	@Override
	public boolean func_149826_e(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
        int l = GameData.blockRegistry.getId(par1IBlockAccess.func_147439_a(par2, par3, par4));

        if (l != GameData.blockRegistry.getId(this) && l != GameData.blockRegistry.getId(Blocks.fence) &&  l != GameData.blockRegistry.getId(Blocks.fence_gate)){
            Block block = GameData.blockRegistry.get(l);
            //return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
            return block != null && block.func_149688_o().isOpaque() && block.func_149686_d() ? block.func_149688_o() != Material.field_151572_C : false;
        }else{
            return true;
        }
    }
	
	//replaces idPicked
	@Override
	public Item func_149694_d(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.func_150898_a(Blocks.fence);
	}
	
	@Override
	public Item func_149650_a(int par1, Random par2Random, int par3) {
		return null;
	}

	@Override
	public TileEntity func_149915_a(World world, int var2) {
		return new TentPostTileDummy();
	}
}
