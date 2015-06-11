package com.fa.test;

import java.io.IOException;  
import java.io.InputStream;  

import javax.microedition.khronos.egl.EGL;

import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.Overlay;

import android.app.Activity;  
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;  
import android.content.res.Resources;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;  
import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.widget.Toast;

public class Load3DActivity extends Activity implements OnGestureListener{
	


	private float mPreviousX;
    private float mPreviousY;
    private float mAngleX,mAngleY,mzoom;
    private boolean isZoom = false;
    private float oldDist;    
	private GLSurfaceView glView;  
	private MyRenderer mr;  
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private boolean isrender=false;
	private Bundle b;
	private Builder builder1;
	private String data;
	private Handler mHandler;  
	private GestureDetector detector;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {  
    	Intent intent = getIntent();//数据传递
		b = intent.getExtras();
        super.onCreate(savedInstanceState);  
        setContentView(com.fa.test.R.layout.row);
        data=b.getSerializable("3Dpicture")+".3DS";
        LoadImage.loadi(getResources());  // 加载图片  
        new LoadAssets(getResources());   // 加载文件    
        glView = new GLSurfaceView(this);   
        mr = new MyRenderer();  
        mr.setcount(0);//判断手势是否触发
        mr.setmode(0);//判断手势动作；
        glView.setRenderer(mr);  
        mHandler=new Handler(){
          	 public void handleMessage(Message msg){
          		 switch (msg.what){
          		 case 123:         	        
          			isrender=true;
          			setContentView(glView);
          			break;
          		 
          		 }
          	 }
          };
          new Thread() {
              public void run() {
                    mr.load(data,Integer.valueOf((String) b.getSerializable("transmode")));
            	 
    				Message message = new Message();  
    	          	message.what=123;// TODO
    	          	mHandler.sendMessage(message);
              }
          }.start(); 


        detector=new GestureDetector(this);
   
        builder1=new AlertDialog.Builder(this);
    }
    
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		MenuItem m5=menu.add(getResources().getString(R.string.Load3D_Op));
		m5.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem mi){
				builder1.setTitle(getResources().getString(R.string.Load3D_Op));
				builder1.setMessage(getResources().getString(R.string.Load3D_Op_content));
				builder1.setNegativeButton(getResources().getString(R.string.cancel), null);
				builder1.create().show();
				return true;
			}
		});
		MenuItem m6=menu.add(getResources().getString(R.string.Exit_mode));
		m6.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem mi){
				finish();
				return true;}
		});
		
	
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isrender){
			switch(keyCode){
			case KeyEvent.KEYCODE_HOME:
		        return true;
		    case KeyEvent.KEYCODE_BACK:
		        return true;
		    case KeyEvent.KEYCODE_CALL:
		        return true;
		    case KeyEvent.KEYCODE_SYM:
		        return true;
		    case KeyEvent.KEYCODE_VOLUME_DOWN:
		        return true;
		    case KeyEvent.KEYCODE_VOLUME_UP:
		        return true;
		    case KeyEvent.KEYCODE_STAR:
		        return true;
			}
		}
	
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	public boolean onTouchEvent(MotionEvent e) {
		mr.setcount(1);
		float newDist=0;
    	float x = e.getX();
        float y = e.getY();
		  switch (e.getAction() & MotionEvent.ACTION_MASK) {  
		  
		    case MotionEvent.ACTION_CANCEL:
		    	mr.setunmuber(-1);
		    	break;
	        
	        case MotionEvent.ACTION_DOWN:   
	            break;  
	            
	        case MotionEvent.ACTION_POINTER_UP:  
	            isZoom = false;  
	            break;  
	            
	        //指非第一个点按下 
	        case MotionEvent.ACTION_POINTER_DOWN:  
	            oldDist = spacing(e);  
	            
	            isZoom = true;  
	            break;  
	            
	        case MotionEvent.ACTION_MOVE:  
        		float dx = x - mPreviousX;
	            float dy = y - mPreviousY;
	            if (isZoom) {  
	            	if (e.getPointerCount()==2){
	            		mr.setmode(1);
	            		newDist = spacing(e);                   
	            		if (oldDist>10)
	            			mzoom=newDist/oldDist;
	            		else mzoom=1;
	            		mr.settranslate(mzoom, 0, 1);
	            		}
	            	else if (e.getPointerCount()==3){
	            		mr.setmode(2);
        
	  	              	mAngleX = dx * TOUCH_SCALE_FACTOR;
	  	              	mAngleY = dy * TOUCH_SCALE_FACTOR;
	  	              	mr.settranslate(mAngleX, mAngleY, 2);
	            	}
					
	            }   
	            else{
	            	if(e.getPointerCount()==1)mr.setmode(0);

	              
	              mAngleX = dx * TOUCH_SCALE_FACTOR;
	              mAngleY = dy * TOUCH_SCALE_FACTOR;
	              mr.settranslate(mAngleX, mAngleY, 2);
	            }
	              
	  
	            break;  
	        }  
		  	oldDist=newDist;
			mPreviousX = x;
          	mPreviousY = y;
          	
          	try {
    			Thread.sleep(15);
    		} catch (Exception ex) {
    			// Doesn't matter here...
    		}
          	
		return detector.onTouchEvent(e);
	}
	   private float spacing(MotionEvent event) {  
	        float x = event.getX(0) - event.getX(1);  
	        float y = event.getY(0) - event.getY(1);  
	        return (float) Math.sqrt(x * x + y * y);  
	    }  
    
//    public boolean onTouchEvent(MotionEvent e) {
//    	mr.count=1;
//        float x = e.getX();
//        float y = e.getY();
//      int pointcount=e.getPointerCount();
//      if(canmodify==mr.mode+1){
//    	  if (pointcount==1){mr.mode=0;}else if(pointcount==2){mr.mode=1;}else if (pointcount==3){mr.mode=2;}
//    	  canmodify=0;
//      }
//    	  switch (e.getAction()) {
//          case MotionEvent.ACTION_MOVE:
//  
//              float dx = x - mPreviousX;
//              float dy = y - mPreviousY;
//             
//              switch (mr.mode)
//              {case 1:
//              if (((x - glView.getWidth() / 2)*dx>=0)&((y-glView.getHeight()/2)*dy>=0)){dx=-Math.abs(dx);dy=-Math.abs(dy);}
//              else{dx=Math.abs(dx);dy=Math.abs(dy);}
//              }
//              
//              mr.mAngleX += dx * TOUCH_SCALE_FACTOR;
//              mr.mAngleY += dy * TOUCH_SCALE_FACTOR;
//
//          case MotionEvent.ACTION_UP:
//        	  canmodify=mr.mode+1;
//    	 }
//    	  if (canmodify==0){mr.mAngleY=0;mr.mAngleX=0;}
//    	   
//    	  mPreviousX = x;
//          mPreviousY = y;
//    
//    return detector.onTouchEvent(e);
//    }
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		mr.setunmuber(mr.JudgeObjects(e.getX(), e.getY()));
		return false;
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(),(CharSequence) b.getSerializable("ZLFA") , Toast.LENGTH_LONG).show();		
	//	Toast.makeText(getBaseContext(),String.valueOf( mr.number) , Toast.LENGTH_LONG).show();		
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onSingleTapUp(MotionEvent e) {
		
		// TODO Auto-generated method stub
		return false;
	}  

    protected void onDestroy() {
		super.onDestroy();
		mr.destroycache();
		glView.destroyDrawingCache();
		glView=null;builder1=null;mHandler=null;detector=null;
		LoadImage.background.recycle();
		LoadImage.bone.recycle();
		System.gc();
		
		}
}  
  
// 载入纹理图片  
  
class LoadImage {  
  
    public static  Bitmap bone;
	public static Bitmap background;  
  
    public static  void loadi(Resources res) {  
  
        bone = BitmapFactory.decodeResource(res, com.fa.test.R.drawable.bone);  
        background=BitmapFactory.decodeResource(res,com.fa.test.R.drawable.zzz);//背景
//        //作图
//        Canvas c=new Canvas(background);
//        Paint mpaint=new Paint();
//        mpaint.setColor(Color.RED);
//        mpaint.setTextSize(14);       
//        c.drawText("施黄骏", 20, 20,mpaint);
//        c.drawBitmap(background, 0, 0, null);
       
    }  
  
}  
  
// 载入Assets文件夹下的文件  
  
class LoadAssets {  
  
    public static Resources res;  
  
    public LoadAssets(Resources resources) {  
  
        res = resources;  
  
    }  
  
    public static  InputStream loadf(String fileName) {  
    	
        AssetManager am = LoadAssets.res.getAssets();  
  
        try {   
        	
            return am.open(fileName, AssetManager.ACCESS_UNKNOWN);  
  
        } catch (IOException e) {  
        	e.printStackTrace();
        	
        	
        	try {
				return am.open("ccddee.3DS", AssetManager.ACCESS_UNKNOWN);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			} 
  
        }  
  
    }  

    
    
}
