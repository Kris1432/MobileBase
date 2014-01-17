package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.*;

public class ModBlocks {
	
	public static Block tentPost;
	public static Block wool;
	public static Block tentBuilder;
	
	public static void init(){
		tentPost = new TentPostBlock();
		wool = new WoolBlock();
		//tentBuilder = new TentBuilderBlock();
		
		GameRegistry.registerBlock(wool, BlockInfo.wool_UnlocalizedName);
		GameRegistry.registerBlock(tentPost, BlockInfo.post_UnlocalizedName);
		//GameRegistry.registerBlock(tentBuilder, BlockInfo.tentBuilder_UnlocalizedName);
		
		GameRegistry.registerTileEntity(TentPostTile.class, BlockInfo.post_tile);
		GameRegistry.registerTileEntity(TentPostTileDummy.class, BlockInfo.postDummy_tile);
		//GameRegistry.registerTileEntity(TentBuilderTile.class, BlockInfo.tentBuilder_Tile);
	}
}
