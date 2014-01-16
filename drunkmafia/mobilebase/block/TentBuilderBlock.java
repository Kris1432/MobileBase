package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameData;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderBlock extends Block implements ITileEntityProvider{
	public TentBuilderBlock() {
		super(Material.field_151575_d);
		func_149658_d("crafting_table");
		func_149663_c(BlockInfo.tentBuilder_UnlocalizedName);
		func_149647_a(MobileBase.tab);
		
	}
	
	@Override
	public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		FMLNetworkHandler.openGui(player, MobileBase.instance, BlockInfo.tentBuilder_Gui_ID, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity func_149915_a(World var1, int var2) {
		return new TentBuilderTile();
	}
}
