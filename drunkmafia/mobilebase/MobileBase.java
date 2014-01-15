package drunkmafia.mobilebase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.client.gui.GuiHandler;
import drunkmafia.mobilebase.common.EventHookContainer;
import drunkmafia.mobilebase.common.MobileBaseTab;
import drunkmafia.mobilebase.common.UpdateChecker;
import drunkmafia.mobilebase.config.ConfigHandler;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.recipes.Recipes;


@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MobileBase {
	
	@Instance(ModInfo.MODID)
	public static MobileBase instance;
		
	public static CreativeTabs tab = new MobileBaseTab(ModInfo.NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(new EventHookContainer());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		ConfigHandler.init(e.getSuggestedConfigurationFile());
		UpdateChecker.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		ModBlocks.init();
		ModItems.init();
		Recipes.init();
	}
}
