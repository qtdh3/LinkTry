package com.ljf.linktry;

import java.util.Timer;
import java.util.TimerTask;

import com.ljf.linktry.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	// private static final boolean AUTO_HIDE = false;

	// /**
	// * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	// * user interaction before hiding the system UI.
	// */
	// private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	// private static final boolean TOGGLE_ON_CLICK = true;
	// private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	// private static final int HIDER_FLAGS =
	// SystemUiHider.FLAG_HIDE_NAVIGATION;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	private int[] lastBlockPos = null;
	private int[] lastBlockPosReal = null;
	private Judgement judgement = null;
	private int[] LeftRightPosReal = null;
	private int blockLinesLength;

	private static final String Tag = "MainActivity";

/*	 @Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	 super.onWindowFocusChanged(hasFocus);
	
	 Log.e(Tag, "onWindowFocusChanged");
	 Display display=getWindowManager().getDefaultDisplay();
	 Point outSize=new Point();
	 display.getSize(outSize);
	
	 Rect noStateBarRect = new Rect();
	 getWindow().getDecorView().getWindowVisibleDisplayFrame(noStateBarRect);
	 int stateBarHeit = noStateBarRect.top;
	
	 Rect drawRect=new Rect();
	 getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(drawRect);
	 Log.e(Tag,
	 "Window.ID_ANDROID_CONTENT："+getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT));
	 int top=
	 getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	 Log.e(Tag,
	 "Screen Height="+outSize.y+";stateBarHeit="+stateBarHeit+";noStateBarRect.height="+noStateBarRect.height()+";\ndrawRect.top="+top+";drawRect.height"+drawRect.height());
	 }*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		// final View controlsView =
		// findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		final LinearLayout main_Layout = (LinearLayout) findViewById(R.id.main_page_part);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					// int mControlsHeight;
					// int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						// if (Build.VERSION.SDK_INT >=
						// Build.VERSION_CODES.HONEYCOMB_MR2) {
						// // If the ViewPropertyAnimator API is available
						// // (Honeycomb MR2 and later), use it to animate the
						// // in-layout UI controls at the bottom of the
						// // screen.
						// if (mControlsHeight == 0) {
						// mControlsHeight = controlsView.getHeight();
						// }
						// if (mShortAnimTime == 0) {
						// // mShortAnimTime = getResources().getInteger(
						// // android.R.integer.config_shortAnimTime);
						// mShortAnimTime = 1000;
						// }
						// controlsView.animate()
						// .translationY(visible ? 0 : mControlsHeight)
						// .setDuration(mShortAnimTime);
						// } else {
						// // If the ViewPropertyAnimator APIs aren't
						// // available, simply show or hide the in-layout UI
						// // controls.
						// controlsView.setVisibility(visible ? View.VISIBLE :
						// View.GONE);
						// }
						//
						// if (visible && AUTO_HIDE) {
						// // Schedule a hide().
						// // delayedHide(AUTO_HIDE_DELAY_MILLIS);
						// delayedHide(1200);
						// }
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		// contentView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// if (TOGGLE_ON_CLICK) {
		// mSystemUiHider.toggle();
		// } else {
		// mSystemUiHider.show();
		// }
		// }});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		// findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

		int[][] TestArray = RandomInit.twoDimenArray(8, 8, 16);
		judgement = new Judgement(TestArray);
		TableLayout tableLayout = initBlocksView(TestArray);
		main_Layout.addView(tableLayout);

		/*TextView textView = new TextView(this);
		LinearLayout.LayoutParams lP = new LinearLayout.LayoutParams(-1, 400);
		lP.height = 400;
		lP.setMargins(15, 15, 15, 15);
		textView.setLayoutParams(lP);

		textView.setBackgroundColor(getResources().getColor(
				com.ljf.linktry.R.color.black_overlay));
		textView.setText("I am Here");
		textView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		// textView.setHeight(100); //该句有效 ，不过在上述LayoutParams 存在时无效
		main_Layout.addView(textView);*/
	}

	private View.OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int btnId = v.getId();
//			Log.e(Tag, "onClickListener:" + btnId);
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
//			Log.e(Tag, "LocationInCanvas:" + PosAtParentCor[0] + ","
//					+ PosAtParentCor[1]);

			boolean isRight = false;
			if (lastBlockPos != null) {
				if (lastBlockPos[0] != blockPosNow[0]
						|| lastBlockPos[1] != blockPosNow[1]) {
					try {
						if (judgement.judge(lastBlockPos, blockPosNow)) {
//							Log.e(Tag, "路径判断结果为--成立");
							if (judgement.judgeStyle(lastBlockPos, blockPosNow)) {
//								Log.e(Tag, "样式判断结果为--成立");
								judgement.removeBlocks(lastBlockPos,
										blockPosNow);
								int[] pathArray = judgement.pathPosArray;
								FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayoutinside);
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

								// delay one second then removeBlockAndLine
								final int[] lastBlockPosCopy = lastBlockPos;
								TimerTask timerTask = new TimerTask() {
									@Override
									public void run() {
										removeBlockAndLine(lastBlockPosCopy,
												blockPosNow);
									}
								};
								Timer timer = new Timer();
								timer.schedule(timerTask, 1000);
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

	private static final int MARGINS_LEFT = 5;
	private static final int MARGINS_TOP = 5;

	private void removeBlockAndLine(final int[] lastBlockPos,
			final int[] blockPosNow) {
		// findViewById(0).setAlpha(0);
		runOnUiThread(new Runnable() {
			public void run() {
				findViewById(100 * lastBlockPos[0] + lastBlockPos[1]).setAlpha(
						0);
				findViewById(blockPosNow[0] * 100 + blockPosNow[1]).setAlpha(0);
				FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayoutinside);
				frameLayout.removeViewAt(frameLayout.getChildCount() - 1);
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
					button.setAlpha(1);
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

					default:
						color = Color.BLACK;
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
					button.setOnClickListener(onClickListener);
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

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				// delayedHide(AUTO_HIDE_DELAY_MILLIS);
				delayedHide(100);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
