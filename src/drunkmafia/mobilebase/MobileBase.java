package drunkmafia.mobilebase;

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
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.network.PacketHandler;
import drunkmafia.mobilebase.proxies.CommonProxy;


@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {ModInfo.CHANNEL}, packetHandler = PacketHandler.class)
public class MobileBase {
	
	@Instance(ModInfo.MODID)
	public MobileBase instance;
	
	@SidedProxy(clientSide = ModInfo.PROXY_CLIENT, serverSide = ModInfo.PROXY_SERVER)
    public static CommonProxy      proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		ConfigHandler.init(e.getSuggestedConfigurationFile());
	}
	
	public void init(FMLInitializationEvent e){
		ModBlocks.init();
		ModItems.init();
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}
}
