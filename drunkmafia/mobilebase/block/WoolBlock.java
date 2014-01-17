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

public class WoolBlock extends BlockColored {
	
	public WoolBlock() {
		super(BlockInfo.wool_ID, Material.cloth);
		setUnlocalizedName(BlockInfo.wool_UnlocalizedName);
		setStepSound(soundClothFootstep);
		setHardness(0.2F);
		setTextureName("wool_colored");
	}
	
	@Override
	public int idDropped(int var, Random rand, int var2) {
		return -1;
	}
	
	@Override
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return Block.cloth.blockID;
	}
}
