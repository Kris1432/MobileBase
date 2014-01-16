package drunkmafia.mobilebase.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ItemInfo;

public class Recipes {
	public static void init(){
		for(int i = 0; i < ItemInfo.canvas_LocalizedName.length; i++){
			ItemStack result = new ItemStack(ModItems.canvas, 1, i);
			result.func_151001_c(ItemInfo.canvas_LocalizedName[i]);
			GameRegistry.addRecipe(result,
					"ccc",
					"ccc",
					"ccc",
					'c', new ItemStack(Blocks.wool, 1, i));
		}
		
		for(int i = 0; i < ItemInfo.tent_LocalizedName.length; i++){
			ItemStack result = new ItemStack(ModItems.smallTent, 1, i);
			result.func_151001_c(ItemInfo.tent_LocalizedName[i]);
			GameRegistry.addRecipe(result,
					"wcw",
					"cec",
					"wcw",
					'c', new ItemStack(ModItems.canvas, 1, i), 'w', new ItemStack(Items.stick), 'e', new ItemStack(Items.ender_pearl));
		}
	}
}
