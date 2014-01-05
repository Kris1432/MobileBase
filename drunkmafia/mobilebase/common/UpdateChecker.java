package drunkmafia.mobilebase.common;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;

import cpw.mods.fml.common.FMLLog;
import drunkmafia.mobilebase.ModInfo;

public class UpdateChecker {
	@SuppressWarnings("resource")
	public static void init(){
		try {
			   URL url = new URL("https://dl.dropboxusercontent.com/u/101919880/MobileBase/Update%20File.txt");
			   Scanner s = new Scanner(url.openStream());
			   
			}
			catch(IOException ex) {
				FMLLog.log(Level.INFO, "[" + ModInfo.MODID + "] Failed to load txt file to check for updates.", ex);
			    ex.printStackTrace();
			}
	}
}
