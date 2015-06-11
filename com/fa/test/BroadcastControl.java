package com.fa.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

	public class BroadcastControl {
		public static String FINISH_ACTIVITY = "finish";
//		public static String FINISH_ACTIVITY_YJFL="finish_YJFL";
//		public static String FINISH_ACTIVITY_gallery="finish_gallery";
//		public static String FINISH_ACTIVITY_Picture="finish_picture";
//		public static String FINISH_ACTIVITY_Load3D="finish_Load3D";
		Context context;
		Activity activity;
		MyBroadcastReceiver receiver;
		public BroadcastControl(Context context,Activity activity){
			this.context = context;
			this.activity = activity;
		}
//此方法是注册广播
	public void registBroad(String actionType){
		receiver = new MyBroadcastReceiver();
//自己定义一个Filter
		IntentFilter filter = new IntentFilter(actionType);
		context.registerReceiver(receiver, filter);
	}
//此方法是注销广播
	public void unregistBroad(){
		context.unregisterReceiver(receiver);
	}
	class MyBroadcastReceiver extends BroadcastReceiver{

//此方法为收到广播后的操作
@Override
	public void onReceive(Context con, Intent intent) {
		String action = intent.getAction();
		if(action.equals(FINISH_ACTIVITY)){
			activity.finish();
		}
	}
}
}

