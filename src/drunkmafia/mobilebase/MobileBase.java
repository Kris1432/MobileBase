package drunkmafia.mobilebase;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.config.ConfigHandler;
import drunkmafia.mobilebase.creativetab.MobileBaseTab;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.recipes.Recipes;
import drunkmafia.mobilebase.tents.ModTents;


@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MobileBase {
	
	@Instance(ModInfo.MODID)
	public MobileBase instance;
		
	public static CreativeTabs tab = new MobileBaseTab(ModInfo.NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		ConfigHandler.init(e.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		ModBlocks.init();
		ModItems.init();
		ModTents.init();
		Recipes.init();
	}
}
