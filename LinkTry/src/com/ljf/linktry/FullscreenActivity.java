package com.ljf.linktry;

import com.ljf.linktry.util.SystemUiHider;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	private int[] lastBlockPos = null;
	private int[] lastBlockPosReal = null;
	private Judgement judgement = null;
	private int[] LeftRightPosReal = null;
	private int blockLinesLength;
	private MediaPlayer mediaPlayer;
	
	private static final int STYLENUM=16;
	private static final String Tag = "MainActivity";

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		Log.d(Tag, "onWindowFocusChanged");
		
		if (hasFocus) {
			setOriginalCoordinate();
			mediaPlayer=MediaPlayer.create(FullscreenActivity.this, R.raw.ding);
		}else {
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		final LinearLayout main_Layout = (LinearLayout) findViewById(R.id.main_page_part);

		int[][] TestArray = RandomInit.twoDimenArray(8, 8, STYLENUM);
		judgement = new Judgement(TestArray,16);
		TableLayout tableLayout = initBlocksView(TestArray);
		main_Layout.addView(tableLayout);
		
		Button hintButton=new Button(this);
		hintButton.setText("Hint");
		hintButton.setBackgroundColor(Color.BLUE);
		hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(Tag, "hintButton Pressed");
				int[] checkResult=null;
				try {
					checkResult = judgement.checkForPairs();
				} catch(IllegalStateException exception){
					exception.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (checkResult.length<4) {
					Log.d(Tag, "没有匹配的结果，需要重新调整阵列");
				}else {
					for (int i = 0; i < 4; i++) {
						checkResult[i] = (checkResult[i])
								* blockLinesLength
								+ LeftRightPosReal[i % 2];
					}
					
					final MyRectangleView myRectangleView=new MyRectangleView(FullscreenActivity.this);
					myRectangleView.setRectangles(new int[]{checkResult[0],checkResult[1]}, new int[]{checkResult[2],checkResult[3]}, blockLinesLength);
					final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayoutinside);
					frameLayout.addView(myRectangleView);
					frameLayout.postDelayed(new Runnable() {
						@Override
						public void run() {
							frameLayout.removeView(myRectangleView);
							
						}
					}, 1000);
				}
			}
		});
		main_Layout.addView(hintButton);
		
		Button resetButton=new Button(this);
		resetButton.setText("ResetMatrix");
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(Tag, "resetButton Pressed");
				int[][] haveResetArray= RandomInit.adjustRestMatrix(judgement.getSmallArray(),STYLENUM);
				judgement=new Judgement(haveResetArray, STYLENUM);
				TableLayout haveResettableLayout= initBlocksView(haveResetArray);
				main_Layout.removeViewAt(0);
				main_Layout.addView(haveResettableLayout, 0);
			}
		});
		main_Layout.addView(resetButton);
	}

	private View.OnClickListener blocks_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int btnId = v.getId();
			// get the matrix simple coordinate
			final int[] blockPosNow = new int[2];
			blockPosNow[0] = btnId / 100;
			blockPosNow[1] = btnId % 100;

			if (LeftRightPosReal == null) {
				setOriginalCoordinate();
			}

			int[] PosAtParentCor = new int[2];
			PosAtParentCor[0] = v.getLeft();
			PosAtParentCor[1] = ((View) v.getParent()).getTop();
			PosAtParentCor[0] += blockLinesLength / 2 + MARGINS_LEFT;
			PosAtParentCor[1] += blockLinesLength / 2 + MARGINS_TOP;

			boolean isRight = false;
			if (lastBlockPos != null) {
				if (lastBlockPos[0] != blockPosNow[0]
						|| lastBlockPos[1] != blockPosNow[1]) {
					try {
						if (judgement.judge(lastBlockPos, blockPosNow)) {
//							Log.d(Tag, "路径判断结果为--成立");
							if (judgement.judgeStyle(lastBlockPos, blockPosNow)) {
//								Log.d(Tag, "样式判断结果为--成立");
								judgement.removeBlocks(lastBlockPos,
										blockPosNow);
								int[] pathArray = judgement.pathPosArray;
								FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayoutinside);
								Thread thread= new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											mediaPlayer.start();
										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}
								});
								thread.start();
								
								try {
									// change simple matrix coordinate into
									// canvas coordinate
									for (int i = 0; i < pathArray.length - 1; i++) {
										pathArray[i] = (pathArray[i])
												* blockLinesLength
												+ LeftRightPosReal[i % 2]
												- blockLinesLength / 2;
									}
									// add View to show the path line with white
									// color
									MyView myView = new MyView(
											FullscreenActivity.this);
									myView.setPosArray(lastBlockPosReal,
											PosAtParentCor, pathArray);
									frameLayout.addView(myView);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								}

								final int[] lastBlockPosCopy = lastBlockPos;
								removeBlockAndLine(lastBlockPosCopy,blockPosNow);
								isRight = true;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (isRight) {
				lastBlockPos = null;
			} else {
				lastBlockPos = blockPosNow;
				lastBlockPosReal = PosAtParentCor;
			}

		}

	};

	protected int normalViewCount =3;

	private static final int MARGINS_LEFT = 5;
	private static final int MARGINS_TOP = 5;

	private void removeBlockAndLine(final int[] lastBlockPos,
			final int[] blockPosNow) {
		runOnUiThread(new Runnable() {
			public void run() {
				final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayoutinside);
				int childViewCount=frameLayout.getChildCount();
				Log.d(Tag, "childViewCount="+childViewCount);
				Animation animation=AnimationUtils.loadAnimation(FullscreenActivity.this, R.anim.disappear);
				animation.setFillAfter(true);
				final View shouRemoveView;
				shouRemoveView=frameLayout.getChildAt(childViewCount - 1);
				shouRemoveView.startAnimation(animation);
				Log.d(Tag, "shouRemoveView"+shouRemoveView);
					
				findViewById(100 * lastBlockPos[0] + lastBlockPos[1]).startAnimation(animation);
				findViewById(blockPosNow[0] * 100 + blockPosNow[1]).startAnimation(animation);
				frameLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						frameLayout.removeView(shouRemoveView);
						findViewById(100 * lastBlockPos[0] + lastBlockPos[1]).setAlpha(
								0);
						findViewById(blockPosNow[0] * 100 + blockPosNow[1]).setAlpha(0);
					}
				}, 1000);
			}
		});

	}

	private void setOriginalCoordinate() {
		View firstButtonView = findViewById(0);
		LeftRightPosReal = new int[2];
		LeftRightPosReal[0] = firstButtonView.getLeft() + MARGINS_LEFT;
		LeftRightPosReal[1] = ((View) firstButtonView.getParent()).getTop()
				+ MARGINS_TOP;
		blockLinesLength = firstButtonView.getWidth();
	}

	@SuppressWarnings("deprecation")
	private TableLayout initBlocksView(int[][] testArray) {
		int x_dimen, y_dimen;
		TableLayout tableLayout = new TableLayout(this);

		x_dimen = testArray.length + 2;
		y_dimen = testArray[0].length + 2;
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		int x_pixel = DM.widthPixels;
		int x_per = x_pixel / x_dimen;
		for (int i = 0; i < y_dimen; i++) {

			TableRow tableRow = new TableRow(this);
			for (int j = 0; j < x_dimen; j++) {
				Button button = new Button(this);
				TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
						x_per, x_per);
				button.setLayoutParams(layoutParams);
				// button.setBackground(getResources().getDrawable(R.drawable.button_selector));
				GradientDrawable shapeDrawable = (GradientDrawable) getResources()
						.getDrawable(R.drawable.btn_shape);
				Drawable[] drawables = new Drawable[] {
						getResources().getDrawable(R.drawable.ic_launcher),
						shapeDrawable };
				LayerDrawable layerDrawable = new LayerDrawable(drawables);
				StateListDrawable stateListDrawable = new StateListDrawable();
				stateListDrawable.addState(
						new int[] { android.R.attr.state_pressed },
						layerDrawable);
				// button.setBackgroundResource(R.drawable.button_selector);
				// button.setBackground(stateListDrawable);
				// button.setBackgroundDrawable(stateListDrawable);
				float invisible = 0.1f;
				if (j == 0 || j == x_dimen - 1 || i == 0 || i == y_dimen - 1) {
					button.setAlpha(invisible);
					button.setBackgroundColor(0xff99cc00);
				} else {
					int color = Color.GREEN;
					switch (testArray[j - 1][i - 1]) {
					case 1:
						color = Color.BLUE;
						break;
					case 2:
						color = Color.CYAN;
						break;
					case 3:
						color = Color.DKGRAY;
						break;
					case 4:
						color = Color.LTGRAY;
						break;
					case 5:
						color = Color.MAGENTA;
						break;
					case 6:
						color = Color.RED;
						break;
					case 7:
						color = Color.GREEN;
						break;
					case 8:
						color = Color.BLACK;
						break;
					case 9:
						color = Color.GRAY;
						break;
					case 10:
						color = 0xFF13579b;
						break;
					case 11:
						color = Color.WHITE;
						break;
					case 12:
						color = Color.YELLOW;
						break;
					case 13:
						color = com.ljf.linktry.R.color.black_overlay;
						break;
					case 14:
						color = 0xFFdd7711;
						break;
					case 15:
						color = 0xFF336699;
						break;
					case 16:
						color = 0xFFb91357;
						break;

					default:                    // inclue the case 0:
						color = Color.TRANSPARENT;
//						button.setAlpha(invisible);
						button.setAlpha(0f);
						break;
					}
					// drawable=getResources().getDrawable(R.drawable.ic_launcher);
					ColorDrawable colorDrawable = new ColorDrawable(color);
					stateListDrawable.addState(
							new int[] { -android.R.attr.state_pressed },
							colorDrawable);
					button.setBackgroundDrawable(stateListDrawable);
					// button.setBackgroundColor(color);
					button.setId((j - 1) * 100 + i - 1);
					button.setOnClickListener(blocks_onClickListener);
//					button.setAlpha(1);
				}
				tableRow.addView(button);
			}
			// tableLayout.setShrinkAllColumns(true);
			tableLayout.addView(tableRow);

		}
		/*
		 * 一点疑惑 ，为什么代码的设置很多都不能生效，到底什么原因，怎么解决 为什么不接受Android自带的color
		 * ，非要资源目录下的color
		 */
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		// lParams.gravity=Gravity.CENTER; //代码的优先级高于 xml文件
		lParams.setMargins(MARGINS_LEFT, MARGINS_TOP, 5, 5);
		tableLayout.setLayoutParams(lParams);
		tableLayout.setBackgroundColor(getResources().getColor(
				com.ljf.linktry.R.color.holo_green_light));

		return tableLayout;
	}
}
