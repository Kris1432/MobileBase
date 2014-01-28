package drunkmafia.mobilebase.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import drunkmafia.mobilebase.lib.ItemInfo;
import net.minecraft.item.Item;

public class ModItems {
	
	public static Item tent;
	public static Item bluePrint;
	public static Item smallTentPrint;
	
	public static void init(){
		tent = new ItemTent();
		bluePrint = new ItemBlueprint(ItemInfo.bluePrint_ID);
		smallTentPrint = new ItemSmallTentPrint();
		
		LanguageRegistry.addName(smallTentPrint, "Small Tent Blueprint");
	}
}
