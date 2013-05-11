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

        resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = "+audioId, null);
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
	   songsList = sInfo.getAlbumSongs(context.getContentResolver(),playlistname);
       return songsList;
   }
}
