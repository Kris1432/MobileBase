package drunkmafia.mobilebase.item;

import net.minecraft.item.Item;

public class ModItems {
	
	public static Item smallTent;
	public static Item canvas;
	public static Item bluePrint;
	
	public static void init(){
		canvas = new ItemCanvas();
		smallTent = new SmallTent();
		bluePrint = new ItemBlueprint();
	}
}
