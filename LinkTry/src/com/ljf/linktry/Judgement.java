package com.ljf.linktry;

import java.util.ArrayList;

import android.R.integer;
import android.util.Log;

public class Judgement {
	

	private int[][] smallArray=null;
	private int xDimenLength;
	private int yDimenLength;
	private int[][] bigArray=null;
	
	int[] pathPosArray=null;
	public static final String Tag="Judgement";
	
	public static final int LinkType1=1;
	public static final int LinkType2=2;
	public static final int LinkType3=3;
	public static final int LinkTypeFail=0;
	
	/**
	 * @param smallArray
	 * 
	 */
	public Judgement(int[][] smallArray) {
		super();
		this.smallArray = smallArray;
		this.xDimenLength=smallArray.length;
		this.yDimenLength=smallArray[0].length;
		initBigArray();
	}
	
	private void initBigArray() {
		bigArray=new int[xDimenLength+1][yDimenLength+1];
		for (int i = 1; i < xDimenLength; i++) {
			for (int j = 1; j < xDimenLength; j++) {
				bigArray[i][j]=smallArray[i-1][j-1];			
			}
		}
		printLocalStates(bigArray,"initBigArray bigarray 3");
		
	}

	public boolean judge(int[] blockPos1,int[] blockPos2) {
		if (!checkInputVariable(blockPos1,blockPos2)) 
			throw new IllegalArgumentException("Input Arrays is Wrong");
		
		printLocalStates(bigArray,"judeg bigArray_now");
		pathPosArray=null;
		if (blockPos1[0]==blockPos2[0]||blockPos1[1]==blockPos2[1]) {
			if (judgeLineClear(blockPos1,blockPos2)) {
				//TODO   Case2:	
				pathPosArray=new int[]{LinkType1};
				Log.e(Tag, "Finish judging-- "+"Case1:--pathPosArray.size="+pathPosArray);
				
			}else {
				
				//TODO   Case3: or  Fail
			}
			
		}
		else if(judgeOnePointCase(blockPos1,blockPos2))
		{
			//TODO   Case2:	
			pathPosArray=new int[]{pathPosArray[0],pathPosArray[1],LinkType2};
			Log.e(Tag, "Finish judging-- "+"Case2:"+pathPosArray[0]+","+pathPosArray[1]);
		}else {
			//TODO   Case3: or  Fail
			
		}

		
		return false; 
	}

	private boolean judgeLineClear(int[] blockPos1, int[] blockPos2) {
		if (blockPos1[0]==blockPos2[0]) {
			if (blockPos1[1]>blockPos2[1]) {
				if (blockPos1[1]-blockPos2[1]==1) {
					return true;
				}
				for (int i = blockPos2[1]+1; i < blockPos1[1]; i++) {
					if (bigArray[blockPos1[0]+1][i+1]!=0) {
						return false;
					}
				}
				return true;
			}
			else if (blockPos1[1]<blockPos2[1]) {
				if (blockPos2[1]-blockPos1[1]==1) {
					return true;
				}
				for (int i = blockPos1[1]+1; i < blockPos2[1]; i++) {
					if (bigArray[blockPos1[0]+1][i+1]!=0) {
						return false;
					}
				}
				return true;
			}
			else {
				throw new IllegalArgumentException("Two block Position is same one");
			}
		}else if (blockPos1[1]==blockPos2[1]) {
			if (blockPos1[0]>blockPos2[0]) {
				if (blockPos1[0]-blockPos2[0]==1) {
					return true;
				}
				for (int i = blockPos2[0]+1; i < blockPos1[0]; i++) {
					if (bigArray[i+1][blockPos1[1]+1]!=0) {
						return false;
					}
				}
				return true;
			}
			else if (blockPos1[0]<blockPos2[0]) {
				if (blockPos2[0]-blockPos1[0]==1) {
					return true;
				}
				for (int i = blockPos1[0]+1; i < blockPos2[0]; i++) {
					if (bigArray[i+1][blockPos1[1]+1]!=0) {
						return false;
					}
				}
				return true;
			}
			else {
				throw new IllegalArgumentException("Two block Position is same one");
			}
		}else {
			throw new IllegalArgumentException("No Dimen is same");
		}
	}

	private boolean judgeOnePointCase(int[] blockPos1, int[] blockPos2) {
		if (blockPos1[0]==blockPos2[0]||blockPos1[1]==blockPos2[1]) {
			throw new IllegalArgumentException("Do not at Different Lines");
		}
		
		int[] BridePoint1=new int[]{blockPos1[0],blockPos2[1]};
		int[] BridePoint2=new int[]{blockPos2[0],blockPos1[1]};
		
		if (judgeLineClear(blockPos1, BridePoint1)) {
			if (judgeLineClear(blockPos2, BridePoint1)) 
			{
				pathPosArray=BridePoint1;
				return true;
			}
			else
				return false;
		}else if (judgeLineClear(blockPos1, BridePoint2)) {
			if(judgeLineClear(blockPos2, BridePoint2))
			{
				pathPosArray=BridePoint2;
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	private boolean checkInputVariable(int[] blockPos1, int[] blockPos2) {
		if (blockPos1.length==2&&blockPos2.length==2) 
		{
			if (blockPos1[0]==blockPos2[0]&&blockPos1[1]==blockPos2[1]) {
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}

	private void printLocalStates(int[][] Array,String hintString ) {
		StringBuilder sb=new StringBuilder(); 
//		sb.append("\n");
		for(int i=0;i<Array.length;i++){
			sb.append("\n");
			for(int j=0;j<Array[i].length;j++){
				sb.append( Array[i][j]);
				sb.append(" ");
			}
			
		}
		Log.e(Tag,hintString+"  ArrayPrint:"+ sb.toString());
		
	}
	
}
