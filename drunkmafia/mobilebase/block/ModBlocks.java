package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static Block tentPost;
	public static Block wool;
	
	public static void init(){
		tentPost = new TentPostBlock();
		wool = new WoolBlock();
		
		GameRegistry.registerBlock(wool, BlockInfo.wool_UnlocalizedName);
		GameRegistry.registerBlock(tentPost, BlockInfo.post_UnlocalizedName);
		GameRegistry.registerTileEntity(TentPostTile.class, BlockInfo.post_tile);
		GameRegistry.registerTileEntity(TentPostTileDummy.class, BlockInfo.postDummy_tile);
	}
}
