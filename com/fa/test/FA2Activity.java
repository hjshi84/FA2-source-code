package com.fa.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;




public class FA2Activity extends TabActivity implements
		TabHost.TabContentFactory {
	/** Called when the activity is first created. */
	////////////////////////////////////////////////////////////////////////////////
    private String databasepath="/data/data/com.fa.test/databases/";//路径
    private String databasefn="data.db";//库名
    private static final String TBL_NAME = "data";//表名
    private SQLiteDatabase database;
    private byte[] buffer;//文件的大小
    ////////////////////////////////////////////////////////////////////////////////
    BroadcastControl receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);//界面
	    //ListView listView=(ListView) findViewById(R.id.listView1);
		receiver = new BroadcastControl(this , this);
        receiver.registBroad(BroadcastControl.FINISH_ACTIVITY);
		//利用sharepreferences储存数据
        if (!Regpart.isregisted(this)){
        	Regpart.regpart(this);
        }
	
		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("upperlimb").setIndicator(null,getResources().getDrawable(R.drawable.upperlimb)).setContent(this));
		th.addTab(th.newTabSpec("lowerlimbs").setIndicator(null,getResources().getDrawable(R.drawable.lowerlimbs)).setContent(this));
		th.addTab(th.newTabSpec("truncus").setIndicator(null,getResources().getDrawable(R.drawable.truncus)).setContent(this));
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem m1=menu.add(getResources().getString(com.fa.test.R.string.FA2Activity_reg));
		MenuItem m2=menu.add(getResources().getString(R.string.FA2Activity_contact));
		m1.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				Regpart.regpart(FA2Activity.this);
				return false;
			}
			
		});
		
		m2.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				Builder build=new Builder(FA2Activity.this);
				build.setTitle(getResources().getString(R.string.FA2Activity_contact));
				build.setMessage(getResources().getString(R.string.FA2Activity_ccontext));
				build.setNegativeButton(getResources().getString(R.string.cancel), null);
//				build.setPositiveButton(getResources().getString(R.string.FA2Activity_sendmss), new OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);    
//				   
//						    
//						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "1825178710@qq.com");    
//					
//						    
//						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "问题反馈");    
//						    
//						
//						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "请输入要反馈的问题");    
//						 
//						emailIntent.setType("text/plain");    
//						
//						startActivity(Intent.createChooser(emailIntent,"Choose Email Client"));    
//					}
//					
//				});
				build.create().show();
				return false;
			}
			
		});
		
		return super.onCreateOptionsMenu(menu);
	}



	public View createTabContent(String tag) {
		ListView lv = new ListView(this);
//	    ListView listView=(ListView) findViewById(R.id.listView1);
//		List<String> list = new ArrayList<String>();
		openDatabase();//打开数据库
		final String strs ="GZXY="+"'"+tag.toString()+"'";//查询条件
		Cursor c = database.query(TBL_NAME,new String[]{"distinct GZBW","SYT_GZBW","_id"},strs,null,null,null,"SYT_GZBW");
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {   //获取数据并填listView
            HashMap<String, Object> map = new HashMap<String, Object>();   
            map.put("GZBW", c.getString(c.getColumnIndex("GZBW")));
            map.put("img", c.getString(c.getColumnIndex("SYT_GZBW")));
            lstImageItem.add(map);
            c.moveToNext();        
        }
        SpecialAdapter adapter = new SpecialAdapter(this, lstImageItem, R.layout.main, new String[]{"img","GZBW"}, new int[]{R.id.img, R.id.name});
//        SimpleAdapter adapter=new SimpleAdapter(this, lstImageItem, R.layout.main, new String[]{"img","GZBW"}, new int[]{R.id.img, R.id.name});
        lv.setAdapter(adapter);//适配
      
//        Resources res = getResources();//背景  
//        Drawable drawable = res.getDrawable(R.drawable.main2);  
//       lv.setBackgroundDrawable(drawable); 
//        lv.setDivider(drawable);
        lv.setFocusable(true);
        lv.setFocusableInTouchMode(true);
        lv.setDividerHeight(2);
        lv.setCacheColorHint(0000000);
        
        lv.setOnItemClickListener(new ItemClickListener());// 添加消息处理
        c.close();database.close();//关数据库    
		return lv;
	}
	
    class ItemClickListener implements OnItemClickListener {   
        @SuppressWarnings("unchecked")   
        public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
        {   
        	if 
        	
        	(Regpart.isregisted(FA2Activity.this))
        	{
            // 在本例中arg2=arg3   
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);   
            // 显示所选Item的ItemText   
            Bundle bundle = new Bundle();//该类用作携带数据
            bundle.putString("GZBW",(String) item.get("GZBW"));
			Intent intent = new Intent(FA2Activity.this,YJFL.class);
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        	}
            }
        } 
    
    
	private SQLiteDatabase openDatabase(){
        try {
        	String DATABASEFN=databasepath+databasefn;//获得li.db绝对路径
            File dir = new File(databasepath);// 如果/data/data/com.fa.test/databases/目录中存在，创建这个目录
            if(!dir.exists())
                dir.mkdir();// 如果在目录中不存在文件，则从res\raw目录中复制这个文件到
            if(!(new File(DATABASEFN)).exists()){ // 获得封装  文件的InputStream对象
              InputStream is =getResources().openRawResource(R.raw.data);
              FileOutputStream fos =new FileOutputStream(DATABASEFN);
              buffer = new byte[8192];
              int count =0;          // 开始复制db文件
             while ((count=is.read(buffer))>0){
                fos.write(buffer, 0, count);
              }
            fos.close();
            is.close();
            }// 打开  目录中的  db文件
             database = SQLiteDatabase.openOrCreateDatabase(DATABASEFN, null);//后面这里第一个参数是路径.
            return null;
            }
            catch (Exception e){
        }
        return null;
        }
	
	protected void onDestroy() {
		super.onDestroy();
		buffer=null;System.gc();
		receiver.unregistBroad();
		}
}
