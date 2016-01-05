package com.ljf.linktry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class MyRectangleView extends View {

	private int[] lastPosArray = null;
	private int[] blockPosNow = null;
	private int Length=0;
	public static final String Tag = "MyView";

	public MyRectangleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		Log.e(Tag, "Draw");
		if (lastPosArray == null || blockPosNow == null || Length == 0) {
			throw new IllegalArgumentException("Position Array is missing");
		}
		// canvas.drawColor(Color.TRANSPARENT);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		Rect r1=new Rect(lastPosArray[0], lastPosArray[1], lastPosArray[0]+Length,lastPosArray[1]+Length);
		canvas.drawRect(r1, paint);
		Rect r2=new Rect(blockPosNow[0], blockPosNow[1], blockPosNow[0]+Length,blockPosNow[1]+Length);
		canvas.drawRect(r2, paint);
	}

	public void setRectangles(int[] lastPosArray, int[] blockPosNow,
			int blockLength) {
//		Log.e(Tag, "setArray");
		this.lastPosArray = lastPosArray;
		this.blockPosNow = blockPosNow;
		this.Length = blockLength;
	}

}
