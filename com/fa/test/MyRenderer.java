package com.fa.test;

import javax.microedition.khronos.egl.EGLConfig;  
import javax.microedition.khronos.opengles.GL10;  

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.FrameBuffer;  
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;  
import com.threed.jpct.Matrix;  
import com.threed.jpct.Object3D;  
import com.threed.jpct.RGBColor;  
import com.threed.jpct.SimpleVector;  
import com.threed.jpct.Texture;  
import com.threed.jpct.TextureManager;  
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.Overlay;
//import com.threed.jpct.example.R;  
//import com.threed.jpct.util.BitmapHelper;  
import android.R.integer;
import android.opengl.GLSurfaceView.Renderer;  
import android.util.Log;
public class MyRenderer implements Renderer{
	public String text;
	
	private int transmode;
	private float mAngleX;
	private float mAngleY;
	private float mzoom;
	private int count,mode,number=-1;
    static private Object3D[] thing;  
    private Overlay overlay;
//    public Object3D compared;
    private World world;  
    // FrameBuffer����  
    private FrameBuffer fb;  
    private int objnumber=0;
    //private SimpleVector midcenter;
	private Object3D o3d=null; 
    // Object3D  
    private Light sun;
    private Texture texture,texture1;
 
    public void setcount(int _count){
    	count=_count;
    }
    public void setmode(int _mode){
    	mode=_mode;
    }
    public void setunmuber(int _number){
    	number=_number;
    }
    
    public void settranslate(float arg0,float arg1,int ismzoomornot){
    	switch (ismzoomornot){
    	case 1:
    		mzoom=arg0;
    		break;
    	case 2:
    		mAngleX=arg0;
    		mAngleY=arg1;
    		break;
    	}
    }
    
    public void onDrawFrame(GL10 gl) {  
        // �Ժ�ɫ���������Ļ  
        fb.clear(RGBColor.BLACK);  
        // �����ж���ν��б任���ƹ����  
        world.renderScene(fb);  
        if (overlay==null){
        overlay=new Overlay(world,fb,"background");
        overlay.setDepth(20);
        overlay.setTransparency(-1);}
       
//        for(Object3D i:thing){
//        	if (number>=0){
//        	if (i==thing[number]){}
//        	else 
//        	{
//        		SimpleVector trans=i.checkForCollisionSpherical(new SimpleVector(mAngleY*3.14f/180,-mAngleX*3.14f/180,0f), 0.5f);
//        		i.translate(trans);
//        	}
//        }
//        }
        
        
        // �����Ѿ���renderScene������fb  
        if(count==1){
        	
        	if (o3d==null){
        //����任����          
        	if (number!=-1) {
        		switch (mode){
        			case 0: 
        				thing[number].rotateY(-mAngleX);
        				thing[number].rotateX(mAngleY);
        				mAngleX=0;mAngleY=0;
        				break;
        		
        			case 1:
        				thing[number].setScale(thing[number].getScale()*((mzoom-1)/5+1));
        				mzoom=1;
        				break;
        			
        			case 2:
        				thing[number].translate(mAngleX/10, mAngleY/10, 0);
        				mAngleX=0;mAngleY=0;
        				break;
        	
        		}
        	}else{
        			
//        			thing[1].translate(-midcenter.x, -midcenter.y, 0);
//        			thing[1].rotateY(-mAngleX*3.14f/180);
//        			thing[1].rotateX(mAngleY*3.14f/180);
//        			thing[1].translate(midcenter.x, midcenter.y, 0);
//        			thing[2].translate(-midcenter.x, -midcenter.y, 0);
//        			thing[2].rotateY(-mAngleX*3.14f/180);
//        			thing[2].rotateX(mAngleY*3.14f/180);
//        			thing[2].translate(midcenter.x, midcenter.y, 0);
          		
        	}
        	}
        	else{
        		switch (mode){
    			case 0: 
    				o3d.rotateY(-mAngleX);
    				o3d.rotateX(mAngleY);
    				mAngleX=0;mAngleY=0;
    				break;
    		
    			case 1:
    				o3d.setScale(o3d.getScale()*((mzoom-1)/5+1));
    				mzoom=1;
    				break;
    			
    			case 2:
    				o3d.translate(mAngleX/10, mAngleY/10, 0);
    				mAngleX=0;mAngleY=0;
    				break;
        	}
        	}
        	count-=1;
        };
       	//��תthing�ӽ�
        
        
        
        
        //��ʼ��
       
        
        sun.setPosition( world.getCamera().getPosition());
        
        world.draw(fb);  
        // ��Ⱦ��ʾͼ��  
        
        fb.display();  
    }  
  
    public void onSurfaceChanged(GL10 gl, int width, int height) {  

        if (fb != null) {  
            fb = null;  
        } 
        
        fb = new FrameBuffer(gl, width, height);  
    
        
    }  
  
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {  
    
    }  
    
    public void load(String data,int _transmode)
    {
    	transmode=_transmode;
    	 world = new World();  
    	 // ���û�����  
         world.setAmbientLight(80,80,80);  
         TextureManager.getInstance().flush();
         // ѭ�����Ѵ���Texture���µ����ִ���TextureManager��  
         loadModel(data,1,_transmode);
         
   	     texture1 = new Texture(BitmapHelper.rescale(LoadImage.bone,64, 64));        
         TextureManager.getInstance().addTexture("bone", texture1); 
 //        LoadImage.bone.recycle();LoadImage.bone=null;
         texture=new Texture(BitmapHelper.rescale(LoadImage.background,64, 64));
         TextureManager.getInstance().addTexture("background", texture);  
//       LoadImage.background.recycle();
//       LoadImage.background=null;
//         texture.removeAlpha();
         // ΪObject3D������������     
         if (judgemode(_transmode)){ 
        
        	 for(int i=0;i<objnumber;i++)
        	 { 
       // ��Ⱦ����ǰ���еĲ���  
        		 
        		 thing[i].build();  
        		 thing[i].calcTextureWrapSpherical();
        		 thing[i].setTexture("bone");
         
        
         // ��thing���Object3D������          
        		 world.addObject(thing[i]);  
        	 }
         }
         else{
        	 o3d.calcTextureWrapSpherical();
        	 o3d.setTexture("bone");
        	 world.addObject(o3d);
         }
         // ��������ϵ  
         world.getCamera().setPosition(0, 0, 10);  
         // ��world��Ӧ�ø����ú��˵�����ϵ  
         world.getCamera().lookAt(SimpleVector.ORIGIN);
         //thing[1].getTransformedCenter()
         sun=new Light(world);
         sun.setIntensity(160, 160, 160);
         sun.setPosition( world.getCamera().getPosition());
         Loader.clearCache();
//    	transmode=_transmode;
//        TextureManager tm=TextureManager.getInstance();
//        tm.flush();
//        world = new World();   
//         // ѭ�����Ѵ���Texture���µ����ִ���TextureManager��  
//         
//         loadModel(data,1,transmode);   
//         //   world.setAmbientLight(80,80,80); 
////            overlay=new Overlay(world,fb,"background");
////            overlay.setDepth(20);
//       //     overlay.setTransparency(-1);
//            if (judgemode(transmode)){ 
//           	 for(int i=0;i<objnumber;i++)
//           	 { 
//          // ��Ⱦ����ǰ���еĲ���        		 
//           		 thing[i].build();  
//           		 thing[i].calcTextureWrapSpherical();
//           		 thing[i].setTexture("bone");
//           		 
//           		 world.addObject(thing[i]);  
//           		 thing[i].strip();
//           	 }
//            }
//            else{
//           	 	o3d.calcTextureWrapSpherical();
//           	 	o3d.setTexture("bone");
//           	 	world.addObject(o3d);
//            }
//            
//            
//            
//            
//            // ��������ϵ  
//            world.getCamera().setPosition(0, 0, 10);  
//            // ��world��Ӧ�ø����ú��˵�����ϵ  
//            world.getCamera().lookAt(SimpleVector.ORIGIN);
//            //thing[1].getTransformedCenter()
//            sun=new Light(world);
//            sun.setIntensity(160, 160, 160);
//            sun.setPosition( world.getCamera().getPosition());
//            MemoryHelper.compact();
//   	     texture1 = new Texture(BitmapHelper.rescale(LoadImage.bone,64, 64));        
//         tm.addTexture("bone", texture1); 
// //        LoadImage.bone.recycle();LoadImage.bone=null;
//         texture=new Texture(BitmapHelper.rescale(LoadImage.background,64, 64));
//         tm.addTexture("background", texture);  
////       LoadImage.background.recycle();
////       LoadImage.background=null;
////         texture.removeAlpha();
//         // ΪObject3D������������     
//         
//         Loader.clearCache();
    }
    
 
    public void loadModel(String filename, float scale,int transmode) {  
        // �������3ds�ļ����浽model������  
    	
        thing = Loader.load3DS(LoadAssets.loadf(filename), scale); 
        // ȡ��һ��3ds�ļ�  
        // ��ʱ����temp  
        // ����model����  
        if (judgemode(transmode)){
        	
        	objnumber=thing.length;
        	
        	for (int i = 0; i < thing.length; i++) {  
        		thing[i].rotateX(-(float) Math.PI / 2);
        	}

        }
        else{
        	o3d=new Object3D(0);
         	for (int i = 0; i < thing.length; i++) {  
                // ��temp����model�����е�ĳһ��  
         		//	if (model[i]==null) continue;
                // ����temp������Ϊ origin (0,0,0)  
         		
            	//	thing[i].setCenter(SimpleVector.ORIGIN);  
                // ��x����ת����ϵ������������ϵ(jpct-ae�������е�y,x�Ƿ���)  
                //thing[i].rotateX((float) (-.5 * Math.PI));  
                // ʹ����ת����ָ���˶�����ת�����ԭʼ����  
                //thing[i].rotateMesh();  
                // new һ����������Ϊ��ת����  
            	//	thing[i].setRotationMatrix(new Matrix());  
            		
         		//  thing[i].compile();
                // �ϲ�o3d��temp  
         		//  objnumber++;
         	
         		o3d = Object3D.mergeObjects(o3d, thing[i]);  
         		o3d.compile();
            	}
         	//SimpleVector aa =o3d.getTransformedCenter();
            	objnumber=1;
        	
        }
        
 
      
    //    thing=model;

        // ����o3d����  
        //objnumber=0;
        return;
         
    }  
    
    public String text(float x,float y){
    	SimpleVector org=Interact2D.reproject2D3D(world.getCamera(), fb,(int)x, (int)y,10);
    
   // 	SimpleVector org=thing[2].getTransformedCenter();
    	text=org.x+" "+org.y+" "+org.z+";";
    	return text;
    }
    
    
    // ����ģ��  
    public int JudgeObjects(float x,float y){
		SimpleVector org=Interact2D.reproject2D3D(world.getCamera(), fb,(int)x, (int)y,10);
		float xx=-org.x;float yy=org.y-2;
		org.set(0, 0, 10);
		SimpleVector dr=new SimpleVector(xx,yy,-10);
		float[] distance=new float[objnumber];
    	for(int i=0;i<objnumber;i++){
//    		SimpleVector sv=Interact2D.projectCenter3D2D(world.getCamera(),fb,thing[i]);//�����ֵ�ĵط��������������Ƚ϶����С;
//    		float a=thing[i].getScale();
//    		if ((sv.x-x)*(sv.x-x)+(sv.y-y)*(sv.y-y)<10000) return i;
//    		thing[i].calcBoundingBox();

//   	if (thing[i].sphereIntersectsAABB(org, 2))return i;//Բ����ײ����
    		
    		
    		distance[i]=(thing[i].rayIntersectsAABB(org, dr)==Object3D.RAY_MISSES_BOX)?65535.0f:thing[i].rayIntersectsAABB(org, dr);
    		
    		
    		
//    	if (thing[i].rayIntersectsAABB(org, dr)!=Object3D.RAY_MISSES_BOX)
//    		return i	;   //������ײ����		
    	}
    	
    	float MINDIST=65535.0f;int MIN=-1,num=0;
    	
    	for (float i:distance){
    		if(i<MINDIST){
    			MINDIST=i;
    			MIN=num;
    		}
    		num++;
    	}
    	
    	return MIN;
 
    }
    
    private boolean judgemode(int transmode){
    	if (transmode==0) return false;
    	else return true;
    }

    public void destroycache(){
    	overlay.dispose();fb.dispose();
		thing=null;world.dispose();
		
		TextureManager.getInstance().flush();
    }
}