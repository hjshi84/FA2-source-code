package com.fa.test;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;

public class Regpart {
	static Builder builder;
	static SharedPreferences sp;
	static Time time;
	
	
	public final static boolean regpart(final Context c){
		sp=c.getSharedPreferences("sp", 0);
		final Editor editor=sp.edit();
		time = new Time();
		time.setToNow();
		String t=android.provider.Settings.System.getString(c.getContentResolver(), "android_id");
		if (!isregisted(c))
		{
		builder=new AlertDialog.Builder(c);
		builder.setTitle(c.getResources().getString(com.fa.test.R.string.Regpart_title));
//		builder.setMessage(t);
//		final EditText show=new EditText(c);
//		show.setText("You ID is: "+t+" ;");
		final EditText input=new EditText(c);
		input.setMaxLines(1);
//		input.setText("3753C6403EFB9920");
//		View view=new View(c);
//		 ArrayList<View> aview=new ArrayList<View>();
//		 aview.add(show);
//		 aview.add(input);
//		 view.addTouchables(aview);
//		builder.setView(view);
		builder.setView(input);
		builder.setPositiveButton(c.getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String value;
				if(DES.decrypt(input.getText().toString())!=null)
				{value=DES.decrypt(input.getText().toString()).substring(0, 4);}
				else {value="0";}
				editor.putInt("key",DES.juInteger(value));
				editor.commit();
				info(c);
				return;
			}
		}
				);

		builder.setNegativeButton(c.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		builder.setNeutralButton("register", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				Uri uri=Uri.parse("http://www.baidu.com");
//				Intent i= new Intent(Intent.ACTION_VIEW,uri);
//				c.startActivity(i);
//			}
//		});
		builder.create().show();
		return true;
		}
		
		else{
			return false;
			}
		
		
	}
	public final static boolean isregisted(Context c){
		sp=c.getSharedPreferences("sp", 0);
		time = new Time();
		time.setToNow();
		int a=sp.getInt("", 0);
		return true;
		/*if ((Math.abs(sp.getInt("key", 0)-time.year)>=1)|(sp.getInt("key", 0)==0))
		{
			return false;
		}
		else 
		{
			return true;
		}*/
		
	}
	
	public final static void info(Context c){
		Builder b=new AlertDialog.Builder(c);
		boolean abc=isregisted(c);
		if (isregisted(c)){
			b.setTitle(c.getResources().getString(R.string.Regpart_about_regresult));
			b.setMessage(c.getResources().getString(R.string.Regpart_about_regresult_ok));
			b.setPositiveButton(c.getResources().getString(R.string.ok), null);
		}
		else {
			b.setTitle(c.getResources().getString(R.string.Regpart_about_regresult));
			b.setMessage(c.getResources().getString(R.string.Regpart_about_regresult_failed));
			b.setPositiveButton(c.getResources().getString(R.string.cancel), null);
		}
		b.create().show();
	}
}
