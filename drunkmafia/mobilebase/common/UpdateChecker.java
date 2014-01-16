package drunkmafia.mobilebase.common;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import drunkmafia.mobilebase.lib.ModInfo;

public class UpdateChecker {
	
	private static ArrayList<String> fileContents = new ArrayList<String>();
	public static ArrayList<String> updateInfo = new ArrayList<String>();	
	
	@SuppressWarnings("resource")
	public static void init(){
		try{
			URL url = new URL("https://dl.dropboxusercontent.com/u/101919880/MobileBase/VersionInfo.txt");
			Scanner scanner = new Scanner(url.openStream());
			while(scanner.hasNext())
				fileContents.add(scanner.nextLine());
		}catch(IOException e){
			FMLLog.log(Level.ALL, "[" + ModInfo.MODID + "] Failed to load VersionInfo, please check your internet connection and/or tell the mod developer", e);
			e.printStackTrace();
		}
		
		updateInfo.add("§6<" + ModInfo.NAME + " Update>");
		
		int index = fileContents.indexOf("<" + ModInfo.VERSION + ">");
		if(index != -1){
			for(int i = index + 1; i < fileContents.size(); i++){
				if(!fileContents.get(i).equals("<End>") && !fileContents.get(i).equals("NULL"))
					updateInfo.add(fileContents.get(i));
				else if(fileContents.get(i).equals("<End>"))
					break;
				else if(fileContents.get(i).equals("NULL")){
					updateInfo = null;
					break;
				}
					
			}
		}else
			FMLLog.log(Level.ALL, "[" + ModInfo.MODID + "] Version Number not found, please inform the Mod Dev");
		
	}
}
