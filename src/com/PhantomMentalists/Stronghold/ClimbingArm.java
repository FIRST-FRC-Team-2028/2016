package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 * <p>Author: Ricky</p>
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")
public class ClimbingArm {
    /**
     * <Enter note here>
     */
	CANTalon position, l_pull, r_pull, extend;
	
	DigitalInput extendedlswitchl = new DigitalInput(0);
	DigitalInput extendedlswitchr = new DigitalInput(1);
	DigitalInput retractedlswitchl = new DigitalInput(2);
	DigitalInput retractedlswitchr = new DigitalInput(3);

    @objid ("de0098a0-c9b9-42cb-8012-212eec71543c")
    protected CANTalon extendRetractMotor;

    @objid ("94a77f41-a9e3-4f3b-b9c4-58e16c9e1898")
    protected CANTalon raiseLowerMotor;

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

    @objid ("a661b8a2-befb-4ed3-95cc-e445670fa559")
    public double getStageOnePosition() 
    {
        return position.getPosition();
    }

    @objid ("6de04ff3-23df-472a-9c94-0e666ba14075")
    public void setStageOnePosition(double Position) 
    {
    	position.setPosition(Position);
    }

    @objid ("a46baf37-02e4-43cf-bd92-42521e614a74")
    public boolean isStageTwoExtendedl() 
    {	
    	if(extendedlswitchr.get() == false)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
    public boolean isStageTwoExtendedr()
    {	
    	if(extendedlswitchl.get() == false)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }

    @objid ("5caadcfa-d288-4f21-999d-8c9b7150dba7")
    public boolean isStageTwoRetractedl() 
    {	
        if(retractedlswitchl.get() == true)
        {
        	return true;
        }
        else
        {
        	return false; 
        }
    }
    	
    public boolean isStageTwoRetractedr()		
    {
    	if(retractedlswitchr.get() == true)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    @objid ("5447b3c0-a7a2-45f6-8b5a-ec97ee145ea1")
    public void extendStageTwo(double extendPosition) 
    {
    	extend.set(extendPosition); 
    }

    @objid ("0053d6a3-1878-46e1-b726-12de04b65c1d")
    public void retractStageTwo(double retractV) 
    {
    	l_pull.set(retractV);
    	r_pull.set(retractV);
    }
}
