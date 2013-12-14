package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TentPostBlock extends Block implements ITileEntityProvider{

	public TentPostBlock() {
		super(BlockInfo.post_ID, Material.wood);
		setUnlocalizedName(BlockInfo.post_UnlocalizedName);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentPostTile();
	}
}
