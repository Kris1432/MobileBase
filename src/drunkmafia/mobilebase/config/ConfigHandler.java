package drunkmafia.mobilebase.config;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import drunkmafia.mobilebase.ModInfo;
import drunkmafia.mobilebase.block.BlockInfo;
import drunkmafia.mobilebase.item.ItemInfo;

public class ConfigHandler {
	public static void init(File file){
		Configuration config = new Configuration(file);
		try{
			
			ItemInfo.tent_ID = config.getItem(ItemInfo.tent_UnlocalizedName, ItemInfo.tent_Default_ID).getInt() - 256;
			
			BlockInfo.post_ID = config.getBlock(BlockInfo.post_UnlocalizedName, BlockInfo.post_Default_ID).getInt();
			
		}catch(Exception e){
			FMLLog.log(Level.SEVERE, "[" + ModInfo.NAME + "] Config Error, please report this to the mod author", e);
			e.printStackTrace();
		}finally{
			config.save();
		}
	}
}
