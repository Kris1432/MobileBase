package drunkmafia.mobilebase.util;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

public class RotationHelper {
	
	private static final int[] yToFlookup = { 3, 4, 2, 5 };
	
	public static ForgeDirection yawToForge(float yaw) {
        ForgeDirection result = ForgeDirection.getOrientation(yToFlookup[MathHelper.floor_double(yaw * 4.0F / 360.0F + 0.5D) & 3]);
        return result;
    }
}
