package drunkmafia.mobilebase.common;

import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import drunkmafia.mobilebase.lib.ModInfo;


public class EventHookContainer {
	private static boolean firstRun = true;
	
	
	//Changed from Forge to Forge Modloader events, couldn't find the class for Forge Events
	@SubscribeEvent
	public void EntityJoinWorldEvent(PlayerLoggedInEvent event){
		System.out.println("Test");
		if(!event.player.getEntityWorld().isRemote){
			if(firstRun && UpdateChecker.updateInfo != null && !UpdateChecker.updateInfo.isEmpty()){
				for(int i = 0; i < UpdateChecker.updateInfo.size(); i++){
					event.player.func_146105_b(new ChatComponentText(UpdateChecker.updateInfo.get(i)));
				}
			}
			event.player.func_146105_b(new ChatComponentText(" [Mobile Base] You are using pre-release version " + ModInfo.VERSION + " of " + ModInfo.NAME + ", know that there will be bugs that can potentially corrupt your world and use with caution."));
			firstRun = false;
		}
	}
}
