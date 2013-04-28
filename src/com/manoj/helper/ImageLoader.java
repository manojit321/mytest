package com.manoj.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.manoj.macawplayer.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	private Activity activity;
	public static boolean pause = false;
    private Map<ImageView, Uri> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, Uri>());
    ExecutorService executorService; 
    
    public ImageLoader(Context context,Activity activity){
        executorService=Executors.newFixedThreadPool(10);
        this.activity = activity;
    }
    
    final int stub_id = R.drawable.adele;
    public void DisplayImage(Uri url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap;
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
    }
        
    private void queuePhoto(Uri url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(Uri url) 
    {
        //File f=fileCache.getFile(url);
        //f = new File(url);
        //from SD cache
        Bitmap bitmap;
          try{
        	  bitmap = MediaStore.Images.Media.getBitmap(
        		activity.getContentResolver(),url);
        if(bitmap!=null)
            bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
        return bitmap;
        }catch(Exception e){
        	Log.i("", e.toString());
        	return null;
        }
        //from web
        /*try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }*/
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public Uri url;
        public ImageView imageView;
        public PhotoToLoad(Uri u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
        	if(!pause){
        	try{
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        	}catch(Exception e){
        		Log.i("", e.toString());
        	}
        	}
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        Uri tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
        	if(!pause){
        	try{
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        	}catch(Exception e){
        		Log.i("", e.toString());
        	}
        	}
        }
    }

    public void clearCache() {
    }

}
