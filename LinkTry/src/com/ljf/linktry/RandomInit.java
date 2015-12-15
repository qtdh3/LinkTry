package com.ljf.linktry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.util.Log;

public class RandomInit {
	public static final String Tag="RandomInit";

	/**
	 * @author JF-066
	 * 
	 * @return ����һ���������
	 * 
	 * @param x_dimen x���������ݸ���
	 * @param y_dimen y���������ݸ���
	 * @param style_num ͼ����ʽ�ĸ���
	 * */
	static int[][] twoDimenArray(int x_dimen,int y_dimen,int style_num){
		
		int productNum=x_dimen*y_dimen;
		int[][] initArray= new int[x_dimen][y_dimen];
		
		if (productNum%(style_num*2)==0){
			if(x_dimen%2==0||y_dimen%2==0)
				Log.e(Tag, "The variable value is legal");
			else
				throw new IllegalArgumentException("The variable value is illegal");
		}else{
			throw new IllegalArgumentException("The variable value is illegal");
		}
		
//		for(int i=0;i<x_dimen;i++){
//			for(int j=0;j<y_dimen;j++){
//				initArray[i][j]=0;
//			}
//		}
		
		int pairNum=productNum/style_num/2;
		int style,randomNum,x_newDimen,y_newDimen;
		int[] oneDimenArray=createRandomOneDimenArray(productNum);
		
		for(int i=0;i<productNum;i++){
			
			style=(int) (i/(pairNum*2));
			randomNum=oneDimenArray[i];		
			x_newDimen=randomNum/y_dimen;
			y_newDimen=randomNum%y_dimen;
//			Log.e(Tag, "randomNum:"+randomNum+";x_newDimen:"+x_newDimen+";y_newDimen:"+y_newDimen);
			initArray[x_newDimen][y_newDimen]=style+1;
			
		}
		
		StringBuilder sb=new StringBuilder(); 
		sb.append("\n");
		for(int i=0;i<x_dimen;i++){
			for(int j=0;j<y_dimen;j++){
				sb.append( initArray[i][j]);
			}
			sb.append("\n");
		}
		Log.e(Tag,"RandomArrayPrint:"+ sb.toString());
		
		
		return initArray;
	}
	
	private static int[] createRandomOneDimenArray(int Num){
		
		int[] resultArray=new int[Num];
		List<Integer> remainNum=new ArrayList<Integer>();
		
		for(int i=0;i<Num;i++){
			remainNum.add(new Integer(i));
		}
		for(int i=0;i<Num;i++){
			int randomNum=(int)(Math.random()*(Num-i));
			resultArray[i]=remainNum.get(randomNum);
			remainNum.remove(randomNum);
		}
		Log.e(Tag, "remainNum--size:"+remainNum.size());
		return resultArray;
		
	}
}