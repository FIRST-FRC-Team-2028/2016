package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
//import edu.wpi.first.wpilibj.DigitalInput;

/**
 * <p>Author: Ricky</p>
 */
/**
 * @author Ricky
 *
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")

public class ClimbingArm {
    /**
     * 
     */
	protected CANTalon position, l_pull, r_pull, extend;
	
//	DigitalInput extendedlswitchl = new DigitalInput(0);
//	DigitalInput extendedlswitchr = new DigitalInput(1);
//	DigitalInput retractedlswitchl = new DigitalInput(2);
//	DigitalInput retractedlswitchr = new DigitalInput(3);

//    @objid ("de0098a0-c9b9-42cb-8012-212eec71543c")
//    protected CANTalon extendRetractMotor;
//
//    @objid ("94a77f41-a9e3-4f3b-b9c4-58e16c9e1898")
//    protected CANTalon raiseLowerMotor;

    @objid ("c215a8ac-a925-4c23-aab7-ec75ee354734")
    public ClimbingArm()
    {
    	position = new CANTalon(0);
		l_pull = new CANTalon(0);
		r_pull = new CANTalon(0);
		extend = new CANTalon(0);
		
		position.changeControlMode(TalonControlMode.Position);
		l_pull.changeControlMode(TalonControlMode.PercentVbus);
		r_pull.changeControlMode(TalonControlMode.PercentVbus);
		extend.changeControlMode(TalonControlMode.Position);
		
		position.enableBrakeMode(true);
		l_pull.enableBrakeMode(true);
		r_pull.enableBrakeMode(true);
		extend.enableBrakeMode(true);
		
		position.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		extend.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		position.setPID(0, 0, 0);
		extend.setPID(0, 0, 0);
		
		position.enable();
		l_pull.enable();
		r_pull.enable();
		extend.enable();
    }
    /**
     * this method gets the angle of stage one.
     * @return angle value of stage one.
     */
    @objid ("a661b8a2-befb-4ed3-95cc-e445670fa559")
    public double getStageOnePosition() 
    {
        return position.getPosition();
    }

    /**
     * This method sets the angle of stage one.
     * @param Position the angle that stage one will be set to
     */
    @objid ("6de04ff3-23df-472a-9c94-0e666ba14075")
    public void setStageOnePosition(double Position) 
    {
    	position.setPosition(Position);
    }
    /**
     * This method checks if the arm is in a known position, if it is known it will return true, if it isn't it will return false.
     * @return true if arm is in a known position, False if it is not.
     */
    public boolean isKnownPosition()
    {
    	return false;
    }
    /**
     * This method checks if stage two is fully extended on the right side.  If it is fully extended, it will return true
     * If it is not fully extended it will return false.
     * @return true if stage two is extended on right side, False if it is not.
     */
    public boolean isStageTwoExtended()
   {	
    	// setSetPoint ---> getSetPoint ---> keep checking ---> arm is there 
    	//apparently i can't be serious in coding so i will not be funny anymore!
    	
    	
//    	if(extendedlswitchl.get() == false)
//    	{
//    		return false;
//    	}
//    	else
//    	{
//    		return true;
//    	}
    	return false;
    }
    /**
     * This method checks if stage two of the climber arm is fully retracted on the right side. If it is retracted, it will return true.
     * If it is not retracted it will return false.
     * @return true if stage two is retracted on the right side, False if it is not.
     */
    public boolean isStageTwoRetracted()		
    {
//    	if(retractedlswitchr.get() == true)
//    	{
//    		return true;
//    	}
//    	else
//    	{
//    		return false;
//    	}
    	return false;
    }
    
    /**
     * This method gets the position of the first stage. (the first stage is what is connected to the chasiss.)
     * 
     * @return angle value of stage one.
     */ 
    public double getPosition()
    {
    	return position.getPosition();
    }
    
    /**
     * This method set the position of the first stage. (the first stage is what is connected to the chassis.)
     * @param positionAngle the angle that the first stage will be set to.
     */
    public void setPosition(double positionAngle)
    {
    	position.setPosition(positionAngle);
    }
    
    /**
     * This method sets the position of the first stage at home. Home is when the arm is laying horizontally facing the back of the robot.
     * @param homeAngle the angle value of the home position of the first stage.
     */
    public void homePosition()
    {
    	position.setPosition(0);
    }
    /**
     * This method extends the position of stage two of the climber arm.
     * It extends the left and right pulling motors while extending the second stage with the extend motor.
     * @param extendPosition position that stage two will extend to.
     * @param retractV the voltage value that will be applied to retracting stage two.
     */
    @objid ("5447b3c0-a7a2-45f6-8b5a-ec97ee145ea1")
    public void extendStageTwo(double extendPosition, double retractV) 
    {
    	extend.setPosition(extendPosition);
    	l_pull.set(retractV);
    	r_pull.set(retractV);
    }

    /**
     * This method retracts stage two of the climber arm.
     * @param retractPosition position that stage two will retract to.
     * @param retractV the voltage value that will be applied to retracting stage two.
     */
    @objid ("0053d6a3-1878-46e1-b726-12de04b65c1d")
    public void retractStageTwo(double retractPosition, double retractV)
    {
    	extend.setPosition(retractPosition);
    	l_pull.set(retractV);
    	r_pull.set(retractV);
    }
}
