import java.util.ArrayList;

import robocode.Robot;
import robocode.ScannedRobotEvent;


public class EnemyBot {
	
	private static final int lastPositionsToKeep = 4;
	
	private String name;
	private double bearing;
	private double heading;
	private double velocity;
	private double distance;
	private double energy;
	
	private double absoluteBearingDeg;
	private double X;
	private double Y;
	
	private ArrayList<EnemyPosition> positions = new ArrayList<EnemyPosition>();
	
	private Robot ourRobot = null;
	
	public EnemyBot(Robot referenceToOurRobot) {		
		this.ourRobot = referenceToOurRobot;
	}
	
	
	public void update(ScannedRobotEvent e) {
		this.bearing = e.getBearing();
		this.heading = e.getHeading();
		this.velocity = e.getVelocity();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		
		double absBearingDeg = (ourRobot.getHeading() +bearing);
		if(absBearingDeg < 0) absBearingDeg += 360;
		this.absoluteBearingDeg = absBearingDeg;
		
		this.X = (int) Math.round(ourRobot.getX() + Math.sin(Math.toRadians(absoluteBearingDeg)) * this.getDistance());
		this.Y = (int) Math.round(ourRobot.getY() + Math.cos(Math.toRadians(absoluteBearingDeg)) * this.getDistance());
		
		if(positions.size() > lastPositionsToKeep) positions.remove(0);
		positions.add(new EnemyPosition(this.X, this.Y, ourRobot.getTime()));
	}
	
	
	
	public void reset() {
		bearing = 0.0;
		heading = 0.0;
		velocity = 0.0;
		distance = 0.0;
		energy = 0.0;
		absoluteBearingDeg = 0.0;
		X = 0.0;
		Y = 0.0;
	}
	
	
	
	
	//helper
	public boolean knownPositionsExist() {
		if(this.positions.size() > 0) return true;
		return false;
	}
	
	private double getMulti() {
		return 1 +(distance/12);
	}
	
	public double getPredictedX() {
		double predX = X;		
		if(positions.size() > 1) {
			double deltaSum = 0.0;
			for(int i=0; i<positions.size()-1; i++) {
				deltaSum += positions.get(i+1).getX() - positions.get(i).getX();
			}
			double avg = deltaSum/positions.size();
			predX = X+getMulti()*avg;
			if(predX < 0) predX = 0.0;
			if(predX > ourRobot.getBattleFieldWidth()) predX = ourRobot.getBattleFieldWidth();
		}	
		return predX;		
	}
	
	public double getPredictedY() {
		double predY = Y;		
		if(positions.size() > 1) {
			double deltaSum = 0.0;
			for(int i=0; i<positions.size()-1; i++) {
				deltaSum += positions.get(i+1).getY() - positions.get(i).getY();
			}
			double avg = deltaSum/positions.size();
			predY = Y+getMulti()*avg;
			if(predY < 0) predY = 0.0;
			if(predY > ourRobot.getBattleFieldHeight()) predY = ourRobot.getBattleFieldHeight();
		}	
		return predY;		
	}
	
	
	
	//Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBearing() {
		return bearing;
	}
	public void setBearing(double bearing) {
		this.bearing = bearing;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getEnergy() {
		return energy;
	}
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	public double getAbsoluteBearingDeg() {
		return absoluteBearingDeg;
	}
	public void setAbsoluteBearingDeg(double absoluteBearingDeg) {
		this.absoluteBearingDeg = absoluteBearingDeg;
	}
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public ArrayList<EnemyPosition> getPositions() {
		return positions;
	}	
	
}
