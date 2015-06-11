package com.fa.test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class YJFL extends TabActivity implements
		TabHost.TabContentFactory {
	/** Called when the activity is first created. */
//	public static int lenghts; 
	private String Classify="123";
	private static String[] images;
	BroadcastControl receiver;
//	private int TotalNumb=0;
	////////////////////////////////////////////////////////////////////////////////
    private String databasepath="/data/data/com.fa.test/databases/";//路径
    private String databasefn="data.db";//库名
    private static final String TBL_NAME = "data";//表名
    private SQLiteDatabase database;
    private  byte[] buffer;//文件
    ////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new BroadcastControl(this , this);
        receiver.registBroad(BroadcastControl.FINISH_ACTIVITY);
        
    	Intent intent = getIntent();//数据传递
		Bundle b = intent.getExtras();
        
		TabHost th = getTabHost();//定义TAB
		openDatabase();//打开数据库
		final String strs ="GZBW="+"'"+b.getString("GZBW")+"'";//查询条件
		Cursor d = database.query(TBL_NAME,new String[]{"distinct FLMC","_id"},strs,null,null,null,null);
//        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
		
        d.moveToFirst();
        
        for (int i = 0; i < d.getCount(); i++) {   //获取数据
        	
    		th.addTab(th.newTabSpec(d.getString(d.getColumnIndex("FLMC")))
    		  .setIndicator(d.getString(d.getColumnIndex("FLMC")),null)
    		  .setContent(this));//填写TAB
    		
            d.moveToNext();
        }
      d.close();if(database!=null)database.close();
        
	}
	public View createTabContent(String tag) {
		ListView lv = new ListView(this);
		database.close();
		openDatabase();//这里是个严重错误，绝对不能用因为在log（1）之前先执行了这里，故同时打开了两个数据库。
		Intent intent = getIntent();//数据传递
		Bundle b = intent.getExtras();
		final String strs ="GZBW="+"'"+b.getString("GZBW")+"'"+"and FLMC="+"'"+tag.toString()+"'";//查询条件
		Cursor c = database.query(TBL_NAME,new String[]{"distinct YJFL","SYT_id","_id"},strs,null,null,null,null);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {   //获取数据并填listView
        	if (Classify.equals(c.getString(c.getColumnIndex("YJFL"))))//排除重复的项目
        	{c.moveToNext();}
        	else{
        		Classify=c.getString(c.getColumnIndex("YJFL"));
//        		TotalNumb=TotalNumb+1;//统计数据
        		HashMap<String, Object> map = new HashMap<String, Object>();   //添加到显示的map
        		map.put("txt", c.getString(c.getColumnIndex("YJFL")));
        		map.put("img", c.getString(c.getColumnIndex("SYT_id")));
        		lstImageItem.add(map);
        		c.moveToNext();
        		}        
        } 
        if (c!=null)c.close();if(database!=null)database.close();
        SpecialAdapter adapter = new SpecialAdapter(this, lstImageItem, R.layout.main, new String[]{"img","txt"}, new int[]{R.id.img, R.id.name});
//        SimpleAdapter adapter = new SimpleAdapter(this, lstImageItem, R.layout.main, new String[]{"img","txt"}, new int[]{  R.id.img, R.id.name});
	    lv.setAdapter(adapter);//适配
	    
//        Resources res = getResources();  
//        Drawable drawable = res.getDrawable(R.drawable.main2);  
//        lv.setBackgroundDrawable(drawable); 
//        lv.setDivider(drawable);
        lv.setDividerHeight(2);
        lv.setFocusable(true);
        lv.setFocusableInTouchMode(true);
        lv.setCacheColorHint(0000000);
	    //关数据库
	    lv.setOnItemClickListener(new OnItemClickListener()//listView点击事件
	    {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3) 
			{
				HashMap<String, Object> item =(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				openDatabase();
				final String strs2 =strs+"and YJFL="+"'"+item.get("txt").toString()+"'";//查询条件
				final String SelectPicture= item.get("img").toString();
				int num = 0;
				Cursor e = database.query(TBL_NAME,new String[]{"SYT_id"},strs2,null,null,null,null);
				images=new String[e.getCount()];
		        e.moveToFirst();
		        for (int i = 0; i < e.getCount(); i++) {   //获取图片数据
		        	images[i]=e.getString(e.getColumnIndex("SYT_id"));
		        	if (SelectPicture.equals(e.getString(e.getColumnIndex("SYT_id"))))
		        		num=i;
		            e.moveToNext();
		        }
		        e.close();if(database!=null)database.close();//关数据库
	            Bundle bundle = new Bundle();//该类用作携带数据
	            bundle.putStringArray("SYT_id", images);
	            bundle.putSerializable("OtherInfo", item.get("txt").toString());
	            bundle.putInt("num",num);
				Intent intent = new Intent(YJFL.this,gallery.class);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

			}
	    }
	    );    
	    return lv;
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