/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.behavior;

import edu.wpi.first.wpilibj.*;
import robot.actuators.RobotActuators;


/**
 *
 * @author Ben
 */
public class RobotDrive {
    
    // there will be a class TBD that will store all the robot
    // sensors, motors
    // take out the next few lines when static class is made
    public final int PORT_LEFT_VICTOR = 1;
    public final int PORT_RIGHT_VICTOR = 2;
    /** Shifter value*/
    public static final double SHIFTER_LOW = 1;
    public static final double SHIFTER_HIGH = 0.3;
    public static final double SHIFTER_NEUTRAL = 0.8;
    
    //private robot.actuators.RobotActuators robotActuators;
    
    
    public RobotDrive( )
    {
	robotDriveInit();
    }
    
    /**
     * Initialize everything
     */
    private void robotDriveInit() {
    }
    
    /**
     * 
     * @param leftSpeed to set the left speed
     * @param rightSpeed to set the right speed
     * sets the speed of the wheels to the parameters given
     */
    public void drive( double leftSpeed, double rightSpeed ) {
	RobotActuators.driveRight.set(rightSpeed);
	RobotActuators.driveLeft.set(leftSpeed);
    }
    
    /**
     * 
     * @param speed for the speed of both wheels
     * calls drive and sends it the parameters of speed for both
     */
    public void driveStraight( double speed )
    {
	RobotActuators.driveRight.set(speed);
	RobotActuators.driveLeft.set(speed);
    }
    
    /**
     * 
     * @param speed to turn in place
     * turns in place at the speed given
     */
    public void turn( double speed )
    {
	RobotActuators.driveRight.set(-speed);
	RobotActuators.driveLeft.set(speed);
    }
    
    /**
     * 
     * stops all the motors
     */
    public void stop()
    {
	RobotActuators.driveRight.set(0);
	RobotActuators.driveLeft.set(0);
    }
    
    /**
     * switches gear when called
     * @param ServoPosition used to set transmissionLeft position
     */
    public void switchGear( double ServoPosition){
//        RobotActuators.transmissionLeft.set(ServoPosition);
//        System.out.println(RobotActuators.transmissionLeft.get());

        RobotActuators.transmissionLeft.set(ServoPosition);
	RobotActuators.transmissionRight.set(ServoPosition);
    }
}