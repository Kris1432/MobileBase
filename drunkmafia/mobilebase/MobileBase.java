package drunkmafia.mobilebase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
public class MobileBase {
	
	@Instance(ModInfo.MODID)
	public static MobileBase instance;
		
	public static CreativeTabs tab = new MobileBaseTab(ModInfo.NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		FMLCommonHandler.instance().bus().register(new EventHookContainer());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		UpdateChecker.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		ModBlocks.init();
		ModItems.init();
		Recipes.init();
	}
}
