package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * <p>Author: Ryan</p>
 */
@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class PusherArm {
    /**
     * <Enter note text here>
     */
	
    @objid ("b1de0831-3bc0-40f1-9fd9-0add4a6d68db")
    protected CANTalon tiltMotor;

    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public PusherArm() {
    	tiltMotor = new CANTalon(Parameters.kPusherArmMotorCanId);
    	tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
//    	tiltMotor.changeControlMode(TalonControlMode.Position);
//    	tiltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//    	tiltMotor.setPID(0, 0, 0);
    	tiltMotor.enable();
    	tiltMotor.enableBrakeMode(true);
    }

	@objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualRaise()
	{
		if(tiltMotor.getControlMode()==TalonControlMode.PercentVbus)
		{
			tiltMotor.set(.5);
		}
//		else if(tiltMotor.getControlMode().equals(TalonControlMode.Position))
//		{
//			tiltMotor.set(0);
//		}
    }

    @objid ("bea74f7c-38ea-4673-bb5d-d27ab80f34de")
    public void manualLower()
    {
    	if(tiltMotor.getControlMode()==TalonControlMode.PercentVbus)
		{
			tiltMotor.set(-.5);
		}
//		else if(tiltMotor.getControlMode().equals(TalonControlMode.Position))
//		{
//			tiltMotor.set(0);
//		}
    }

    @objid ("08cddce2-8b6f-4b33-aaab-0e3e90c8fed4")
    public void manualStop() 
    {
//    	if(tiltMotor.getControlMode() != TalonControlMode.PercentVbus)
//    	{
//    		tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
//    	}
    	tiltMotor.set(0);
    }
    
    public void process()
    {
    	
    }
    
    public enum derp
    {
    	Unknown, Home, Down
    }

}
