package drunkmafia.mobilebase.util;

public class Vector3 {
	
	private int x, y, z;
	
	public Vector3(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setPos(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setPos(int[] coords){
		if(!(coords.length > 3)){
			x = coords[0];
			y = coords[1];
			z = coords[2];
		}
	}
	
	public static Vector3 getVector(int[] coords){
		Vector3 vec = new Vector3(coords[0], coords[1], coords[2]);
		return vec;
	}
}
