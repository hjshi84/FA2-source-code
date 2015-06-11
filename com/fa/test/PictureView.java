package com.fa.test;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class PictureView extends Activity implements OnGestureListener, OnTouchListener{

	private Builder ad;
	private ViewFlipper myViewFlipper;  
	private GestureDetector myGestureDetector;
	private byte[] buffer;
	private EditText text;
	private int data;
	private String otherinfo;
	private Bundle bd;
	////////////////////////////////////////////////////////////////////////////////
    private String databasepath="/data/data/com.fa.test/databases/";//路径
    private String databasefn="data.db";//库名
    private static final String TBL_NAME = "data";//表名
    private SQLiteDatabase database;
    ////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();//数据传递		
        Bundle b = intent.getExtras();	
        data=(Integer) b.getSerializable("SYT_id");
        otherinfo=(String) b.getSerializable("YJFL");
        bd= new Bundle();
		super.onCreate(savedInstanceState);
		
		ad=new AlertDialog.Builder(this);
		
		getWindow().setContentView(LayoutInflater.from(this).inflate(com.fa.test.R.layout.pvbackground, null));
		myViewFlipper = (ViewFlipper) findViewById(com.fa.test.R.id.myViewFlipper);
		LayoutInflater factory = LayoutInflater.from(PictureView.this); 
		View first=factory.inflate(com.fa.test.R.layout.pictureview,null);
		View next=factory.inflate(com.fa.test.R.layout.pviewnext, null);
		myViewFlipper.addView(first);
		myViewFlipper.addView(next);

//		setContentView(com.fa.test.R.layout.pictureview);
		ImageView image=(ImageView) findViewById(com.fa.test.R.id.imageView1);
		image.setAdjustViewBounds(true);
		image.setImageResource(data);
//		image.setOnLongClickListener(new OnLongClickListener(){
//
//			@Override
//			public boolean onLongClick(View arg0) {
//				// TODO Auto-generated method stub
//
//			Intent intent = new Intent(PictureView.this,Load3DActivity.class);	
//			intent.putExtras(bd);
//			PictureView.this.startActivity(intent);
//			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//				
//				
//				
//				return false;
//			}
//			
//		});
		openDatabase();
		final String strs ="SYT_id="+"'"+data+"'"+"and YJFL="+"'"+otherinfo+"'";//查询条件
		Cursor c = database.query(TBL_NAME,new String[]{"EJFL","SJFL","ZLFA","SYT_FLMC","SYT"},strs,null,null,null,null);
		c.moveToFirst();
		
		EditText FLtext=(EditText) findViewById(com.fa.test.R.id.editText2);
		EditText ZLtext=(EditText) findViewById(com.fa.test.R.id.editText4);
		text=(EditText) findViewById(com.fa.test.R.id.editText6);
		FLtext.setText(c.getString(c.getColumnIndex("EJFL"))+c.getString(c.getColumnIndex("SJFL")));
		ZLtext.setText(c.getString(c.getColumnIndex("ZLFA")));
		if (c.getString(c.getColumnIndex("SYT_FLMC"))==null) {text.setHint("请输入自定义内容");}
		else {text.setText(c.getString(c.getColumnIndex("SYT_FLMC")));}
		bd.putSerializable("3Dpicture", (Serializable)c.getString(c.getColumnIndex("SYT")));
		bd.putSerializable("ZLFA", (Serializable)c.getString(c.getColumnIndex("ZLFA")));
		c.close();
		database.close();
		text.addTextChangedListener(new TextWatcher(){
			 private CharSequence temp;
			 private int selectionStart;
			 private int selectionEnd;
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
	            selectionStart = text.getSelectionStart();
	            selectionEnd = text.getSelectionEnd();
	               if (temp.length() > 512) {
	                   arg0.delete(selectionStart - 1, selectionEnd);
	                   int tempSelection = selectionEnd;
	                    text.setText(arg0);
	                 text.setSelection(tempSelection);//设置光标在最后
	               }
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				temp=arg0;
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
			
		});
		
		
		
		myGestureDetector = new GestureDetector(this);
		myViewFlipper.setFlipInterval(1000);
		myViewFlipper.setLongClickable(true);
		myViewFlipper.setOnTouchListener(this);
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() > e2.getX()) {
			myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, 
                    R.anim.fade_in));
			myViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, 
                    R.anim.fade_out));
			openDatabase();
			ContentValues args = new ContentValues();
			args.put("SYT_FLMC", text.getText().toString().trim());
			database.update(TBL_NAME, args, "SYT_id="+"'"+data+"'"+"and YJFL="+"'"+otherinfo+"'", null);
			database.close();
			myViewFlipper.showNext();
		}
		// 按下时的横坐标小于放开时的横坐标，从左向右滑动
		else if (e1.getX() < e2.getX()) {
			myViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, 
                    R.anim.slide_out_right));
			myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, 
                    R.anim.slide_in_left));
			openDatabase();
			ContentValues args = new ContentValues();
			args.put("SYT_FLMC", text.getText().toString().trim());
			database.update(TBL_NAME, args,"SYT_id="+"'"+data+"'"+"and YJFL="+"'"+otherinfo+"'", null);
			database.close();
			myViewFlipper.showPrevious();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		ad.setTitle(getResources().getString(com.fa.test.R.string.PictureView_trans_mode_title));
		
		ad.setItems(new String[]{getResources().getString(com.fa.test.R.string.PictureView_sinchoose_item1),
				getResources().getString(com.fa.test.R.string.PictureView_sinchoose_item2)}
		, new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1){
				case 0:
					bd.putSerializable("transmode", (Serializable)"0");
					break;
				case 1:
					bd.putSerializable("transmode", (Serializable)"1");
					break;
				}
				Intent intent = new Intent(PictureView.this,Load3DActivity.class);	
				intent.putExtras(bd);
				PictureView.this.startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
			
		});
		ad.create().show();
		
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return myGestureDetector.onTouchEvent(arg1);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem m1=menu.add(getResources().getString(com.fa.test.R.string.PictureView_help));
		m1.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Builder b=new AlertDialog.Builder(PictureView.this);
				b.setTitle(getResources().getString(com.fa.test.R.string.PictureView_help));
				b.setMessage(getResources().getString(com.fa.test.R.string.PictureView_help_content));
				b.setNegativeButton(getResources().getString(com.fa.test.R.string.ok), null);
				b.create().show();
				return false;
			}
			
		});
		
		
		
		
		return super.onCreateOptionsMenu(menu);
	}
	
	private SQLiteDatabase openDatabase(){
        try {
        	String DATABASEFN=databasepath+databasefn;//获得li.db绝对路径
            File dir = new File(databasepath);// 如果/data/data/com.fa.test/databases/目录中存在，创建这个目录
            if(!dir.exists())
                dir.mkdir();// 如果在目录中不存在文件，则从res\raw目录中复制这个文件到
            if(!(new File(DATABASEFN)).exists()){ // 获得封装  文件的InputStream对象
              InputStream is =getResources().openRawResource(com.fa.test.R.raw.data);
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
            
            return database;
            }
            catch (Exception e){
        }
       
        return null;
        }	
    
}
