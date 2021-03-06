package com.manoj.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class SongInfo {
	String[] songToFetch = {MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.TRACK,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.COMPOSER,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.TITLE_KEY,MediaStore.Audio.Media._ID};
	@SuppressWarnings("finally")
	public ArrayList<Song> getSongs(ContentResolver contentResolver)
	{
		ArrayList<Song> songList=new ArrayList<Song>();
		Cursor cursor=null;
		try{
	    List<Integer> result = new ArrayList<Integer>();
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songToFetch, null, null, null);
	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	        	if(cursor.getString(7)!=null && cursor.getString(8).equals("0"))
	        		continue;
	        	Song song=new Song();
	        	int albumId=cursor.getInt(0);
	            song.setAlbum_id(cursor.getInt(0));
	            song.setTitle(cursor.getString(1));
	            song.setArtist(cursor.getString(2));
	            song.setTrack(cursor.getString(3));
	            song.setAlbum(cursor.getString(4));
	            song.setComposer(cursor.getString(5));
	            song.setUrl(cursor.getString(6));
	            song.setDuration(cursor.getString(7));
	            song.setTitle_key(cursor.getString(9));
	            song.setUnique_row_id(cursor.getString(10));
	            Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                song.setAlbumArtUrl(albumArtUri);
	            //if (!result.contains(albumId)){
	              //  result.add(albumId);
	                songList.add(song);
	            //}
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return songList;
		}
	}
	
	@SuppressWarnings("finally")
	public ArrayList getAlbums(ContentResolver contentResolver)
	{
		Set songList=new HashSet();
		Cursor cursor=null;
		try{
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
	    			MediaStore.Audio.Media.ALBUM
	    			}, null, null, null);
	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	            songList.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return new ArrayList(songList);
		}
	}
	
	@SuppressWarnings("finally")
	public ArrayList getAlbumSongs(ContentResolver contentResolver,String album)
	{
		Set songList=new HashSet();
		Cursor cursor=null;
		try{
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songToFetch,
	    			 MediaStore.Audio.Media.ALBUM+"=?", new String[]{album}, null);
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
	            song.setTitle_key(cursor.getString(9));
	            song.setUrl(cursor.getString(6));
	            
	            Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                song.setAlbumArtUrl(albumArtUri);
                songList.add(song);
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return new ArrayList(songList);
		}
	}
	
	@SuppressWarnings("finally")
	public ArrayList getArtist(ContentResolver contentResolver)
	{
		Set songList=new HashSet();
		Cursor cursor=null;
		try{
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
	    			MediaStore.Audio.Media.ARTIST
	    			}, null, null, null);
	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	            songList.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return new ArrayList(songList);
		}
	}
	@SuppressWarnings("finally")
	public ArrayList getArtistSongs(ContentResolver contentResolver,String album)
	{
		Set songList=new HashSet();
		Cursor cursor=null;
		try{
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songToFetch,
	    			 MediaStore.Audio.Media.ARTIST+"=?", new String[]{album}, null);
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
                songList.add(song);
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return new ArrayList(songList);
		}
	}

	@SuppressWarnings("finally")
	public int getSongBasedOnTitle_key(ArrayList songList,String title_key)
	{
		Iterator<Song> iterator = songList.iterator();
		Song song = null;
		int position = 0;
		while(iterator.hasNext()){
			song = iterator.next();
			if(song.getTitle_key().equalsIgnoreCase(title_key))
				return position;
			position++;
		}
		return 0;
	}
	
	
	@SuppressWarnings("finally")
	public ArrayList getAlbumsWithArt(ContentResolver contentResolver)
	{
		ArrayList<Song> songList=new ArrayList<Song>();
		Cursor cursor=null;
		try{
	    List<Integer> result = new ArrayList<Integer>();
	    	cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]{"DISTINCT "+MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ALBUM_ID},
	    			null, null, null);
	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	        	Song song=new Song();
	        	int albumId=cursor.getInt(1);
	            song.setAlbum_id(cursor.getInt(1));
	            song.setAlbum(cursor.getString(0));
	            Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                song.setAlbumArtUrl(albumArtUri);
	            //if (!result.contains(albumId)){
	              //  result.add(albumId);
	                songList.add(song);
	            //}
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return songList;
		}
	}
	
	
	public ArrayList<Song> getPlaylistSongs(ContentResolver contentResolver,String playlist_id){
		ArrayList<Song> songList=new ArrayList<Song>();
		String[] playlistSongToFetch = {MediaStore.Audio.Playlists.Members.ALBUM_ID,MediaStore.Audio.Playlists.Members.TITLE,MediaStore.Audio.Playlists.Members.ARTIST,MediaStore.Audio.Playlists.Members.TRACK,
				MediaStore.Audio.Playlists.Members.ALBUM,MediaStore.Audio.Playlists.Members.COMPOSER,MediaStore.Audio.Playlists.Members.DATA,MediaStore.Audio.Playlists.Members.DURATION,MediaStore.Audio.Playlists.Members.IS_MUSIC,
				MediaStore.Audio.Playlists.Members.TITLE_KEY,MediaStore.Audio.Playlists.Members.AUDIO_ID};
		Cursor cursor=null;
		try{
	    List<Integer> result = new ArrayList<Integer>();
	    Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(playlist_id));
	    	cursor = contentResolver.query(uri,playlistSongToFetch, MediaStore.Audio.Playlists.Members.PLAYLIST_ID+"=?", new String[]{playlist_id}, null);
	    if (cursor!=null && cursor.moveToFirst())
	    {
	        do{
	        	if(cursor.getString(7)!=null && cursor.getString(8).equals("0"))
	        		continue;
	        	Song song=new Song();
	        	int albumId=cursor.getInt(0);
	            song.setAlbum_id(cursor.getInt(0));
	            song.setTitle(cursor.getString(1));
	            song.setArtist(cursor.getString(2));
	            song.setTrack(cursor.getString(3));
	            song.setAlbum(cursor.getString(4));
	            song.setComposer(cursor.getString(5));
	            song.setUrl(cursor.getString(6));
	            song.setDuration(cursor.getString(7));
	            song.setTitle_key(cursor.getString(9));
	            song.setAudio_id(cursor.getInt(10));
	            Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                song.setAlbumArtUrl(albumArtUri);
	            //if (!result.contains(albumId)){
	              //  result.add(albumId);
	                songList.add(song);
	            //}
	        } while (cursor.moveToNext());
	    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
			cursor.close();
			return songList;
		}
	}
}