package drunkmafia.mobilebase.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ItemInfo;

public class Recipes {
	public static void init(){
		for(int i = 0; i < ItemInfo.canvas_LocalizedName.length; i++){
			ItemStack result = new ItemStack(ModItems.canvas, 1, i);
			result.setItemName(ItemInfo.canvas_LocalizedName[i]);
			GameRegistry.addRecipe(result,
					"ccc",
					"ccc",
					"ccc",
					'c', new ItemStack(Block.cloth, 1, i));
		}
		
		for(int i = 0; i < ItemInfo.tent_LocalizedName.length; i++){
			ItemStack result = new ItemStack(ModItems.smallTent, 1, i);
			result.setItemName(ItemInfo.tent_LocalizedName[i]);
			GameRegistry.addRecipe(result,
					"wcw",
					"cec",
					"wcw",
					'c', new ItemStack(ModItems.canvas, 1, i), 'w', new ItemStack(Item.stick), 'e', new ItemStack(Item.enderPearl));
		}
	}
}
