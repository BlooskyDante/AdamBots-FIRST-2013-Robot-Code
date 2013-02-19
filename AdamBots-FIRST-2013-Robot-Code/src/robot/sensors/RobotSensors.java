/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.sensors;

import edu.wpi.first.wpilibj.*;
import robot.RobotMain;

/**
 * <p>Contains static instances of every sensor on the robot. Classes that
 * require input from sensors should access them through one of the instances
 * here; some sensors have been wrapped with a "Fancy" class, simplifying their
 * use elsewhere.</p> <p><h3>Additional Notes</h3> <ul> <li>The Axis Camera is
 * handled within RobotCamera.</li> <li>Pairs of Limit Switches are labeled with
 * either A or B, corresponding to "ABOVE" and "BELOW," respectively.</li> </ul>
 * </p> <p>Note that the Axis Camera is handled within RobotCamera.</p>
 *
 * @author Ben
 */
public class RobotSensors {
    //// CONSTANTS -------------------------------------------------------------
	
	// TODO:  Constants
	
	// Encoders (Distance Per Pulse)
	public static final double DPP_ENCODER_DRIVE_LEFT_INCHES = 1.0;		// Inches
	public static final double DPP_ENCODER_DRIVE_RIGHT_INCHES = 1.0;	// Inches
	public static final double DPP_ENCODER_WINCH = 1.0;					// ?
	public static final double DPP_ENCODER_ELEVATOR = 1.0;			// Degrees
	
	// Encoders (Degrees Per Inch)
	public static final double DPI_ENCODER_DRIVE_LEFT_DEGREES = 1.0;	// Degrees Per Inch
	public static final double DPI_ENCODER_DRIVE_RIGHT_DEGREES = 1.0;	// Degrees Per Inch
	
	// FancyCounters (Ticks Per Period)
	public static final int TPP_COUNTER_SHOOTER_ANGLE = 1;					// ?
	public static final int TPP_COUNTER_SHOOTER_SPEED = 1;				// ?
	
	// Gyro
	public static final double GYRO_VPDPS = 1.0;  // Volts per Degree Per Second
	
	//// PORT CONSTANTS --------------------------------------------------------
	
	// Card Constants (by card, not by slot)
    public static final int ANA1 = 1;
    public static final int DIO1 = 1;
    public static final int DIO2 = 2;
    public static final int SOL1 = 1;
	
	/**
	 * Port constants for the competition bot.
	 */
	public static final class CompetitionBot {
		/** Analog Card 1 Port Constants. */
		public static final class Analog {
			public static final int GYRO		= 1;
			public static final int CONFIG_A	= 2;
			public static final int CONFIG_B	= 3;
			public static final int CONFIG_C	= 4;
		}
		
		/** Digital Card 1 Port Constants. */
		public static final class DigitalIn1 {
			public static final int LEFT_DRIVE_ENCODER_A = 1;
			public static final int LEFT_DRIVE_ENCODER_B = 2;
			public static final int RIGHT_DRIVE_ENCODER_A = 3;
			public static final int RIGHT_DRIVE_ENCODER_B = 4;
			public static final int WINCH_ENCODER_A = 5;
			public static final int WINCH_ENCODER_B = 6;
			public static final int WINCH_LIMIT_A = 7;
			public static final int WINCH_LIMIT_B = 8;
			public static final int ARM_LIMIT_A = 9;
			public static final int ARM_LIMIT_B = 10;
			public static final int SHOOTER_ANGLE_ENCODER = 11;
			public static final int ELEVATOR_LIMIT_A = 12;
			public static final int ELEVATOR_LIMIT_B = 13;
			public static final int SHOOTER_SPEED_ENCODER = 14;
		}
		
		/** Digital Card 2 Port Constants. */
		public static final class DigitalIn2 {
			public static final int ELEVATOR_ENCODER_A = 1;
			public static final int ELEVATOR_ENCODER_B = 2;
			public static final int HOOK_LEFT_BASE_LIMIT = 5;
			public static final int HOOK_RIGHT_BASE_LIMIT = 6;
			public static final int DISC_ORIENTATION_LIMIT_A = 7;
			public static final int DISC_ORIENTATION_LIMIT_B = 8;
			public static final int SHOOTER_LIMIT_A = 9;
			public static final int SHOOTER_LIMIT_B = 10;
			public static final int PRESSURE_SWITCH = 11;
		}
		
		/** Digital Card 1 Serial Port Constants. */
		public static final class DigitalSerial1 {
			public static final int ACCELEROMETER = 1;
		}
	}
	
	//// SENSOR INSTANCES ------------------------------------------------------

	// Drive
    public static Encoder encoderDriveLeft;
    public static Encoder encoderDriveRight;
	
	// Winch
    public static Encoder encoderWinch;
    public static DigitalInput limitWinchA;
    public static DigitalInput limitWinchB;
    public static DigitalInput limitArmA;
    public static DigitalInput limitArmB;
	
	// Elevator
    public static Encoder encoderElevator;
    public static DigitalInput limitElevatorA;
    public static DigitalInput limitElevatorB;
	
	// Climb Hooks
    public static DigitalInput limitHookLeftBase;
    public static DigitalInput limitHookRightBase;
	
	// Chassis
    public static Gyro gyroChassis;
    public static Accelerometer accelerometerChassis; // ?? ?? Is this the right class?
    public static AnalogChannel configA;
    public static AnalogChannel configB;
    public static AnalogChannel configC;
	
	// Shooter
    public static FancyCounter counterShooterSpeed;
    public static FancyCounter counterShooterAngle;
    public static DigitalInput limitShooterA;
    public static DigitalInput limitShooterB;
	
	// Disc Pickup
    public static DigitalInput limitDiscTop;
    public static DigitalInput limitDiscBottom;
	
	// Compressor
	public static DigitalInput pressureSwitch;

	//// INITIALIZATION --------------------------------------------------------
	
    /**
     * Instantiates all sensors handled by class.
     */
    public static void init() {
		initCompetition();
		
		// Configure
		configure();
    }
	
	private static void initCompetition(){
		//// ANALOG CARD -------------------------------------------------------

        gyroChassis = new Gyro(ANA1, CompetitionBot.Analog.GYRO); //?
        configA = new AnalogChannel(ANA1, CompetitionBot.Analog.CONFIG_A);
        configB = new AnalogChannel(ANA1, CompetitionBot.Analog.CONFIG_B);
        configC = new AnalogChannel(ANA1, CompetitionBot.Analog.CONFIG_C);

        //// DIGITAL CARD 1 ----------------------------------------------------

        encoderDriveLeft = new Encoder(DIO1, CompetitionBot.DigitalIn1.LEFT_DRIVE_ENCODER_A,
									   DIO1, CompetitionBot.DigitalIn1.LEFT_DRIVE_ENCODER_B);
        encoderDriveRight = new Encoder(DIO1, CompetitionBot.DigitalIn1.RIGHT_DRIVE_ENCODER_A,
										DIO1, CompetitionBot.DigitalIn1.RIGHT_DRIVE_ENCODER_B);

        encoderWinch = new Encoder(DIO1, CompetitionBot.DigitalIn1.WINCH_ENCODER_A, 
								   DIO1, CompetitionBot.DigitalIn1.WINCH_ENCODER_B);

        limitWinchA = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.WINCH_LIMIT_A);
        limitWinchB = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.WINCH_LIMIT_B);

        limitArmA = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.ARM_LIMIT_A);
        limitArmB = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.ARM_LIMIT_B);

        limitElevatorA = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.ELEVATOR_LIMIT_A);
        limitElevatorB = new DigitalInput(DIO1, CompetitionBot.DigitalIn1.ELEVATOR_LIMIT_B);

		counterShooterSpeed = new FancyCounter(DIO1, CompetitionBot.DigitalIn1.SHOOTER_SPEED_ENCODER, TPP_COUNTER_SHOOTER_SPEED);
		counterShooterAngle = new FancyCounter(DIO1, CompetitionBot.DigitalIn1.SHOOTER_ANGLE_ENCODER, TPP_COUNTER_SHOOTER_ANGLE);
		
        //// DIGITAL CARD 2 ----------------------------------------------------
        
        encoderElevator = new Encoder(DIO2, CompetitionBot.DigitalIn2.ELEVATOR_ENCODER_A, DIO2, CompetitionBot.DigitalIn2.ELEVATOR_ENCODER_B);
		encoderElevator.start();
		
        limitHookLeftBase = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.HOOK_LEFT_BASE_LIMIT);
        limitHookRightBase = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.HOOK_RIGHT_BASE_LIMIT);

        limitDiscTop = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.DISC_ORIENTATION_LIMIT_A);
        limitDiscBottom = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.DISC_ORIENTATION_LIMIT_B);

        limitShooterA = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.SHOOTER_LIMIT_A);
        limitShooterB = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.SHOOTER_LIMIT_B);
		
		pressureSwitch = new DigitalInput(DIO2, CompetitionBot.DigitalIn2.PRESSURE_SWITCH);
		
		//// DIGITAL SERIAL 1
		
		// TODO:  Accelerometer?
	}
	
	//// CONFIGURATION ---------------------------------------------------------
	
	private static void configure(){
		// Encoders
		encoderDriveLeft.setDistancePerPulse(DPP_ENCODER_DRIVE_LEFT_INCHES);
		encoderDriveRight.setDistancePerPulse(DPP_ENCODER_DRIVE_RIGHT_INCHES);
		encoderWinch.setDistancePerPulse(DPP_ENCODER_WINCH);
		encoderElevator.setDistancePerPulse(DPP_ENCODER_ELEVATOR);
		
		//encoderDriveLeft.start();
		//encoderDriveRight.start();
		encoderWinch.start();
		encoderElevator.start();
		counterShooterAngle.start();
		
		// Shooter Counters
		counterShooterAngle.setTicksPerPeriod(TPP_COUNTER_SHOOTER_ANGLE);
		counterShooterAngle.start();
		counterShooterAngle.setMaxPeriod(10000);
		counterShooterAngle.setUpSourceEdge(true, false); // TODO:  Determine Correct Values
        counterShooterSpeed.start();
		counterShooterSpeed.setMaxPeriod(10000);
		counterShooterSpeed.setUpSourceEdge(true, false);
		
		// Gyro
		// TODO:  Gyro Config?
		//gyroChassis.setSensitivity(GYRO_VPDPS);
	}
}
