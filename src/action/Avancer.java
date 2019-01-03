package action;

import core.Robot;
import tools.Debug;

/** Classe executable representant l'action d'avancer
 * 
 * @author DHT
 *
 */
public class Avancer implements Executable {
	private Robot robot;
	private double distance;
	
	public Avancer(Robot robot, double distance) {
		this.robot = robot;
		this.distance = distance;
	}
	

	public Avancer(Robot robot) {
		this.robot = robot;
		this.distance = 1f;
	}

	@Override
	public void execute(double dt) {
		double dX = Math.cos(robot.getAngle()) * robot.getRayon() * distance;
		double dY = Math.sin(robot.getAngle()) * robot.getRayon() * distance;
		robot.setPosition(robot.getPos().x + dt*dX, robot.getPos().y + dt*dY);
	}
	
}
