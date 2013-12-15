package drunkmafia.mobilebase.block;

import drunkmafia.mobilebase.tents.TentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class TentPostBlock extends BlockFence implements ITileEntityProvider{

	public TentPostBlock() {
		super(BlockInfo.post_ID, "planks_oak", Material.wood);
		setUnlocalizedName(BlockInfo.post_UnlocalizedName);
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y,int z, int meta) {
		if(world.isRemote) return;
		
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = TentHelper.getItemVersionOfTent(world, x, y, z, tile.woolType, tile.tentType, tile.direction);
		EntityItem item = new EntityItem(world, x, y, z, stack);
		TentHelper.playerBreaksTent(world, x, y, z, tile.tentType, tile.direction);
		world.spawnEntityInWorld(item);
	}
	
	@Override
	public int idPicked(World world, int x, int y, int z) {
		return Block.fence.blockID;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentPostTile();
	}
}
