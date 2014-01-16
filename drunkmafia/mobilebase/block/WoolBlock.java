package drunkmafia.mobilebase.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.BlockInfo;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class WoolBlock extends Block{
	
	public WoolBlock() {
		super(Material.field_151580_n);
		func_149663_c(BlockInfo.wool_UnlocalizedName); //Unlocalized name
		func_149672_a(Block.field_149775_l); //Sound Type
		func_149711_c(0.2F); //Hardness
	}
	
    @SideOnly(Side.CLIENT)
	IIcon[] icons;
    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister p_149651_1_)
    {
        this.icons = new IIcon[16];

        for (int i = 0; i < this.icons.length; ++i)
        {
            this.icons[i] = p_149651_1_.registerIcon(ModInfo.MODID + ":canvas\\" + ItemInfo.canvas_LocalizedName[i].toLowerCase());
        }
    }
	
	@Override
	public Item func_149650_a(int var, Random rand, int var2) {
		return null;
	}
	
	//replaces idPicked
	@Override
	public Item func_149694_d(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.func_150898_a(Blocks.wool);
	}
		
}
