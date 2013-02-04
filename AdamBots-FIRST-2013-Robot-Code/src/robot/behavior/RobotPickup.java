/**
 * This class contains methods to control the pickup mechanisms on the robot.
 * @author Steven
 */

package robot.behavior;

import edu.wpi.first.wpilibj.Relay;
import robot.actuators.RobotActuators;

public abstract class RobotPickup {
    
    /**
     * Method to control the disc pickup winch.
     * @param speed The speed to set the winch motor to.
     */
    public static void setWinch(double speed) {
	RobotActuators.discWinch.set(speed);
    }
    
    /**
     * Controls the disk intake roller.
     * @param value The direction to run the intake roller. kForward is in kBackward is out.
     */
    public static void intakeRoller(Relay.Value value) {
	RobotActuators.discIntake.set(value);
    }
}
