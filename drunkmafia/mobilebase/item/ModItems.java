package drunkmafia.mobilebase.item;

import cpw.mods.fml.common.registry.GameRegistry;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;
import net.minecraft.item.Item;

public class ModItems {
	
	public static Item smallTent;
	public static Item canvas;
	public static Item bluePrint;
	
	public static void init(){
		canvas = new ItemCanvas();
		smallTent = new SmallTent();
		bluePrint = new ItemBlueprint();
		
		GameRegistry.registerItem(canvas, ItemInfo.canvas_UnlocalizedName, ModInfo.MODID);
		GameRegistry.registerItem(smallTent, ItemInfo.tent_UnlocalizedName, ModInfo.MODID);
		GameRegistry.registerItem(bluePrint, ItemInfo.bluePrint_UnlocalizedName, ModInfo.MODID);
		
	}
}
