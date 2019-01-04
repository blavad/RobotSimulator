package action;

import core.Robot;
import tools.Outils;

/** Classe executable representant l'action de pivoter
 * 
 * @author DHT
 *
 */
public class Pivoter implements Executable {

	private Robot robot;
	private double angle;
	
	public Pivoter(Robot robot, double angle) {
		this.robot = robot;
		this.angle = angle;
	}
	
	public Pivoter(Robot robot) {
		this.robot = robot;
		this.angle = 1;
	}
	
	@Override
	public void execute(double dt) {
		double newAngle = robot.getAngle() + angle*dt;
		if (newAngle < 0) {
			robot.setAngle(newAngle + Outils.DeuxPif);
		}
		else if (newAngle > Outils.DeuxPif) {
			robot.setAngle(newAngle - Outils.DeuxPif);
		}
		else robot.setAngle(newAngle);

	}

}
