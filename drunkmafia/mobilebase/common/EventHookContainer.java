package drunkmafia.mobilebase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import drunkmafia.mobilebase.ModInfo;

public class EventHookContainer {
	
	private static boolean firstRun = true;
	
	@ForgeSubscribe
	public void EntityJoinWorldEvent(EntityJoinWorldEvent event){
		if(firstRun && !ModInfo.update_Info.isEmpty() && event.entity instanceof EntityPlayer && event.world.isRemote){
			EntityPlayer player = (EntityPlayer)event.entity;
			player.sendChatToPlayer(ChatMessageComponent.createFromText(ModInfo.update_Info));
			firstRun = false;
		}
	}
}
