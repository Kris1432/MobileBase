package drunkmafia.mobilebase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHookContainer {
	
	private static boolean firstRun = true;
	
	
	public void EntityJoinWorldEvent(EntityJoinWorldEvent event){
		if(firstRun && UpdateChecker.updateInfo != null && !UpdateChecker.updateInfo.isEmpty() && event.entity instanceof EntityPlayer && event.world.isRemote){
			EntityPlayer player = (EntityPlayer)event.entity;
			for(int i = 0; i < UpdateChecker.updateInfo.size(); i++){
				//can't find this?
				//player.sendChatToPlayer(ChatMessageComponent.createFromText(UpdateChecker.updateInfo.get(i)));
			}
			firstRun = false;
		}
	}
}
