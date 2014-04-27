
public class EnemyPosition implements Comparable<EnemyPosition>{
	
	private double X;
	private double Y;
	private long time;
	
	public EnemyPosition(double X, double Y, long time) {
		this.X = X;
		this.Y = Y;
		this.time = time;
	}
	
	public int getX() {
		return (int) Math.round(this.X);
	}
	
	public int getY() {
		return (int) Math.round(this.Y);
	}
	
	public long getTime(){
		return time;
	}

	@Override
	public int compareTo(EnemyPosition other) {
		if(this.time > other.getTime()) return 1;
		else if(this.time < other.getTime()) return -1;
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if(this.X == ((EnemyPosition) other).getX() && this.Y == ((EnemyPosition) other).getY() && this.time == ((EnemyPosition) other).getTime()) return true;
		return false;
	}
	
	
	
	

}
