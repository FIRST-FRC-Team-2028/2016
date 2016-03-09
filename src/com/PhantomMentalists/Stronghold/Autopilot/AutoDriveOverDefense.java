package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.PhantomMentalists.Stronghold.Autopilot.AutoDrive.DriveStates;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This class encapsulates the code to drive over one of the Stronghold
 * defenses that require the robot to "simply" drive over them.  These
 * are the Moat, Rock Wall, Ramparts and Rough Terrain.  For all of these
 * defenses the climber, pusher arm and shooter will be in their raised
 * positions.  The constructor will take an enum to specify the exact
 * defense being crossed to allow the speed to be tailored for the
 * specific defense.
 * 
 * @author Hunter
 */
@objid ("2e7b2583-3a04-4188-beec-544aff2708ac")
public class AutoDriveOverDefense extends Autopilot {

	/**
     * <Enter note text here>
     */
	protected Defense defense;
	/**
	 * 
	 */
	protected DriveStates state;

	/**
     * <Enter note text here>
     */
	protected AnalogGyro gyro;
	
	/**
     * <Enter note text here>
     */
    @objid ("e1bf47bf-543a-45f6-b369-e5a8c5869185")
    public AutoDriveOverDefense(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm, DigitalInput lTape, DigitalInput rTape, AnalogGyro gyro, Defense defense) {
        super(drive, shooter, pusherArm, climbingArm, lTape, rTape);
        this.gyro = gyro;
        this.defense = defense;
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
    
    /**
     * <Enter note text here>
     */
    @objid ("46ef2765-db09-4fc1-a9a8-04e9fb7c4ff9")
    public void process() 
    {
    	if(enabled)
    	{
	    	switch(state)
	    	{
		    	case begin:
		    		switch(defense)
		    		{
			    		case kMoat:
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		state = DriveStates.driving;
			    			break;
			    		case kRockWall:
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		state = DriveStates.driving;
			    			break;
			    		case kRamparts:
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		state = DriveStates.driving;
			    			break;
			    		case kRoughTerain:
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		state = DriveStates.driving;
			    			break;
			    		default:
			    			System.out.println("Should never happen in AutoDriveOverDefense");
		    		}
		    		break;
		    	case driving:
		    		//TODO: Might not be correct and check what happens to tape sensor if robot jumps
		    		if(lTape.get())
		    		{
		    			state = DriveStates.done;
		    		}
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
    
    /**
     * <Enter note text here>
     */
    public enum Defense {
        /**
         * <Enter note text here>
         */
    	kMoat,

    	/**
         * <Enter note text here>
         */
    	kRockWall,

    	/**
         * <Enter note text here>
         */
    	kRamparts,

    	/**
         * <Enter note text here>
         */
    	kRoughTerain
    }

}
