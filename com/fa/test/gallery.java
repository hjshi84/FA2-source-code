package com.fa.test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class gallery extends Activity implements OnItemSelectedListener{
	////////////////////////////////////////////////////////////////////////////////
    private String databasepath="/data/data/com.fa.test/databases/";//·��
    private String databasefn="data.db";//����
    private static final String TBL_NAME = "data";//����
    private SQLiteDatabase database;
    ////////////////////////////////////////////////////////////////////////////////
//	private ImageSwitcher mSwitcher;
//	private Integer[] mThumbIds;
	private Integer[] mImageIds;
	private Bundle bd;
	private int num=0;
	private byte[] buffer;
	private static String[] images;
	private String otherinfo;
	private ImageAdapter adapter;
	BroadcastControl receiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		receiver = new BroadcastControl(this , this);
        receiver.registBroad(BroadcastControl.FINISH_ACTIVITY);
		setContentView(R.layout.gallery);
		Intent intent = getIntent();//���ݴ���		
        Bundle b = intent.getExtras();		
	 	images =b.getStringArray("SYT_id");  
	 	otherinfo=(String) b.getSerializable("OtherInfo");
//		mThumbIds=new Integer[images.length];
		mImageIds=new Integer[images.length];
        num=b.getInt("num");
		bd= new Bundle();

//        for(int i = 0; i < images.length; i++){
//        	   try{
//        		   mThumbIds[i] = Integer.parseInt(images[i].trim());
//        	   }catch (NumberFormatException nbFmtExp){
//        		   mThumbIds[i] = 0;
//        	   }
//        	  }
        for(int i = 0; i < images.length; i++){    	   
     	       try{
     		       mImageIds[i] = Integer.parseInt(images[i].trim());
     	       }catch (NumberFormatException nbFmtExp){
     		       mImageIds[i] = 0;
     	       }
     	      }
        adapter = new ImageAdapter(this, mImageIds);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        adapter.createReflectedImages( dm.widthPixels,dm.heightPixels);
        
//		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
//		mSwitcher.setFactory(this);
//		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
//		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.gallery);
        galleryFlow.setAdapter(adapter);
//		g.setAdapter(new ImageAdapter(this));//���������
		galleryFlow.setOnItemSelectedListener(this);//��Ӽ���
//		ImageSwitcher im=(ImageSwitcher) findViewById(R.id.switcher);
	
		galleryFlow.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ActivityManager.MemoryInfo meminfo=new ActivityManager.MemoryInfo();
				// TODO Auto-generated method stub
				if (meminfo.lowMemory)
				{		
					Intent i = new Intent(BroadcastControl.FINISH_ACTIVITY);
					sendBroadcast(i);
				}
				Intent intent = new Intent(gallery.this,PictureView.class);	
				intent.putExtras(bd);
				gallery.this.startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
//		������ά��ʾ
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				ActivityManager.MemoryInfo meminfo=new ActivityManager.MemoryInfo();
//				// TODO Auto-generated method stub
//				if (meminfo.lowMemory)
//				{		
//					Intent i = new Intent(BroadcastControl.FINISH_ACTIVITY);
//					sendBroadcast(i);
//				}
//				Intent intent = new Intent(gallery.this,Load3DActivity.class);	
//				intent.putExtras(bd);
//				gallery.this.startActivity(intent);
//				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//			}
			
			});
	}

    


//	public class ImageAdapter extends BaseAdapter {
//		public ImageAdapter(Context c) {mContext = c;}
//		public int getCount() {return mThumbIds.length;}
//		public Object getItem(int position) {return position;}
//		public long getItemId(int position) {return position;}
//		public View getView(int position, View convertView, ViewGroup parent) {//���View����
//			ImageView i = new ImageView(mContext);
//			i.setImageResource(mThumbIds[position]);
//			i.setAdjustViewBounds(true);
//			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			i.setBackgroundColor(Color.rgb(255, 255, 255));
//			return i;
//		}
//		private Context mContext;
//	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View v, int position,long id) {
		//����,��ͼ
//		mSwitcher.setImageResource(mImageIds[position]);
		openDatabase();
		final String strs ="SYT_id="+"'"+mImageIds[position].toString()+"'"+"and YJFL="+"'"+otherinfo+"'";//��ѯ����
		Cursor c = database.query(TBL_NAME,new String[]{"EJFL","SJFL","ZLFA","SYT"},strs,null,null,null,null);
        c.moveToFirst();
        TextView EJFLT =(TextView) findViewById(R.id.EJFLT);
        EJFLT.setText(c.getString(c.getColumnIndex("EJFL")));
        TextView SJFLT =(TextView) findViewById(R.id.SJFLT);
        TextView SJFL =(TextView) findViewById(R.id.SJFL);
        if (c.getString(c.getColumnIndex("SJFL"))==null){SJFLT.setVisibility(8);SJFL.setVisibility(8);}
        else
        	{
        	if (c.getString(c.getColumnIndex("SJFL")).equals(c.getString(c.getColumnIndex("EJFL")))){
        	SJFLT.setVisibility(8);SJFL.setVisibility(8);
        		}
        else {
        	
        	SJFLT.setText(c.getString(c.getColumnIndex("SJFL")));
        	}
        }
        TextView ZLFAT =(TextView) findViewById(R.id.ZLFAT);
        ZLFAT.setText(c.getString(c.getColumnIndex("ZLFA")));
        bd.putSerializable("FL", (Serializable) SJFLT.getText());
        bd.putSerializable("ZLFA", (Serializable) ZLFAT.getText());
        bd.putSerializable("3Dpicture", (Serializable)c.getString(c.getColumnIndex("SYT")));
        bd.putSerializable("YJFL", otherinfo);
        bd.putSerializable("SYT_id", mImageIds[position]);
        if (c!=null)c.close();if(database!=null)database.close();//�����ݿ�
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

//	@Override
//	public View makeView() {//ΪImageView���ò��ָ�ʽ
//		ImageView i = new ImageView(this);
//		i.setBackgroundColor(Color.rgb(255, 255, 255));
//	
//		i.setScaleType(ImageView.ScaleType.FIT_XY);
//		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		return i;
//	}
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
            
            return database;
            }
            catch (Exception e){
        }
       
        return null;
        }	
	protected void onDestroy() {
		super.onDestroy();
		buffer=null;System.gc();
		receiver.unregistBroad();
		adapter.destroycache();
		}






}




