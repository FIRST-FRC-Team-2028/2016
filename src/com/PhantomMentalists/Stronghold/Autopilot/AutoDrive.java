package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.DigitalInput;

@objid ("faf35b12-f052-4aeb-b323-6c3b10671387")
public class AutoDrive extends Autopilot {
   
	DriveStates state;
		
	@objid ("13eec206-2a84-4912-8c67-28d062557de8")
    public AutoDrive(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm, DigitalInput lTape, DigitalInput rTape) {
        super(drive, shooter, pusherArm, climbingArm, lTape, rTape);
    }

	public void setEnabled(boolean value)
	{
		enabled = value;
		if(value)
		{
			state = DriveStates.begin;
		}
		else
		{
			state = DriveStates.stopped;
		}
	}
	
    @objid ("730f3e5c-3b87-46f6-934e-79aa04f30461")
    public void process() 
    {
    	if(enabled)
    	{
	    	switch(state)
	    	{
		    	case begin:
		    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
		    		state = DriveStates.driving;
		    		break;
		    	case driving:
		    		//TODO: FIX
		    		state = DriveStates.done;
		    		break;
		    	case done:
		    	case stopped:
		    		drive.setSpeedSetpoint(0);
		    		break;
		    	default:
	    	}
    	}
    }
    
    public boolean isDriving()
    {
    	return state == DriveStates.driving;
    }
    
    public boolean isDone()
    {
    	return state == DriveStates.done;
    }
    
    public boolean isStopped()
    {
    	return state == DriveStates.stopped;
    }
    
    public enum DriveStates
    {
    	begin,
    	driving,
    	done,
    	stopped
    }
}
