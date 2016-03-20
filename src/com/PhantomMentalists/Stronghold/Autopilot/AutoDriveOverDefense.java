package com.PhantomMentalists.Stronghold.Autopilot;

import java.util.Timer;
import java.util.TimerTask;

import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.Telepath;
import com.PhantomMentalists.Stronghold.WestCoastDrive.Gear;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.AnalogGyro;

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
	protected DefenceSelection defense;
	/**
	 * 
	 */
	protected DriveStates state;

	/**
     * <Enter note text here>
     */
	protected AnalogGyro gyro;
	
	protected boolean driven = false;
	
	protected Timer timer;
	
	
	/**
     * <Enter note text here>
     */
    @objid ("e1bf47bf-543a-45f6-b369-e5a8c5869185")
    public AutoDriveOverDefense(Telepath tele,DefenceSelection defence) {
        super(tele);
        this.gyro = tele.getGyro();
        this.defense = defence;
//        this.defense = ;TODO: FIX DISd
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
			    		case kRamp:
			    		case kRock:
			    			drive.setGear(Gear.kHighGear);
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		
				    		timer = new Timer();
				    		timer.schedule(new driveTime(), 4000);
				    		state = DriveStates.driving;
			    			break;
			    		case kRough:
				    		drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
				    		state = DriveStates.driving;
				    		timer = new Timer();
				    		timer.schedule(new driveTime(), 5500);
			    			break;
			    		case kLowBar:
			    			drive.setSpeedSetpoint(Parameters.autonomousDrivePower);
			    			state = DriveStates.driving;
			    			break;
			    		default:
			    			System.out.println("Should never happen in AutoDriveOverDefense");
		    		}
		    		break;
		    	case driving:
		    		//TODO: Might not be correct and check what happens to tape sensor if robot jumps
		    		if(driven)
		    		{
		    			drive.setSpeedSetpoint(0);
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
    
    class driveTime extends TimerTask
    {
    	public void run()
    	{
    		driven = true;
    	}
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
    	kRoughTerain,
    	/**
    	 * 
    	 */
    	kLowBar
    }

}
