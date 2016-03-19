package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.Camera;
import com.PhantomMentalists.Stronghold.ClimbingArm.ClimberPositions;
import com.PhantomMentalists.Stronghold.PusherArm.Position;
import com.PhantomMentalists.Stronghold.Shooter.ShooterState;
import com.PhantomMentalists.Stronghold.Telepath;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    
    protected Camera cam;
    
    @objid ("89eb6fde-adee-4a71-8d26-0f72ceaaea93")
    public void process() 
    {
    	SmartDashboard.putString("Auto State", state.name());
    	switch(state)
    	{
    	case kInit:
    		state = State.kHome;
    		break;
    	case kHome:
    		if(lane == 1 || defence == DefenceSelection.kPort || defence == DefenceSelection.kRough)//Low Bar Lane
    		{
    			if(!homing)
    			{
	    			homing = true;
	    			shooter.setAutoPilot(true);
	    			shooter.setShootAngle(ShooterState.kHome);
	    			climbingArm.setPositionSetpoint(ClimberPositions.kHome);
	//    			TODO: Duck bar Home method
    			}
    			else if(shooter.isShooterHome() /*TODO: check if duck bar is also home*/)
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
    		if(lane == 1)
    		{
    			shooter.setShootAngle(ShooterState.kReload);
    			climbingArm.setPositionSetpoint(ClimberPositions.kLowBar);
    			pusherArm.setTiltPosition(Position.kDown);
//    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(180));
    			if(shooter.isTiltAngleAtSetpoint() && climbingArm.isAtSetpoint() && pusherArm.isPositionAtSetpoint())
    			{
    				state = State.kCrossDefence;
    			}
    		}
    		if(defence == DefenceSelection.kRough || defence == DefenceSelection.kMoat)
    		{
    			shooter.setShootAngle(ShooterState.kHome);
//    			climbingArm.setPositionSetpoint(ClimberPositions.kLowBar);
//    			pusherArm.setTiltPosition(Position.kHome);
    			if(shooter.isShooterHome())
    			{
    				state = State.kCrossDefence;
    			}
    		}
    		if(defence == DefenceSelection.kRock || defence == DefenceSelection.kRamp)
    		{
    			
    		}
    		if(defence == DefenceSelection.kCheval)
    		{
    			
    		}
    		break;
    	case kCrossDefence:
    		if(autopilotMode == null)
    		{
	    		autopilotMode = new AutoDriveOverDefense(tele,defence);
	    		autopilotMode.setEnabled(true);
	    		
    		}
    		else
    		{
	    		autopilotMode.process();
	    		if(((AutoDriveOverDefense)autopilotMode).isDone())
	    		{
	    			state = State.kDrive;
	    			autopilotMode = null;
	    		}
    		}
    		break;
    	case kDrive:
    		state = State.kAim;
//    		if(autopilotMode == null)
//    		{
//    			autopilotMode = new AutoDrive(tele);
//    			autopilotMode.setEnabled(true);
//    		}
//    		else
//    		{
//    			autopilotMode.process();
//    			if(((AutoDrive)autopilotMode).isDone())
//    			{
//    				state = State.kTurn;
//    				autopilotMode = null;
//    			}
//    		}
    		break;
    	case kTurn:
    		if(lane == 1)
    		{
    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(319.62));
    		}
    		else if(lane == 2)
    		{
    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(334.36));
    		}
    		else if(lane == 3)
    		{
    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(343.68));
    		}
    		else if(lane == 4)
    		{
    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(6.59));
    		}
    		else if(lane == 5)
    		{
    			turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(19.17));
    		}
    		if(isAngleInDeadband(gyro.getAngle(),turncon.getSetpoint()))
    		{
    			state = State.kAim;
    		}
    		break;
    	case kAim:
    		cam.setCam(-1, 0.8);
    		cam.getImage();
    		cam.centerTarget();
    		shooter.setShooterAngle(cam.getCameraAngle());
//    		turncon.setSetpoint(gyro.getAbsoluteAngleFromRelative(gyro.getRelativeAngle()+cam.getDiffAngleX()));
//    		turncon.enable();
    		state = State.kDone;
    		break;
    	case kDriveToRange:
    		break;
    	case kShoot:
    		break;
    	case kDone:
    		break;
    	}
    	shooter.process();
//    	climberArm.process();
    	pusherArm.process();
    	drive.process();
    }

    /**
     * Constructor
     * 
     * This method initializes the local references to Telepath's major components.
     */
    @objid ("a472f866-0ec6-4c52-bc30-c893eb1086b3")
    public Autonomous(Telepath tele) {
        super(tele);
        cam = tele.getCamera();
    }

    public void setLane(int lane)
    {
    	this.lane = lane;
    }
    
    public void setDefence(DefenceSelection defence)
    {
    	this.defence = defence;
    }
    
    public boolean isAngleInDeadband(double angle,double setpoint)
    {
    	boolean val = false;
    	double up = setpoint+1;
    	double down = setpoint-1;
    	if(angle <= up && angle >= down)
    	{
    		val = true;
    	}
    	return val;
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
