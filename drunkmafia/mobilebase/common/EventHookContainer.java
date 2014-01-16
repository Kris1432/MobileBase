package drunkmafia.mobilebase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import drunkmafia.mobilebase.lib.ModInfo;


public class EventHookContainer {
	private static boolean firstRun = true;
	
	
	@SubscribeEvent
	public void EntityJoinWorldEvent(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityPlayer){
		EntityPlayer player = (EntityPlayer)event.entity;
			if(firstRun && !player.getEntityWorld().isRemote){
				if(UpdateChecker.updateInfo != null && !UpdateChecker.updateInfo.isEmpty()){
					for(int i = 0; i < UpdateChecker.updateInfo.size(); i++){
						player.func_146105_b(new ChatComponentText(UpdateChecker.updateInfo.get(i)));
					}
				}
				player.func_146105_b(new ChatComponentText(" [Mobile Base] You are using pre-release version " + ModInfo.VERSION + " of " + ModInfo.NAME + ", know that there will be bugs that can potentially corrupt your world and use with caution."));
				firstRun = false;
			}
		}
	}
}
