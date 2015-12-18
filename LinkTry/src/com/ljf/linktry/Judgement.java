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
		bigArray=new int[xDimenLength+2][yDimenLength+2];
		for (int i = 1; i <= xDimenLength; i++) {
			for (int j = 1; j <= yDimenLength; j++) {
				bigArray[i][j]=smallArray[i-1][j-1];			
			}
		}
		printLocalStates(bigArray,"initBigArray bigarray 3");
		
	}
	/**
	 * @param blockPosition1  point include x,y coordinate
	 * @param blockPosition2  point include x,y coordinate
	 * @return Suceess or Failed ,可以从pathPosArray获取桥点坐标，坐标以大数组为准（bigArray）
	 * */

	public boolean judge(int[] blockPosition1,int[] blockPosition2) {			//判断的一个注意点，   大小数组矩阵的转换 ，都放在最后一步，如 clear的判断
																	// 由于具体判断时不仅有用到大矩阵，还用到小矩阵，所以，最终方块消掉了以后要同时更新大矩阵与小矩阵
		if (!checkInputVariable(blockPosition1,blockPosition2)) 
			throw new IllegalArgumentException("Input Arrays is Wrong");
		
//		printLocalStates(bigArray,"judge bigArray_now");
		Log.e(Tag, "Two Points'value Print|"+smallArray[blockPosition1[0]][blockPosition1[1]]+","+smallArray[blockPosition2[0]][blockPosition2[1]]);
		
		pathPosArray=null;
		int[] blockPos1=new int[]{blockPosition1[0]+1,blockPosition1[1]+1};
		int[] blockPos2=new int[]{blockPosition2[0]+1,blockPosition2[1]+1};
		if (blockPos1[0]==blockPos2[0]||blockPos1[1]==blockPos2[1]) {
			if (judgeLineClear(blockPos1,blockPos2)) {
				//TODO   Case1:	
				pathPosArray=new int[]{LinkType1};
				Log.e(Tag, "Finish judging-- "+"Case1:--pathPosArray.length="+pathPosArray.length);
				return true;
			}else {
				//TODO   Case3: or  Fail
				if(judgeTwoPointRectangle(blockPos1,blockPos2)){
					Log.e(Tag, "Finish judging-- "+"Case3:"+pathPosArray[0]+","+pathPosArray[1]+" "+pathPosArray[2]+","+pathPosArray[3]);
					return true;
				}
				else {
					pathPosArray=new int[]{LinkTypeFail};
					Log.e(Tag, "Finish judging-- "+"Case0:--Failed");
					return false;
				}
				
			}
			
		}
		else if(judgeOnePointCase(blockPos1,blockPos2))
		{
			//TODO   Case2:	
			pathPosArray=new int[]{pathPosArray[0],pathPosArray[1],LinkType2};
			Log.e(Tag, "Finish judging-- "+"Case2:"+pathPosArray[0]+","+pathPosArray[1]);
			return true;
		}else {
			//TODO   Case3: or  Fail
			if(judgeTwoPointCase(blockPos1,blockPos2)){
				Log.e(Tag, "Finish judging-- "+"Case3:"+pathPosArray[0]+","+pathPosArray[1]+" "+pathPosArray[2]+","+pathPosArray[3]);
				return true;
			}
			else {
				pathPosArray=new int[]{LinkTypeFail};
				Log.e(Tag, "Finish judging-- "+"Case0:--Failed");
				return false;
			}
		}
	}

	private boolean judgeTwoPointRectangle(int[] blockPos1, int[] blockPos2) {
		//TODO  改为用大矩阵，保证能取到外框边
		Log.e(Tag, "judgeTwoPointRectangle");
		if (blockPos1[0]==blockPos2[0]) {
			for (int i = blockPos2[0]+1; i <bigArray.length-1; i++) {
				int[] testPoint=new int[]{i, blockPos1[1]};
				if (!isEmpty(testPoint)) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
			for (int i = blockPos2[0]-1; i >=0; i--) {
				int[] testPoint=new int[]{i, blockPos1[1]};
				if (!isEmpty(testPoint)) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
		}else {
			for (int i = blockPos2[1]+1; i <smallArray[0].length+1; i++) {
				int[] testPoint=new int[]{blockPos1[0],i};
				if (!isEmpty(testPoint)) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
			for (int i = blockPos2[1]-1; i >=0; i--) {
				int[] testPoint=new int[]{blockPos1[0],i};
				if (!isEmpty(testPoint)) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
		}
		return false;
	}

	private boolean isEmpty(int[] testPoint) {
		if (testPoint.length==2) 
			throw new IllegalArgumentException("int Array's length is wrong");
		if (bigArray[testPoint[0]][testPoint[1]]!=0) {
			return true;
		}
		return false;
	}

	private boolean judgeTwoPointCase(int[] blockPos1, int[] blockPos2) {		//  先跟据测试点与点1的判断决定循环是否继续走下去，包括对测试点是否为空的判断，测试点从靠近点1开始取
		//TODO  改为用大矩阵，保证能取到外框边
		// 从第二方向出发，寻找合适点
		if (blockPos1[1]>blockPos2[1]) {
			for (int i = blockPos1[1]-1; i > blockPos2[1]; i--) {
//			for (int i = blockPos2[1]+1; i < blockPos1[1]; i++) {
				int[] testPoint=new int[]{ blockPos1[0],i};
				if (smallArray[testPoint[0]][testPoint[1]]!=0) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
		}
		else {
			for (int i = blockPos1[1]+1; i < blockPos2[1]; i++) {
				int[] testPoint=new int[]{ blockPos1[0],i};
				if (smallArray[testPoint[0]][testPoint[1]]!=0) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
		}
		
		//  从第一方向出发，寻找合适点
		if (blockPos1[0]>blockPos2[0]) {
			for (int i =blockPos1[0]-1; i > blockPos2[0]; i--) {
//			for (int i = blockPos2[0]+1; i < blockPos1[0]; i++) {
				
				int[] testPoint=new int[]{i, blockPos1[1]};
				if (smallArray[testPoint[0]][testPoint[1]]!=0) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
		}
		else {
			for (int i = blockPos1[0]+1; i < blockPos2[0]; i++) {
				int[] testPoint=new int[]{i, blockPos1[1]};
				if (smallArray[testPoint[0]][testPoint[1]]!=0) {
					break;
				}
				if (!judgeLineClear(testPoint, blockPos1)) {
					break;
				}
				if (judgeOnePointCase(testPoint, blockPos2)) {
					int[] pathCase3=new int[5];
					System.arraycopy(testPoint, 0, pathCase3, 0, 2);
					System.arraycopy(pathPosArray, 0, pathCase3, 2, 2);
					pathCase3[4]=LinkType3;
					pathPosArray=pathCase3; 
					return true;
				}
			}
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
					if (bigArray[blockPos1[0]][i]!=0) {				//Have transform smallArray into bigArray's coordinate
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
					if (bigArray[blockPos1[0]][i]!=0) {
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
					if (bigArray[i][blockPos1[1]]!=0) {
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
					if (bigArray[i][blockPos1[1]]!=0) {
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
			if (blockPos1[0]>=xDimenLength||blockPos2[0]>=xDimenLength
					||blockPos1[1]>=yDimenLength||blockPos2[1]>=yDimenLength) {
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