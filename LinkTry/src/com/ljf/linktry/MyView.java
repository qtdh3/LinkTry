package com.ljf.linktry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

public class MyView extends View {

	private int[] lastPosArray = null;
	private int[] blockPosNow = null;
	private int[] pathArray = null;
	public static final String Tag = "MyView";

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		Log.e(Tag, "Draw");
		if (lastPosArray == null || blockPosNow == null || pathArray == null) {
			throw new IllegalArgumentException("Position Array is missing");
		}
		// canvas.drawColor(Color.TRANSPARENT);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(13);

		Path path = new Path();
		path.moveTo(lastPosArray[0], lastPosArray[1]);
		int caseIntNum = pathArray[pathArray.length - 1];
		switch (caseIntNum) {
		case Judgement.LinkType1:
			break;
		case Judgement.LinkType2:
			path.lineTo(pathArray[0], pathArray[1]);
			break;
		case Judgement.LinkType3:
			path.lineTo(pathArray[0], pathArray[1]);
			path.lineTo(pathArray[2], pathArray[3]);
			break;
		default:
			break;
		}
		path.lineTo(blockPosNow[0], blockPosNow[1]);
		// path.close(); //该句会回到初始点，形成封闭的曲线
		// canvas.drawCircle(340, 340, 130, paint);
		canvas.drawPath(path, paint);
	}

	public void setPosArray(int[] lastPosArray, int[] blockPosNow,
			int[] pathArray) {
//		Log.e(Tag, "setArray");
		this.lastPosArray = lastPosArray;
		this.blockPosNow = blockPosNow;
		this.pathArray = pathArray;
	}

}
