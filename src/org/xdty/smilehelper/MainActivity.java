package org.xdty.smilehelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    
    private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.main);
		Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
		startFloatWindow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
				startService(intent);
				FloatWindowSmallView.close = false;
				MyWindowManager.setAddState(false);
				finish();
			}
		});
		
		Button stopFloatWindow = (Button) findViewById(R.id.stop_float_window);
		stopFloatWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyWindowManager.removeSmallWindow(getContext());
                FloatWindowSmallView.close = true;
            }
        });
		
		Button addFloatWindow = (Button) findViewById(R.id.add_float_window);
		addFloatWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
                FloatWindowSmallView.close = false;
                MyWindowManager.setAddState(true);
                finish();
            }
        });
	}
	
	private Context getContext(){
	    return mContext;
	}
}
