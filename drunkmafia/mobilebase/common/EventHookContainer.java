package drunkmafia.mobilebase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import drunkmafia.mobilebase.lib.ModInfo;

public class EventHookContainer {
	
	private static boolean firstRun = true;
	
	@ForgeSubscribe
	public void EntityJoinWorldEvent(EntityJoinWorldEvent event){
		if(firstRun && UpdateChecker.updateInfo != null && !UpdateChecker.updateInfo.isEmpty() && event.entity instanceof EntityPlayer && event.world.isRemote){
			EntityPlayer player = (EntityPlayer)event.entity;
			for(int i = 0; i < UpdateChecker.updateInfo.size(); i++)
				player.sendChatToPlayer(ChatMessageComponent.createFromText(UpdateChecker.updateInfo.get(i)));
			firstRun = false;
		}
	}
}
