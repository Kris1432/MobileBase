package drunkmafia.mobilebase.util;

public class Vector2 {
	private int x, y;
	
	public Vector2(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	
	public void setPos(int x, int y, int z){
		this.x = x;
		this.y = y;
	}
	
	public void setPos(int[] coords){
		if(!(coords.length > 2)){
			x = coords[0];
			y = coords[1];
		}
	}
}
