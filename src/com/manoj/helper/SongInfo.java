package com.manoj.helper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class SongInfo {
	public ArrayList getSongs(ContentResolver contentResolver)
	{
	    List<Integer> result = new ArrayList<Integer>();
	    ArrayList songList=new ArrayList();
	    Cursor cursor = contentResolver.query(MediaStore.Audio.Media.getContentUri("external"), new String[]{MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.TRACK,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.COMPOSER,MediaStore.Audio.Media.DATA}, null, null, null);

	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	        	Song song=new Song();
	        	int albumId=cursor.getInt(0);
	            song.setAlbum_id(cursor.getInt(0));
	            song.setTitle(cursor.getString(1));
	            song.setArtist(cursor.getString(2));
	            song.setTrack(cursor.getString(3));
	            song.setAlbum(cursor.getString(4));
	            song.setComposer(cursor.getString(5));
	            song.setUrl(cursor.getString(6));
	            
	            Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                song.setAlbumArtUrl(albumArtUri);
	            if (!result.contains(albumId)){
	                result.add(albumId);
	                songList.add(song);
	            }
	        } while (cursor.moveToNext());
	    }
	    return songList;
	}
}
