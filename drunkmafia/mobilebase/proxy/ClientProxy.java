package drunkmafia.mobilebase.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import drunkmafia.mobilebase.client.renderer.item.ItemTentRenderer;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class ClientProxy extends CommonProxy{
	@Override
	public void initRendering(){		
		MinecraftForgeClient.registerItemRenderer(ModItems.tent.itemID, new ItemTentRenderer());
	}
}
