package drunkmafia.mobilebase.tents;

public class ModTents {
	
	public static Tent smallTent;
	
	public static void init(){
		smallTent = new TentSmall();
	}
}
