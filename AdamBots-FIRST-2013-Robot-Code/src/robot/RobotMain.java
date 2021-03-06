/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.IO.DataIO;
import robot.actuators.FancyMotor;
import robot.actuators.RobotActuators;
import robot.behavior.RobotClimb;
import robot.behavior.RobotDrive;
import robot.behavior.RobotShoot;
import robot.camera.RobotCamera;
import robot.control.FancyJoystick;
import robot.logic.LogicPhase;
import robot.logic.LogicTask;
import robot.logic.targeting.TargetShooterAngleLogic;
import robot.logic.targeting.TargetShooterSpeedLogic;
import robot.logic.targeting.TargetSpinLogic;
import robot.logic.auton.AutonLogic;
import robot.logic.auton.AutonType;
import robot.logic.climb.ClimbLogic;
import robot.logic.teleop.TeleopLogic;
import robot.sensors.RobotSensors;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation.
 * 
 * Controls program flow and initializes all necessary classes.  
 *
 * @author Ben Bray
 * @author Steven Ploog
 */
public final class RobotMain extends IterativeRobot {
    //// INSTANCE --------------------------------------------------------------

    private static RobotMain _instance;

    /** Gets the active instance of RobotMain. **/
    public static RobotMain getInstance() {
		return _instance;
    }
    
    //// OUTPUT CONSTANTS ------------------------------------------------------
    
	public static final boolean ALLOW_OUTPUT		= true;
    public static final boolean VERBOSE_AUTON		= true;
    public static final boolean VERBOSE_TELEOP		= false;
    public static final boolean VERBOSE_CLIMB		= false;
    public static final boolean VERBOSE_ROBOTCLIMB	= false;
    public static final boolean VERBOSE_ROBOTDRIVE	= false;
    public static final boolean VERBOSE_ROBOTPICKUP	= false;
    public static final boolean VERBOSE_ROBOTSHOOT	= false;
    public static final boolean VERBOSE_ROBOTCAMERA	= false;
    public static final boolean VERBOSE_TARGETLOGIC	= false;
	public static final boolean VERBOSE_FANCYMOTOR	= false;
	public static final boolean VERBOSE_LOGICTASK	= false;
    
    //// ROBOT LOGIC PHASES ----------------------------------------------------
    
    private LogicPhase _currentLogicPhase = null;
    private AutonLogic _autonLogic;
    private TeleopLogic _teleopLogic;
    private ClimbLogic _climbLogic;
    
    //// JOYSTICKS -------------------------------------------------------------
    
    public static FancyJoystick primaryJoystick;
    public static FancyJoystick secondaryJoystick;
	
	// Smartdashboard "RobotPreferences" widget getter
	private static Preferences prefs;
	
    //// ITERATIVE ROBOT METHODS -----------------------------------------------
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		_instance = this;
		
		//Loads the calibration file
		DataIO.loadCalibrations();
		
		// Initialize Classes with Static References
		FancyMotor.init();
		RobotActuators.init();
		RobotSensors.init();
		RobotActuators.configure();
		RobotSensors.configure();

		// Initialize Static Behavior Classes
		RobotDrive.init();
		RobotShoot.init();

		//Initialize Static Logic Classes
		TargetShooterAngleLogic.init();
		TargetShooterSpeedLogic.init();
		TargetSpinLogic.init();

		// Output Filtering
		RobotClimb.verboseOutput = VERBOSE_ROBOTCLIMB;
		RobotDrive.verboseOutput = VERBOSE_ROBOTDRIVE;
		RobotShoot.verboseOutput = VERBOSE_ROBOTSHOOT;
		RobotCamera.verboseOutput = VERBOSE_ROBOTCAMERA;
		TargetShooterAngleLogic.verboseOutput = VERBOSE_TARGETLOGIC;
		TargetShooterSpeedLogic.verboseOutput = VERBOSE_TARGETLOGIC;
		TargetSpinLogic.verboseOutput = VERBOSE_TARGETLOGIC;
		FancyMotor.verboseOutput = VERBOSE_FANCYMOTOR;
		AutonLogic.verboseOutput = VERBOSE_AUTON;
		TeleopLogic.verboseOutput = VERBOSE_TELEOP;
		ClimbLogic.verboseOutput = VERBOSE_CLIMB;
		LogicTask.verboseOutput = VERBOSE_LOGICTASK;

		// Initialize Joysticks
		primaryJoystick = new FancyJoystick(FancyJoystick.PRIMARY_DRIVER, .15);
		secondaryJoystick = new FancyJoystick(FancyJoystick.SECONDARY_DRIVER);

		// Turn lights on
		//RobotActuators.ledGreenEffect.set(true);
    }

    //// AUTONOMOUS ------------------------------------------------------------
    
    /**
     * Initialization code for the autonomous period.
     */
    public void autonomousInit() {
		System.out.println("RobotMain :: autonomousInit()");
		
		// Manage Camera & Lights
		RobotCamera.init();
		RobotActuators.cameraLED.set(true);
		
		// Initialize AutonLogic
		System.out.println("\tautonInit() :: creating new instance of AutonLogic()");
		_autonLogic = new AutonLogic();
		segueToLogicPhase(_autonLogic);
		System.out.println("\tautonInit() :: end of autoninit");
    }

    /**
     * Called periodically during autonomous.
     */
    public void autonomousPeriodic() {
		update();
    }

    //// TELEOP ----------------------------------------------------------------
    
    /**
     * Initialization code for the teleoperated period.
     */
    public void teleopInit() {
		System.out.println("RobotMain :: teleopInit()");
		// Camera Init
		RobotCamera.init();
		
		// Initialize Climbing and Teleop
		_teleopLogic = new TeleopLogic();
		_climbLogic = new ClimbLogic();
		segueToLogicPhase(_teleopLogic);

		// Manage LEDs
		RobotActuators.cameraLED.set(true);
		RobotActuators.ledGroundEffect.set(true);
		RobotActuators.ledArmEffect.set(true);

		// Destroy Autonomous if it Exists
		if (_autonLogic != null) {
			_autonLogic = null;
		}
		
		// Start and Reset Encoders
		RobotSensors.encoderDriveLeft.start();
		RobotSensors.encoderDriveLeft.reset();
		RobotSensors.encoderDriveRight.start();
		RobotSensors.encoderDriveRight.reset();
		
		RobotShoot.stopMovingToTarget();
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
		update();
    }

    //// UPDATE ----------------------------------------------------------------
    
    public void update() {
		
		// Update the current LogicPhase
		if(_currentLogicPhase != null){
			_currentLogicPhase.updatePhase();
		}
		
		// Compressor
		if (RobotSensors.pressureSwitch.get()) {
			RobotActuators.compressor.set(Relay.Value.kOff);
		} else {
			RobotActuators.compressor.set(Relay.Value.kOn);
		}
		
		// Update Subsystems
		TargetShooterSpeedLogic.update();
		TargetShooterAngleLogic.update();
		TargetSpinLogic.update();
		RobotShoot.update();
		//TODO: Add robotcamera back in RobotCamera.update();
		RobotClimb.update();
		FancyMotor.update();	// Checks Limit Switches for each FancyMotor
		
		// Print to Dashboardp
		SmartDashboard.putNumber("Target Place", RobotCamera.getTargetLocationUnits());
		
		// Smartdashboard get variables
		RobotShoot.SHOOTER_KP = SmartDashboard.getNumber("shooterPidKP", 0.0001);
		RobotShoot.SHOOTER_KI = SmartDashboard.getNumber("shooterPidKI", 0.0010);
		RobotShoot.SHOOTER_KD = SmartDashboard.getNumber("shooterPidKD", 0.0000);
		
		SmartDashboard.putNumber("currentShooterPidKP", RobotShoot.SHOOTER_KP);
		SmartDashboard.putNumber("currentShooterPidKI", RobotShoot.SHOOTER_KI);
		SmartDashboard.putNumber("currentShooterPidKD", RobotShoot.SHOOTER_KD);
		
		SmartDashboard.putNumber("shooterWheelVoltage", RobotActuators.shooterWheelMotor.get());
		
		SmartDashboard.putBoolean("configA", RobotSensors.configA.get());
		SmartDashboard.putBoolean("configB", RobotSensors.configB.get());
		SmartDashboard.putBoolean("configC", RobotSensors.configC.get());
		
		SmartDashboard.putNumber("winchVoltage", RobotActuators.climbWinch.get());
		
		// Reset Shooter Lift Encoder if it's at the Bottom of its Range
		SmartDashboard.putBoolean("shooterAngleLimitB", RobotSensors.limitShooterB.get());
		SmartDashboard.putBoolean("Can Expand Winch", !RobotSensors.limitWinchA.getRaw());
		SmartDashboard.putNumber("Shooter Angle", RobotShoot.getShooterAngleDegrees());
		SmartDashboard.putNumber("stringPot.getVoltage", RobotSensors.stringPot.getVoltage());
		SmartDashboard.putBoolean("Shooter In Position", RobotShoot.isShooterInPosition());
		
		if (RobotShoot.isShooterInPosition())
		{
			long u = System.currentTimeMillis();
			//RobotActuators.ledGreenEffect.set((u % 300) < 150);
			//RobotActuators.ledArmEffect.set((u % 300) < 150);
			//RobotActuators.ledGroundEffect.set((u % 300) < 150);
		}
		else
		{
			RobotActuators.ledArmEffect.set(true);
			RobotActuators.ledGroundEffect.set(true);
		}
		
    }

    //// TEST ------------------------------------------------------------------
    
    /**
     * Initialization code for test mode should go here
     */
    public void testInit() {
		System.out.println("RobotMain :: testInit()");
	}

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
	}

    //// DISABLED --------------------------------------------------------------
    
    /**
     * Initialization code for disabled mode should go here
     */
    public void disabledInit() {
		System.out.println("RobotMain :: disabledInit()");
                RobotDrive.shiftNeutral();

		// Turn off LEDs
		RobotActuators.cameraLED.set(false);
		RobotActuators.ledGroundEffect.set(false);
		RobotActuators.ledArmEffect.set(false);
		
		//DataIO.writeLogFile();
    }

    /**
     * Periodic code for disabled mode should go here.
     */
    public void disabledPeriodic() {
        RobotDrive.shiftNeutral();
		SmartDashboard.putBoolean("configA", RobotSensors.configA.get());
		SmartDashboard.putBoolean("configB", RobotSensors.configB.get());
		SmartDashboard.putBoolean("configC", RobotSensors.configC.get());
    }

    //// LOGICPHASE METHODS ----------------------------------------------------
    
	/**
	 * Ends the current logic phase by calling its finish() method and nulling
	 * the current LogicPhase instance.  Does <i>not</i> segue to another phase,
	 * but rather waits for the next phase to be initiated by another process.
	 */
	public void endPhase(){
		System.out.println("RobotMain :: endPhase()");
		
		if(_currentLogicPhase != null){
			_currentLogicPhase.finishPhase();
			_currentLogicPhase = null;
		}
	}
	
	/**
     * Revokes power from the logic phase currently in control and grants
     * control to the phase specified. Before the segue, this method invokes
     * finish() in the original phase, and after the segue, this method invokes
     * init() in the new phase.
     *
     * @param phase An integer indicating the phase to switch to.
     * @return Boolean value indicating the success or failure of the segue.  
	 * (Success=TRUE, Failure=FALSE)
     * @see LogicPhase#AUTONOMOUS
     * @see LogicPhase#TELEOP
     * @see LogicPhase#CLIMB
     */
    public boolean segueToLogicPhase(int phase) {
		System.out.println("RobotMain :: segueToLogicPhase(int)");
		
		LogicPhase segueTo;
		switch (phase) {
			case LogicPhase.AUTONOMOUS:
				if(DriverStation.getInstance().isAutonomous()) { return false; }
				System.out.println("\tTransitioning to AutonLogic...");
				segueTo = new AutonLogic();
			break;
			case LogicPhase.TELEOP:
				if(DriverStation.getInstance().isOperatorControl()) { return false; }
				System.out.println("\tTransitioning to TeleopLogic...");
				segueTo = new TeleopLogic();
			break;
			case LogicPhase.CLIMB:
				System.out.println("\tTransitioning to ClimbLogic...");
				segueTo = new ClimbLogic();
			break;
			default:
				throw new IllegalArgumentException();
		}

		return segueToLogicPhase(segueTo);
    }

    /**
     * Revokes power from the logic phase currently in control and grants
     * control to the phase specified. Before the segue, this method invokes
     * finish() in the original phase, and after the segue, this method invokes
     * init() in the new phase.
     *
     * @param phase The phase to transition to.
     * @return Boolean value indicating the success or failure of the segue.
     * @see LogicPhase
     * @see LogicPhase#finishPhase()
     * @see LogicPhase#initPhase()
     */
    public boolean segueToLogicPhase(LogicPhase phase) {
		System.out.println("RobotMain :: segueToLogicPhase(LogicPhase)");
		// Finish the Current Phase
		if (_currentLogicPhase != null) {
			_currentLogicPhase.finishPhase();
		}

		// Initialize the Specified New Phase
		System.out.println("\tSetting current logic phase and initializing...");
		_currentLogicPhase = phase;
		_currentLogicPhase.initPhase();
		System.out.println("\tDone initializing.");

		return true; // TODO:  Update segueToLogicPhase() return value as needed.
    }
}
