package org.xdty.smilehelper;

import java.lang.reflect.Field;
import java.util.List;

import org.xdty.smilehelper.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatWindowAddView extends LinearLayout {

    /**
     * 记录添加悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录添加悬浮窗的高度
     */
    public static int viewHeight;
    
    /**
     * 用于更新添加悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 添加悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;
    
    /**
     * 记录系统状态栏的高度
     */
     private static int statusBarHeight;
    
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在添加悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在添加悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    
    public FloatWindowAddView(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_add, this);
        View view = findViewById(R.id.add_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button add = (Button) findViewById(R.id.add);
        Button finish = (Button) findViewById(R.id.finish);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击添加后添加当前的activity到数据库
                Toast.makeText(context, getTopActivityName(context), Toast.LENGTH_LONG).show();
            }
        });
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击完成后返回到主界面
                MyWindowManager.removeAddWindow(context);
                MyWindowManager.removeBigWindow(context);
                Intent mIntent = new Intent();
                mIntent.setClassName("org.xdty.smilehelper",  
                        "org.xdty.smilehelper.MainActivity"); 
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
                MyWindowManager.removeAddWindow(context);
                MyWindowManager.setAddState(false);
            }
        });
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
            xInView = event.getX();
            yInView = event.getY();
            xDownInScreen = event.getRawX();
            yDownInScreen = event.getRawY() - getStatusBarHeight();
            xInScreen = event.getRawX();
            yInScreen = event.getRawY() - getStatusBarHeight();
            break;
        case MotionEvent.ACTION_MOVE:
            xInScreen = event.getRawX();
            yInScreen = event.getRawY() - getStatusBarHeight();
            // 手指移动的时候更新添加悬浮窗的位置
            updateViewPosition();
            break;
        case MotionEvent.ACTION_UP:
            // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
            if (Math.abs(xDownInScreen - xInScreen) < 3 && Math.abs(yDownInScreen - yInScreen) < 3) {
                //openBigWindow();
            }
            break;
        default:
            break;
        }
        return true;
    }
    
    /**
     * 将添加悬浮窗的参数传入，用于更新添加悬浮窗的位置。
     * 
     * @param params
     *            添加悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
    
    /**
     * 更新添加悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }
    
    private String getTopActivityName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return rti.get(0).topActivity.getClassName();
    }
    
    /**
     * 用于获取状态栏的高度。
     * 
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
