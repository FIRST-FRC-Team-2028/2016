package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.Parameters;

public enum DefenceSelection 
{
	kLowBar(Parameters.kLowBar),//everything down
	kRock(Parameters.kRock),//shooter up, pusher up, climber down
	kRough(Parameters.kRough),//shooter level, climber down, pusher up
	kDraw(Parameters.kDraw),
	kSally(Parameters.kSally),
	kRamp(Parameters.kRamp),//shooter up, climber down,pusher up
	kMoat(Parameters.kMoat),//shooter level, climber down, pusher up
	kPort(Parameters.kPort),
	kCheval(Parameters.kCheval),//shooter up, climber down
	kClimb(Parameters.kClimb);
	
	private int num;
	DefenceSelection(int num)
	{
		this.num = num;
	}
	public int getNum()
	{
		return num;
	}
}
