package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderBlock extends Block implements ITileEntityProvider{
	
	@SideOnly(Side.CLIENT)
    private Icon workbenchIconTop, workbenchIconBlueTop, workbenchIconFront;
	
	public TentBuilderBlock() {
		super(BlockInfo.tentBuilder_ID, Material.wood);
		setUnlocalizedName(BlockInfo.tentBuilder_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
	}
		
	@Override
	public Icon getIcon(int side, int dmg) {
		 return side == 1 ? (dmg == 0 ? this.workbenchIconTop : workbenchIconBlueTop) : (side == 0 ? Block.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.workbenchIconFront));
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		this.blockIcon = register.registerIcon("crafting_table_side");
        this.workbenchIconTop = register.registerIcon("crafting_table_top");
        this.workbenchIconBlueTop = register.registerIcon(ModInfo.MODID +":builder_top");
        this.workbenchIconFront = register.registerIcon("crafting_table_front");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return true;
		FMLNetworkHandler.openGui(player, MobileBase.instance, BlockInfo.tentBuilder_Gui_ID, world, x, y, z);
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		TentBuilderTile tile = (TentBuilderTile) world.getBlockTileEntity(x, y, z);
		for(int i = 0; i < tile.getSizeInventory(); i++){
			ItemStack stack = tile.getStackInSlot(i);
			if(stack != null){
				EntityItem item = new EntityItem(world, x, y, z, stack);
				world.spawnEntityInWorld(item);
			}
		}
		super.breakBlock(world, x, y, z, id, meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TentBuilderTile();
	}
}
