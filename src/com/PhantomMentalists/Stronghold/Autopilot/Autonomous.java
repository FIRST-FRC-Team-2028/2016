package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.ClimbingArm.ClimberPositions;
import com.PhantomMentalists.Stronghold.Shooter.ShooterPosition;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class controls the Telepath robot during the 15 second autonomous period.  It instantiates its own Autopilot classes to control traversing the defense, driving to the goal and shooting.
 */
@objid ("d5c53723-495a-4d0d-9cc0-37fc89facee4")
public class Autonomous extends Autopilot {
    /**
     * <Enter note text here>
     */
    @objid ("81e544d6-b681-4b60-8197-0654c188c97b")
    protected Autopilot autopilotMode;
    
    protected int lane;
    
    protected DefenceSelection defence;
    
    protected State state = State.kInit;
    
    protected boolean homing = false;

    @objid ("89eb6fde-adee-4a71-8d26-0f72ceaaea93")
    public void process() 
    {
    	switch(state)
    	{
    	case kInit:
    		state = State.kHome;
    		break;
    	case kHome:
    		if(lane == 1 || defence == DefenceSelection.kPort)//Low Bar Lane
    		{
    			if(!homing)
    			{
	    			homing = true;
	    			shooter.setShootAngle(ShooterPosition.kHome);
	    			climbingArm.setPositionSetpoint(ClimberPositions.kHome);
	//    			TODO: Duck bar Home method
    			}
    			else if(shooter.isTiltAngleAtSetpoint() && climbingArm.isAtSetpoint() /*TODO: check if duck bar is also home*/)
        		{
        			homing = false;
        			state = State.kConfigure;
        		}
    		}
    		else if(defence == DefenceSelection.kDraw || defence == DefenceSelection.kSally)
    		{
    			if(!homing)
    			{
    				homing = true;
    				climbingArm.setPositionSetpoint(ClimberPositions.kHome);
    				//TODO: Duck bar Home method
    			}
    			else if(climbingArm.isAtSetpoint() /*TODO: check if duck bar is home*/)
    			{
    				homing = false;
    				state = State.kConfigure;
    			}
    		}
    		else if(defence == DefenceSelection.kCheval)
    		{
    			if(!homing)
    			{
    				homing = true;
    				//TODO: Duck bar home
    			}
//    			else if(TODO:Check for duck bar home)
    			{
    				homing = false;
    				state = State.kConfigure;
    			}
    		}
    		break;
    	case kConfigure:
    		
    		break;
    	case kCrossDefence:
    		break;
    	case kDrive:
    		break;
    	case kTurn:
    		break;
    	case kAim:
    		break;
    	case kDriveToRange:
    		break;
    	case kShoot:
    		break;
    	case kDone:
    		break;
    	}
    }

    /**
     * Constructor
     * 
     * This method initializes the local references to Telepath's major components.
     */
    @objid ("a472f866-0ec6-4c52-bc30-c893eb1086b3")
    public Autonomous(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm) {
        super(drive, shooter, pusherArm, climbingArm);
    }

    public void setLane(int lane)
    {
    	this.lane = lane;
    }
    
    public void setDefence(int defence)
    {
    	for(DefenceSelection defences: DefenceSelection.values())
    	{
    		if(defences.getNum() == defence)
    		{
    			this.defence = defences;
    			return;
    		}
    	}
    }
    
    public enum State{
    	kInit,
    	kHome,
    	kConfigure,
    	kCrossDefence,
    	kDrive,
    	kTurn,
    	kAim,
    	kDriveToRange,
    	kShoot,
    	kDone;
    }
}
