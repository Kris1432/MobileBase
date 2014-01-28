package drunkmafia.mobilebase.item;

import java.util.List;

import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.ItemInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemSmallTentPrint extends ItemBlueprint{

	private static int[][][] structure = {
        {
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1}
        },
        {
                {1,1,1,1,1,1,1,1,1},
                {1,5,5,5,5,5,5,5,1},
                {1,5,5,5,5,5,5,5,1},
                {1,5,5,5,5,5,5,5,1},
                {1,5,5,5,-1,5,5,5,1},
                {1,5,5,5,5,5,5,5,1},
                {1,5,5,5,5,5,5,5,1},
                {1,5,5,5,3,5,5,5,1},
                {1,1,1,1,5,1,1,1,1}
        },
        {
                {0,1,1,1,1,1,1,1,0},
                {0,1,5,5,5,5,5,1,0},
                {0,1,5,5,5,5,5,1,0},
                {0,1,5,5,5,5,5,1,0},
                {0,1,5,5,2,5,5,1,0},
                {0,1,5,5,5,5,5,1,0},
                {0,1,5,5,5,5,5,1,0},
                {0,1,5,5,3,5,5,1,0},
                {0,1,1,1,5,1,1,1,0}
        },
        {
                {0,0,1,1,1,1,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,5,2,5,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,5,5,5,1,0,0},
                {0,0,1,1,1,1,1,0,0}
        },
        {
                {0,0,0,1,1,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,2,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,5,1,0,0,0},
                {0,0,0,1,1,1,0,0,0}
        },
        {
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0,0}
        }
};
	
	public ItemSmallTentPrint() {
		super(ItemInfo.smallTentPrint_ID);
		setUnlocalizedName("smallTentPrint");
		setCreativeTab(MobileBase.tab);
	}
	
	@Override
    public void getSubItems(int var, CreativeTabs creativeTabs, List list) {
    	ItemStack stack = new ItemStack(this, 1, 0);
    	NBTTagCompound tag = new NBTTagCompound();
    	
    	tag.setInteger("tentY", structure.length);
		tag.setInteger("tentX", structure[0].length);
		tag.setInteger("tentZ", structure[0][0].length);
		for(int y = 0; y < structure.length; y++)
			for(int x = 0; x < structure[0].length; x++)
				tag.setIntArray("tentStructure:" + y + x, structure[y][x]);
    	
        tag.setString("tentName", "Small Tent");
        stack.setTagCompound(tag);
        list.add(stack);
    }
}
