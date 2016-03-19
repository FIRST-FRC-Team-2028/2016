package com.PhantomMentalists.Stronghold;
/**
 * imports
 */
import com.PhantomMentalists.Stronghold.DriveUnit.Placement;
import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Drive
 * 
 * The drive system allows us to drive the robot and to change between high gear and low gear using 
 * joysticks/controllers. Moreover, we implement the change from high gear to low gear if colliding 
 * with another robot.
 * 
 * States:
 * <p>
 * voltage control mode / speed control mode
 * The robot is driven with speed control mode, which makes the robot more reliable. Nevertheless, 
 * we implement the change to voltage mode in case a failure happens.  Inputs/Sensors: Quad encoders
 * (both right and left side).
 * <p>
 * low gear / high gear
 * The two states depend on the solenoid position. The transition between states (from low gear to
 * high gear and vice versa) depend on the inputs from the joysticks/controllers. We also implement 
 * the change from high gear to low gear if a certain threshold of current is met, for preventing
 * motors from burning up. It affects both sides. Inputs/sensors : None. 
 * <p>
 * Sensors
 * Two quad encoders (1 per side speed control).
 * Motors
 * 4 Talon SRX (2 on each side)
 * 
 * @author Zavion
 */
@objid ("bfe07845-b9da-40e9-a047-c801ee86b209")
public class WestCoastDrive implements PIDOutput
{   
    
   /**
    * left side
    */
    @objid ("b8a106fc-3334-47d0-b500-cf128b91b6c0")
	protected DriveUnit leftSide;
  
   /**
	* Right side
	*/
    @objid ("d0db5703-a6c1-4909-baf4-37eb81a88730")
    protected DriveUnit rightSide;

    /**
     * The setpoint for the gear we want both DriveUnits to be in.
     */
    @objid ("28bc5bec-15fd-4924-b01c-5f725341e189")
    protected Gear gear = Gear.kLowGear;

   /**
    * Controls the solenoid valve for the high gear 
    */
   protected Solenoid highGear;
   /**
    * Controls the solenoid valve for the low gear
    */
   protected Solenoid lowGear;
   /**
    * constructor
    */
    @objid ("06e23f53-7280-4c29-b319-50a651aca613")
    public WestCoastDrive() 
    
    {
    	  leftSide = new DriveUnit(Placement.Left);
    	  rightSide = new DriveUnit(Placement.Right);
    	  highGear = new Solenoid(Parameters.kHighGearSolenoidChanel);
    	  lowGear = new Solenoid(Parameters.kLowGearSolenoidChanel);
    	  setGear(Gear.kLowGear);
    }

   /**
    * This method is invoked by the turn PID controller whenever the measured turn angle
    * is different from the turn setpoint.  It passes the supplied parameter 'output' to
    * both the left and right side DriveUnits as their turn setpoint.
    * 
    * @param output the value calculated by PIDController
    */
    @objid ("fc5f1ae8-f7bf-4800-924d-f54e5ee2a661")
    public void pidWrite(double output) 
    {
    	System.out.println("PID write: "+output);
    	setTurnSetpoint(output);
    }
    
   /**
 	* Sets the set point for drive speed.
 	* <p>
 	* Sets the forward/reverse setpoint for both the right and left DriveUnit (the power applied
 	* when the robot is driving in a straight line).  The actual power applied to the motor can be
 	* different if there is a turn setpoint set as well as a speed setpoint.
 	* <p>
 	* When in voltage control mode,  this method takes a percentage parameter (-1.0 .. 0 .. 1.0),
 	* however, when in speed control mode this method takes a velocity (in encoder "ticks" per
 	* 100 miliseconds (e.g., -1300 .. 0 .. 1300).
 	* 
 	* @param setPoint - The speed setpoint the range of (-1.0 .. 0 .. 1.0) when in voltage mode and
 	*                   "ticks" per 100 miliseconds when in speed control mode
 	*/
    @objid ("efa356b3-f44c-44ee-a418-1ba6be974be5")
    public void setSpeedSetpoint(double setPoint)
    {
    	leftSide.setSpeedSetpoint(setPoint);
    	rightSide.setSpeedSetpoint(setPoint);	
    }
    
    /**
     * Sets the speed setpoint to left and right DriveUnit individually
     * <p>
     * This method is used when we have a two joystick control scheme and we want to specify
     * the power to the left and right side explicitly.  Note:  In this control scheme we will
     * never need to set the turn setpoint (it will always be zero).
     * 
     * @param leftSetPoint
     * @param rightSetPoint
     */
    public void setSpeedSetpoint(double leftSetPoint, double rightSetPoint)
    {
    	leftSide.setSpeedSetpoint(leftSetPoint);
    	rightSide.setSpeedSetpoint(rightSetPoint);
    }

   /**
 	* gets the speed setpoint.  
 	* <p>
 	* Since both the right and left DriveUnit will have the same speed setpoint, this method
 	* can return the value from either the right or left DriveUnit.
 	* 
 	* @return double - The speed setpoint for the two DriveUnits.  
 	*/
     @objid ("ea714300-5ea7-4831-acb5-2503084baa47")
    public double getSpeedSetpoint()
    {
    	return leftSide.getSpeedSetpoint();
    }
    
     /**
      * Sets the turn power for the robot.
      * <p>
      * The power applied to the right and left DriveUnits varies depending on how fast we're 
      * driving (speedSetpoint).  The faster we're going, the less delta in speed we'll allow
      * between the two DriveUnits.  This method passes the turn setpoint to the two DriveUnits
      *  
      * @param value Percentage to turn in the range (-1.0 .. 0 .. 1.0) where negative values 
      *              turn the robot left and positive values turn the robot right
      */     
     @objid ("77501865-67e2-4889-b074-f047bd3616a6")
     void setTurnSetpoint(double value)
     { 
    	 double leftvalue = value*0.5;
    	 double rightvalue = -value*0.5;
    	 System.out.println("Left: "+leftvalue);
    	 System.out.println("Right: "+rightvalue);
    	 leftSide.setSpeedSetpoint(0);
    	 rightSide.setSpeedSetpoint(0);
    	 leftSide.setTurnSetpoint(leftvalue);
    	 rightSide.setTurnSetpoint(rightvalue);
     }
     
     /**
      * 
      * @param value
      * @return
      */
     double getTurnSetpoint(double value)
     {
    	 return leftSide.getTurnSetpoint();
     }
     
   /**
    * Gets the gear that both DriveUnits are in.  Since both DriveUnits share a single
    * 2-solenoid value body (the air line has a tee to feed both gearboxes), this class
    * maintains the setpoint.
    * 
    * @return Gear - The gear that both DriveUnits are in 
    */
    @objid ("77501865-67e2-4889-b074-f047bd3616a6")
    public Gear getGear() 
    {
    	 return gear;
    }
    
   /**
    * Sets the gear for both the right and left DriveUnit
    * <p>
    * This method sets the left and right gear based on the gear value
    * 
    * @param value - The new gear value
    */
    @objid ("3e825af2-8a08-4402-9058-dd7ae4aa2260")
    public void setGear(Gear value) 
    {
    	gear = value;
    	if (gear == Gear.kLowGear)
    	{
    		highGear.set(false);
    		lowGear.set(true);
    	}
    	else
    	{
    		lowGear.set(false);
    		highGear.set(true);
    	}
    }

    /**
     * This method is called every iteration through the robot's main loop, in both
     * autonomous and operatorControll.  It checks safety values (such as any of the 
     * drive motor's current draw exceeding it's threshold and downshifting to low gear
     * if exceeded) as well as populating the smart dashboard with any telemetry values.
     */
    public void process()
    {
//    	if(leftSide.isCurrentThresholdExceeded() || rightSide.isCurrentThresholdExceeded())
//    	{
//    		setGear(Gear.kLowGear);
//    	}
    	leftSide.process();
    	rightSide.process();
    }
    
    /**
     * This enum provides an easy to reference name to the constant values for the low
     * and high gear mode for drive.
     */
    public enum Gear {
        kLowGear,
        kHighGear;
    }
}
