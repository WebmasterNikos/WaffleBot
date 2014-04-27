import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Sets.SetView;

import robocode.*;
import robocode.util.Utils;


public class WaffleBot extends AdvancedRobot{

	EnemyBot enemy = new EnemyBot(this);

	private static final int NO_PREDICTION = 0;
	private static final int APPROXIMATE = 1;
	private static final int LINEAR_AIM_AHEAD = 2;

	int aimingMode = APPROXIMATE;
	double bulletPower = 3;


	double radarTurn;

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		enemy.setName(e.getName());
		enemy.update(e);

		//Try to keep radar on enemy
		radarTurn = this.getHeading() + e.getBearing() - this.getRadarHeading();
		radarTurn = Utils.normalRelativeAngleDegrees(radarTurn);
		setTurnRadarRight(4 * radarTurn);
		
		//90 degrees to enemy
		setTurnRight(e.getBearing() +90);

		//aim at target and fire
		if(aimingMode == NO_PREDICTION) {
			setTurnGunRight(Utils.normalRelativeAngleDegrees(this.getHeading() - this.getGunHeading() +e.getBearing()));

		} else if(aimingMode == APPROXIMATE) {
			aimAt(enemy.getPredictedX(), enemy.getPredictedY());
		} else if(aimingMode == LINEAR_AIM_AHEAD) {
			double headOnBearing = getHeadingRadians() + e.getBearingRadians();
			double linearBearing = headOnBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bulletPower) * Math.sin(e.getHeadingRadians() - headOnBearing));
			setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - getGunHeadingRadians()));
		}
		setFire(bulletPower);
	}


	@Override
	public void run() {

		//Set colors
		setBodyColor(Color.BLACK);
		setGunColor(Color.RED);
		setRadarColor(Color.RED);
		setScanColor(Color.RED);
		setBulletColor(Color.RED);

		//Move radar and gun independently
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);	


		//Turn the radar around as fast as possible
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		
		Random rand = new Random();

		while(true) {


			//setAhead(20);
			//setTurnRight(45);
			//setAhead(200*(Math.sin(getTime())));
			setAhead(-100 +rand.nextInt(200));
			execute(); //do the above at the same time
			scan(); //ask for onScannedRobot

			//If somehow the radar stops, reset it
			if(getRadarTurnRemaining() == 0) {
				setTurnRadarLeft(Double.POSITIVE_INFINITY);
			}


		}
	}


	//For graphical debugging
	@Override
	public void onPaint(Graphics2D g) {
		
		//visualize our heading
		g.setColor(Color.YELLOW);
		int headingX1 = (int) Math.round(getX() + Math.sin(getHeadingRadians())*1200);
		int headingX2 = (int) Math.round(getX() + Math.sin(getHeadingRadians())*(-1200));
		int headingY1 = (int) Math.round(getY() + Math.cos(getHeadingRadians())*1200);
		int headingY2 = (int) Math.round(getY() + Math.cos(getHeadingRadians())*(-1200));
		g.drawLine(headingX1, headingY1, headingX2, headingY2);
		
		
		//visualize where we're aiming
		g.setColor(Color.GREEN);
		int targetX = (int) Math.round(getX() + Math.sin(getGunHeadingRadians())*1200);
		int targetY = (int) Math.round(getY() + Math.cos(getGunHeadingRadians())*1200);
		g.drawLine((int)getX(), (int)getY(), targetX, targetY);
		
		
		g.setColor(Color.RED);
		if(enemy.getName() != null && !enemy.getName().equals("") && enemy.getPositions() != null) {			
			if(enemy.knownPositionsExist()) {
				
				for(EnemyPosition p : enemy.getPositions()) {
					g.drawRect(p.getX()-15, p.getY()-15, 30, 30);
				}

				if(aimingMode == APPROXIMATE) {
					if(enemy.getPositions().size() >= 2) {
						g.setColor(Color.GREEN);
						g.drawRect((int)enemy.getPredictedX()-15, (int)enemy.getPredictedY()-15, 30, 30);
					}	
				}

			}
		}



	}

	private ArrayList<EnemyPosition> getLastPositions(EnemyBot enemy, int numPositions) {
		ArrayList<EnemyPosition> lastPositions = new ArrayList<EnemyPosition>();

		if(enemy.getPositions().size() <= numPositions) return enemy.getPositions();

		//only the last numPositions
		ArrayList<EnemyPosition> enemyPositions = enemy.getPositions();
		for(int i=0; i<numPositions; i++) {
			lastPositions.add(enemyPositions.get(enemyPositions.size()-i));
		}

		return lastPositions;
	}

	private void aimAt(double x, double y) {
		double dX = x - this.getX();
		double dY = y - this.getY();
		double bulletHeadingDeg = Math.toDegrees(Math.atan2(dX, dY));
		double turnAngle = Utils.normalRelativeAngleDegrees(bulletHeadingDeg - this.getGunHeading());
		setTurnGunRight(turnAngle);		
	}


}
