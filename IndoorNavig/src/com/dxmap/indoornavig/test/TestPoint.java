package com.dxmap.indoornavig.test;

import com.fengmap.android.map.geometry.FMMapCoord;

public class TestPoint {
	
	public int mGid;
	public FMMapCoord mCoord;
	
	public void parse(int gid,FMMapCoord coord){
		mGid = gid;
		mCoord = coord;
	}
	
	public void  parse(TestPoint point){
		mGid = point.mGid;
		mCoord = point.mCoord;
	}

}
