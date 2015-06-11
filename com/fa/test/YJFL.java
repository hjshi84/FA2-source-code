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
    private String databasepath="/data/data/com.fa.test/databases/";//·��
    private String databasefn="data.db";//����
    private static final String TBL_NAME = "data";//����
    private SQLiteDatabase database;
    private  byte[] buffer;//�ļ�
    ////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new BroadcastControl(this , this);
        receiver.registBroad(BroadcastControl.FINISH_ACTIVITY);
        
    	Intent intent = getIntent();//���ݴ���
		Bundle b = intent.getExtras();
        
		TabHost th = getTabHost();//����TAB
		openDatabase();//�����ݿ�
		final String strs ="GZBW="+"'"+b.getString("GZBW")+"'";//��ѯ����
		Cursor d = database.query(TBL_NAME,new String[]{"distinct FLMC","_id"},strs,null,null,null,null);
//        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
		
        d.moveToFirst();
        
        for (int i = 0; i < d.getCount(); i++) {   //��ȡ����
        	
    		th.addTab(th.newTabSpec(d.getString(d.getColumnIndex("FLMC")))
    		  .setIndicator(d.getString(d.getColumnIndex("FLMC")),null)
    		  .setContent(this));//��дTAB
    		
            d.moveToNext();
        }
      d.close();if(database!=null)database.close();
        
	}
	public View createTabContent(String tag) {
		ListView lv = new ListView(this);
		database.close();
		openDatabase();//�����Ǹ����ش��󣬾��Բ�������Ϊ��log��1��֮ǰ��ִ���������ͬʱ�����������ݿ⡣
		Intent intent = getIntent();//���ݴ���
		Bundle b = intent.getExtras();
		final String strs ="GZBW="+"'"+b.getString("GZBW")+"'"+"and FLMC="+"'"+tag.toString()+"'";//��ѯ����
		Cursor c = database.query(TBL_NAME,new String[]{"distinct YJFL","SYT_id","_id"},strs,null,null,null,null);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {   //��ȡ���ݲ���listView
        	if (Classify.equals(c.getString(c.getColumnIndex("YJFL"))))//�ų��ظ�����Ŀ
        	{c.moveToNext();}
        	else{
        		Classify=c.getString(c.getColumnIndex("YJFL"));
//        		TotalNumb=TotalNumb+1;//ͳ������
        		HashMap<String, Object> map = new HashMap<String, Object>();   //��ӵ���ʾ��map
        		map.put("txt", c.getString(c.getColumnIndex("YJFL")));
        		map.put("img", c.getString(c.getColumnIndex("SYT_id")));
        		lstImageItem.add(map);
        		c.moveToNext();
        		}        
        } 
        if (c!=null)c.close();if(database!=null)database.close();
        SpecialAdapter adapter = new SpecialAdapter(this, lstImageItem, R.layout.main, new String[]{"img","txt"}, new int[]{R.id.img, R.id.name});
//        SimpleAdapter adapter = new SimpleAdapter(this, lstImageItem, R.layout.main, new String[]{"img","txt"}, new int[]{  R.id.img, R.id.name});
	    lv.setAdapter(adapter);//����
	    
//        Resources res = getResources();  
//        Drawable drawable = res.getDrawable(R.drawable.main2);  
//        lv.setBackgroundDrawable(drawable); 
//        lv.setDivider(drawable);
        lv.setDividerHeight(2);
        lv.setFocusable(true);
        lv.setFocusableInTouchMode(true);
        lv.setCacheColorHint(0000000);
	    //�����ݿ�
	    lv.setOnItemClickListener(new OnItemClickListener()//listView����¼�
	    {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3) 
			{
				HashMap<String, Object> item =(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				openDatabase();
				final String strs2 =strs+"and YJFL="+"'"+item.get("txt").toString()+"'";//��ѯ����
				final String SelectPicture= item.get("img").toString();
				int num = 0;
				Cursor e = database.query(TBL_NAME,new String[]{"SYT_id"},strs2,null,null,null,null);
				images=new String[e.getCount()];
		        e.moveToFirst();
		        for (int i = 0; i < e.getCount(); i++) {   //��ȡͼƬ����
		        	images[i]=e.getString(e.getColumnIndex("SYT_id"));
		        	if (SelectPicture.equals(e.getString(e.getColumnIndex("SYT_id"))))
		        		num=i;
		            e.moveToNext();
		        }
		        e.close();if(database!=null)database.close();//�����ݿ�
	            Bundle bundle = new Bundle();//��������Я������
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
        	String DATABASEFN=databasepath+databasefn;//���li.db����·��
            File dir = new File(databasepath);// ���/data/data/com.fa.test/databases/Ŀ¼�д��ڣ��������Ŀ¼
            if(!dir.exists())
                dir.mkdir();// �����Ŀ¼�в������ļ������res\rawĿ¼�и�������ļ���
            if(!(new File(DATABASEFN)).exists()){ // ��÷�װ  �ļ���InputStream����
              InputStream is =getResources().openRawResource(R.raw.data);
              FileOutputStream fos =new FileOutputStream(DATABASEFN);
              buffer = new byte[8192];
              int count =0;          // ��ʼ����db�ļ�
             while ((count=is.read(buffer))>0){
                fos.write(buffer, 0, count);
              }
            fos.close();
            is.close();
            }// ��  Ŀ¼�е�  db�ļ�
             database = SQLiteDatabase.openOrCreateDatabase(DATABASEFN, null);//���������һ��������·��.
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