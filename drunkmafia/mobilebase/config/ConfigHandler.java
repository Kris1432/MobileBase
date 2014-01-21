package drunkmafia.mobilebase.config;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;

public class ConfigHandler {
	public static void init(File file){
		Configuration config = new Configuration(file);
		try{
			
			ItemInfo.tent_ID = config.getItem(ItemInfo.tent_UnlocalizedName, ItemInfo.tent_Default_ID).getInt() - 256;
			ItemInfo.bluePrint_ID = config.getItem(ItemInfo.bluePrint_UnlocalizedName, ItemInfo.bluePrint_Default_ID).getInt() - 256;
			
			BlockInfo.post_ID = config.getBlock(BlockInfo.post_UnlocalizedName, BlockInfo.post_Default_ID).getInt();
			BlockInfo.wool_ID = config.getBlock(BlockInfo.wool_UnlocalizedName, BlockInfo.wool_Default_ID).getInt();
			BlockInfo.tentBuilder_ID = config.getBlock(BlockInfo.tentBuilder_UnlocalizedName, BlockInfo.tentBuilder_Default_ID).getInt();
			
			int[] empty = {0};
			
			Property blackListBlocks = config.get("Blacklisted", "blocks", empty);
			Property blackListErrors = config.get("Blacklisted", "errors", empty);
			
			int[] temp1 = blackListBlocks.getIntList();
			int[] temp2 = blackListBlocks.getIntList();
			
			for(int t : temp1)
				ModInfo.blackListedBlocks.add(t);
			
			for(int t : temp2)
				ModInfo.errorBlackListedBlocks.add(t);
			
		}catch(Exception e){
			FMLLog.log(Level.SEVERE, "[" + ModInfo.NAME + "] Config Error, please report this to the mod author", e);
			e.printStackTrace();
		}finally{
			config.save();
		}
	}
}
