package com.ljf.linktry;

import com.ljf.linktry.util.SystemUiHider;

import android.R.color;
import android.R.integer;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        
        final LinearLayout main_Layout=(LinearLayout) findViewById(R.id.main_page_part);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        
        int[][] TestArray=new int[6][10];
        TableLayout tableLayout=  initBlocksView(TestArray);
        main_Layout.addView(tableLayout);
        
//        LinearLayout linearLayout=new LinearLayout(this);
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
////        layoutParams.height=200;
////        layoutParams.width=50;
//        linearLayout.setLayoutParams(layoutParams);
//        linearLayout.setBackgroundColor(color.holo_blue_light);
        TextView textView=new TextView(this);
        LinearLayout.LayoutParams lP=  new LinearLayout.LayoutParams(-1, 400);
        lP.height=400;
        lP.setMargins(15, 15, 15, 15);
        textView.setLayoutParams(lP);
        
        textView.setBackgroundColor(getResources().getColor(com.ljf.linktry.R.color.black_overlay));
        textView.setText("I am Here");
        textView.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
//        textView.setHeight(100);     //该句有效  ，不过在上述LayoutParams 存在时无效
        main_Layout.addView(textView);
        
//       View view=  LayoutInflater.from(this).inflate(R.layout.testtv, null);
//       TextView tView=(TextView) view.findViewById(R.id.test_tv);
//       LinearLayout.LayoutParams lP=  new LinearLayout.LayoutParams(-1,-1);
//       lP.height=200;
//       tView.setLayoutParams(lP);
//       tView.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); 
//       main_Layout.addView(tView);
       
        
        
//        
//        
//        new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				 try {
//						int[][] TestArray=RandomInit.twoDimenArray(6,10, 10);
//						TestArray[3][4]=0;
//						TestArray[3][5]=0;
//						TestArray[4][5]=0;
//						TestArray[5][5]=0;
//						TestArray[5][4]=0;
//						
//						Judgement judgement=new Judgement(TestArray);
//						judgement.judge(new int[]{3,3} ,new int[] {5,3});
////						judgement.judge(new int[]{5,9} ,new int[] {5,3});
////						judgement.judge(new int[]{1,0} ,new int[] {5,0});
////						judgement.judge(new int[]{1,9} ,new int[] {5,9});
//					} catch (IllegalArgumentException e) {
//						Log.e("MainActivity", "IllegalArgumentException");
//						e.printStackTrace();
//					} 
//			}
//		}).start();
       
    }


	private TableLayout initBlocksView(int[][] testArray) {
		// TODO Auto-generated method stub
		int x_dimen,y_dimen;
		TableLayout tableLayout=new TableLayout(this);
		
		x_dimen=testArray.length+2;
		y_dimen=testArray[0].length+2;
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		int x_pixel=DM.widthPixels-100;
		int y_pixel=DM.heightPixels-400;
		int x_per=x_pixel/x_dimen-50;
		int y_per=y_pixel/y_dimen-50;
		for (int i = 0; i < y_dimen; i++) {
			
			TableRow tableRow=new TableRow(this);
			for (int j = 0; j < x_dimen; j++) {
				Button button=new Button(this);
				button.setId(j*10+i);
				
//				android.view.ViewGroup.LayoutParams layoutParams= button.getLayoutParams();
				
				TableRow.LayoutParams layoutParams=new TableRow.LayoutParams(x_per, y_per);
//				layoutParams.height=y_per;
//				layoutParams.width=x_per;
				button.setLayoutParams(layoutParams);
//				
				float invisible=0.1f;
				if (j==0||j==x_dimen-1||i==0||i==y_dimen-1) {
					button.setAlpha(invisible);
				}else {
					button.setAlpha(1);
				}
				tableRow.addView(button);
			}
			tableLayout.setShrinkAllColumns(true);
			tableLayout.addView(tableRow);
			
		}
		/*
		 * 一点疑惑 ，为什么代码的设置很多都不能生效，到底什么原因，怎么解决
		 * 为什么不接受Android自带的color ，非要资源目录下的color
		 * */
		LinearLayout.LayoutParams lParams=new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
//		lParams.gravity=Gravity.CENTER;             //代码的优先级高于 xml文件
		lParams.setMargins(5, 15, 5, 5);
		tableLayout.setLayoutParams(lParams);
		tableLayout.setBackgroundColor(getResources().getColor(com.ljf.linktry.R.color.holo_green_light));
		
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
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
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
