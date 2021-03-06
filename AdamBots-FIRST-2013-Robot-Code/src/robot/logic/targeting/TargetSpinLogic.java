/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.logic.targeting;

import robot.RobotObject;
import robot.camera.RobotCamera;
import robot.logic.LogicTask;
import robot.logic.tasks.TTurnDegrees;

/**
 *
 * @author Nathan
 */
public class TargetSpinLogic extends RobotObject {
	//// PRINT FILTERING -------------------------------------------------------
	
	/** Hide RobotObject field to allow for proper print filtering. */
	public static boolean verboseOutput = true;
	
	//// CONSTANTS -------------------------------------------------------------

	public static double TARGET_TOLERANCE_DEGREES = 1;
	
	//// PRIVATE VARIABLES -----------------------------------------------------
	
	private static boolean _pointedRight = false;
	private static boolean _isTargeting = false;
	private static TTurnDegrees _turnTask = null;

	//// INITIALIZATION --------------------------------------------------------
	
	/**
	 * Inits anything the class might need.
	 */
	public static void init() {
	}
	
	//// UPDATE ----------------------------------------------------------------
	
	//TODO:  Comment TargetSpinLogic (NATHAN)
	public static void update() {
		if ( _isTargeting ) {
			if ( RobotCamera.imageIsFresh() ) {
				RobotCamera.imageUnfresh();
				if ( Math.abs(RobotCamera.getDirectionDegrees()) > TARGET_TOLERANCE_DEGREES ) {
					_pointedRight = false;
					_turnTask = new TTurnDegrees(RobotCamera.getDirectionDegrees(), 0.1, TARGET_TOLERANCE_DEGREES);
					_turnTask.initialize();
				}
				else
				{
					_pointedRight = true;
				}
			}
			if ( _turnTask != null ) {
				_turnTask.update();
				if ( _turnTask.isDone() ) {
					_turnTask.finish();
					_turnTask = null;
					_pointedRight = true;
				}
			}
		}
		else {
			if ( _turnTask != null ) {
				_turnTask.finish();
				_turnTask = null;
			}
		}
	}
	
	//// GETTER METHODS --------------------------------------------------------
	
	/**
	 * True if the robot need not turn more to face the target.
	 * @return whether needs to turn more
	 */
	public static boolean isPointedRight()
	{
		return _pointedRight;
	}
	
	//// SETTER METHODS --------------------------------------------------------

	/**
	 * Sets whether the robot is targeting.
	 * @param x Whether to target.
	 */
	public static void setIsTargeting( boolean x ) {
		_isTargeting = x;
	}
}
