package com.fa.test;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FaceActivity extends Activity {	
    /** Called when the activity is first created. */
	 BroadcastControl receiver;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face);
        receiver = new BroadcastControl(this , this);
        receiver.registBroad(BroadcastControl.FINISH_ACTIVITY);
        new Timer().schedule(new TimerTask()
        { 
        	public void run()
        	{ 
            	Intent intent = new Intent(FaceActivity.this,FA2Activity.class);
        	    startActivity(intent);
        	    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        	    finish();
		    }
        }, 2000); 

	}
	protected void onDestroy() {
		super.onDestroy();
		receiver.unregistBroad();
		}
}    