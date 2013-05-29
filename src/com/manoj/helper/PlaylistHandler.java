package com.manoj.helper;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.bean.PlaylistBean;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class PlaylistHandler {

	String TAG = "PlaylistHandler";
	SongInfo sInfo;
	private ArrayList songsList = new ArrayList();
	
	public static void createPlaylist(ContentResolver resolver, String pName) {
		Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.NAME, pName);
		Uri newPlaylistUri = resolver.insert(uri, values);
	}
	
	
	public static void addToPlaylist(ContentResolver resolver, int audioId,long YOUR_PLAYLIST_ID) {

        String[] cols = new String[] {
                "count(*)"
        };
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", YOUR_PLAYLIST_ID);
        Cursor cur = resolver.query(uri, cols, null, null, null);
        cur.moveToFirst();
        final int base = cur.getInt(0);
        cur.close();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);
        resolver.insert(uri, values);
    }

   public static void removeFromPlaylist(ContentResolver resolver, int audioId,long YOUR_PLAYLIST_ID) {
       Log.v("made it to add",""+audioId);
        String[] cols = new String[] {
                "count(*)"
        };
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", YOUR_PLAYLIST_ID);
        Cursor cur = resolver.query(uri, cols, null, null, null);
        cur.moveToFirst();
        final int base = cur.getInt(0);
        cur.close();
        ContentValues values = new ContentValues();
        String[] whereVal = {audioId+""};
        Log.i("","no of rows deleted "+resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = ?",whereVal));
    }
   public ArrayList<PlaylistBean> checkforplaylists(Context context)
   {
	   ArrayList<PlaylistBean> playlist = new ArrayList<PlaylistBean>();
       ContentResolver cr = context.getContentResolver();
       final Uri uri=MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
       final String id=MediaStore.Audio.Playlists._ID;
       final String name=MediaStore.Audio.Playlists.NAME;
       final String[]columns={id,name};
       final Cursor cursor= cr.query(uri, columns, null, null, null);
           if(cursor==null)
               {
                   Log.e(TAG,"Found no Playlists.");
                       return null;
               }
           Log.e(TAG,"Found playlists.");
           cursor.moveToFirst();
           int i=0;
           while(i<cursor.getCount()){
        	   PlaylistBean playlistBean = new PlaylistBean();
        	   playlistBean.setPlaylistId(cursor.getLong(0));
        	   playlistBean.setPlaylistName(cursor.getString(1));
        	   playlist.add(playlistBean);
        	   Log.e(TAG,playlistBean.getPlaylistName());
        	   cursor.moveToNext();
        	   i++;
           }
       return playlist;
   }
   
   public ArrayList songsforplaylists(Context context,String playlistname)
   {
	   sInfo =new SongInfo();
	   songsList = sInfo.getPlaylistSongs(context.getContentResolver(),playlistname);
       return songsList;
   }
   
   public static void deletePlaylist(ContentResolver resolver, String pName) {
	   String where = MediaStore.Audio.Playlists.NAME + "=?";
	   String[] whereVal = {pName}; 
	   int num = resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal);
	   Log.i("","deleted "+num); 
	}
   
   
   
   
   
   
   
   
   
   
   
   public static Cursor query(Context context, Uri uri, String[] projection,
	           String selection, String[] selectionArgs, String sortOrder, 
	               int limit) 
	{
	 ContentResolver resolver = context.getContentResolver();
	 if (resolver == null) {
	     return null;
	 }
	
	 if (limit > 0) {
	     uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
	 }
	
	 return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
	} 
	
	public static Cursor query(Context context, Uri uri, String[] projection,
	       String selection, String[] selectionArgs, String sortOrder)
	{
	
	 String[] projection1 = new String[]{MediaStore.Audio.Playlists.Members.AUDIO_ID};
	
	 return query(context, uri, projection1, selection, selectionArgs, sortOrder, 0);
	}
    public static int idFortrack(Context context, String path) {
	    Cursor c = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	                            new String[] { MediaStore.Audio.Media._ID },
	                            MediaStore.Audio.Media.TITLE_KEY + "=?",
	                            new String[] { path },
	                            MediaStore.Audio.Media.TITLE_KEY);
	    return intFromCursor(c);
	}
   	private static int intFromCursor(Cursor c) {
	    int id = -1;

	    if (c != null) {
	        c.moveToFirst();

	        if (!c.isAfterLast()) {
	            id = c.getInt(0);
	        }
	    }

	    c.close();
	    return id;
	}

   
   
   
   
   
   
   
   
   
   
   
   
   
   
}
