package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.Parameters;

public enum DefenceSelection 
{
	kLowBar(Parameters.kLowBar),
	kRock(Parameters.kRock),
	kRough(Parameters.kRough),
	kDraw(Parameters.kDraw),
	kSally(Parameters.kSally),
	kRamp(Parameters.kRamp),
	kMoat(Parameters.kMoat),
	kPort(Parameters.kPort),
	kCheval(Parameters.kCheval);
	
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
