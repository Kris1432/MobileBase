package drunkmafia.mobilebase.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;
import drunkmafia.mobilebase.tileentity.TentPostTile;
import drunkmafia.mobilebase.tileentity.TentPostTileDummy;
import drunkmafia.mobilebase.tileentity.TentWoolTile;

public class ModBlocks {
	
	public static Block tentPost;
	public static Block wool;
	public static Block tentBuilder;
	
	public static void init(){
		tentPost = new TentPostBlock();
		wool = new WoolBlock();
		tentBuilder = new TentBuilderBlock();
		
		GameRegistry.registerBlock(wool, BlockInfo.wool_UnlocalizedName);
		GameRegistry.registerBlock(tentPost, BlockInfo.post_UnlocalizedName);
		GameRegistry.registerBlock(tentBuilder, BlockInfo.tentBuilder_UnlocalizedName);
		
		LanguageRegistry.addName(tentBuilder, "Tent Builder");
		
		GameRegistry.registerTileEntity(TentPostTile.class, BlockInfo.post_tile);
		GameRegistry.registerTileEntity(TentPostTileDummy.class, BlockInfo.postDummy_tile);
		GameRegistry.registerTileEntity(TentBuilderTile.class, BlockInfo.tentBuilder_Tile);
		GameRegistry.registerTileEntity(TentWoolTile.class, BlockInfo.wool_Tile);
	}
}
