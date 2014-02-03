package drunkmafia.mobilebase.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ItemInfo;

public class Recipes {
	public static void init(){
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.tentBuilder, 1), new ItemStack(ModItems.bluePrint), new ItemStack(Block.workbench));
		
		ItemStack stack = new ItemStack(ModItems.bluePrint, 1);
		stack.setItemName("Blank Blueprint");
		GameRegistry.addShapelessRecipe(stack, new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.paper));
	}
}
