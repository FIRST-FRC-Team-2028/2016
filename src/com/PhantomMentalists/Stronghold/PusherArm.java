package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * <p>Author: Austin</p>
 */
@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class PusherArm
{
	CANTalon positionMotor;
    /**
     * <Enter note text here>
     */
    @objid ("b1de0831-3bc0-40f1-9fd9-0add4a6d68db")
    protected CANTalon tiltMotor;

    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public PusherArm()
    {
    	positionMotor = new CANTalon(0);
		positionMotor.changeControlMode(TalonControlMode.Position);
		positionMotor.enableBrakeMode(true);
		positionMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		positionMotor.setPID(0, 0, 0);
		positionMotor.enable();
    }

    @objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualRaise()
    {
    	
    }

    @objid ("bea74f7c-38ea-4673-bb5d-d27ab80f34de")
    public void manualLower()
    {
    	
    }

    @objid ("08cddce2-8b6f-4b33-aaab-0e3e90c8fed4")
    public void manualStop()
    {
    	
    }
    
}
