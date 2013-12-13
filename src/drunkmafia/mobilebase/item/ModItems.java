package drunkmafia.mobilebase.item;

import net.minecraft.item.Item;

public class ModItems {
	
	public static Item tent;
	
	public static void init(){
		tent = new ItemTent();
	}
}
