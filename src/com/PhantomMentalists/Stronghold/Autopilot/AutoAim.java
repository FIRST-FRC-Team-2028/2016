package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.Telepath;

import edu.wpi.first.wpilibj.PIDController;

public class AutoAim extends Autonomous {

	AimingStates state = AimingStates.getTargetInFrame;
	
	PIDController turncont;
	
	public AutoAim(Telepath tele) {
		super(tele);
		turncont = tele.getTurnController();
		
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
