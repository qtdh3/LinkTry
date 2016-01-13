package com.ljf.linktry;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class RandomInit {
	public static final String Tag = "RandomInit";

	/**
	 * @author JF-066
	 * 
	 * @return 返回一个随机数据
	 * 
	 * @param x_dimen
	 *            x方向上数据个数
	 * @param y_dimen
	 *            y方向上数据个数
	 * @param style_num
	 *            图形样式的个数
	 * */
	static int[][] twoDimenArray(int x_dimen, int y_dimen, int style_num) {
		int productNum = x_dimen * y_dimen;
		int[][] initArray = new int[x_dimen][y_dimen];
		if (productNum % (style_num * 2) == 0) {
			if (x_dimen % 2 == 0 || y_dimen % 2 == 0)
				Log.e(Tag, "The variable value is legal");
			else
				throw new IllegalArgumentException(
						"The variable value is illegal");
		} else {
			throw new IllegalArgumentException("The variable value is illegal");
		}
		int pairNum = productNum / style_num / 2;
		int style, randomNum, x_newDimen, y_newDimen;
		int[] oneDimenArray = createRandomOneDimenArray(productNum);
		for (int i = 0; i < productNum; i++) {
			style = (int) (i / (pairNum * 2));
			randomNum = oneDimenArray[i];
			x_newDimen = randomNum / y_dimen;
			y_newDimen = randomNum % y_dimen;
			initArray[x_newDimen][y_newDimen] = style + 1;
		}
		// StringBuilder sb=new StringBuilder();
		// sb.append("\n");
		// for(int i=0;i<x_dimen;i++){
		// for(int j=0;j<y_dimen;j++){
		// sb.append( initArray[i][j]);
		// }
		// sb.append("\n");
		// }
		// Log.e(Tag,"RandomArrayPrint:"+ sb.toString());
		return initArray;
	}

	private static int[] createRandomOneDimenArray(int Num) {
		int[] resultArray = new int[Num];
		List<Integer> remainNum = new ArrayList<Integer>();
		for (int i = 0; i < Num; i++) {
			remainNum.add(Integer.valueOf(i));
		}
		for (int i = 0; i < Num; i++) {
			int randomNum = (int) (Math.random() * (Num - i));
			resultArray[i] = remainNum.get(randomNum);
			remainNum.remove(randomNum);
		}
		return resultArray;
	}
	
	public static int[][] adjustRestMatrix(int[][] ArrayHasToReset,int styleNum) {
		int[] nonEmptySave=new int[ArrayHasToReset.length*ArrayHasToReset[0].length];
		int nonEmptyCount=0;
		int styleIndex=0;
		for (int i = 0; i < ArrayHasToReset.length; i++) {
			for (int j = 0; j < ArrayHasToReset[0].length; j++) {
				styleIndex=ArrayHasToReset[i][j];
				if (styleIndex!=0) {
					nonEmptySave[nonEmptyCount]=styleIndex;
					nonEmptyCount++;
				}
			}
		}
		int randomIndex=0;
	    int[] oneDimenArray=createRandomOneDimenArray(nonEmptyCount);
	    for (int i = 0; i < ArrayHasToReset.length; i++) {
			for (int j = 0; j < ArrayHasToReset[0].length; j++) {
				styleIndex=ArrayHasToReset[i][j];
				if (styleIndex!=0) {
					randomIndex= oneDimenArray[nonEmptyCount-1];
					ArrayHasToReset[i][j]=nonEmptySave[randomIndex];
					nonEmptyCount--;
				}
			}
		}
		
		return ArrayHasToReset;
	}
}
