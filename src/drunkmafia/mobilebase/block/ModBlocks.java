package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static Block tentPost;
	
	public static void init(){
		tentPost = new TentPostBlock();
		
		GameRegistry.registerBlock(tentPost, BlockInfo.post_UnlocalizedName);
		GameRegistry.registerTileEntity(TentPostTile.class, BlockInfo.post_tile);
	}
}
