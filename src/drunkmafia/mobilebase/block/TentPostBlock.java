package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TentPostBlock extends Block implements ITileEntityProvider{

	public TentPostBlock() {
		super(BlockInfo.post_ID, Material.wood);
		setUnlocalizedName(BlockInfo.post_UnlocalizedName);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = tile.tentType.getItemVersionOfTent(world, x, y, z, tile.woolType);
		EntityItem item = new EntityItem(world, x, y, z, stack);
		tile.tentType.breakTent(world, x, y, z);
		world.spawnEntityInWorld(item);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentPostTile();
	}
}
