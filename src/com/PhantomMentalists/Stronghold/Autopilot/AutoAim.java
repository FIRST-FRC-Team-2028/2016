package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;

import edu.wpi.first.wpilibj.DigitalInput;

public class AutoAim extends Autonomous {

	AimingStates state = AimingStates.getTargetInFrame;
	
	public AutoAim(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm,ClimbingArm climbingArm, DigitalInput lTape, DigitalInput rTape) {
		super(drive, shooter, pusherArm, climbingArm, lTape, rTape);
	}

	public void process()
	{
		switch(state)
		{
		case getTargetInFrame:
			break;
		case offsetTargetOnImage:
			break;
		case done:
			break;
		default:
		}
		
	}
	
	public enum AimingStates
	{
		getTargetInFrame,
		offsetTargetOnImage,
		done
	}
}
