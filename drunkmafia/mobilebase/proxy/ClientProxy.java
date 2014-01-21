package drunkmafia.mobilebase.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import drunkmafia.mobilebase.client.renderer.item.ItemTentRenderer;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ItemInfo;

public class ClientProxy extends CommonProxy{
	@Override
	public void initRendering(){
		MinecraftForgeClient.registerItemRenderer(ModItems.tent.itemID, new ItemTentRenderer());
	}
}
