package com.manoj.helper;

import android.graphics.Bitmap;
import android.net.Uri;

public class Song {
   private int album_id;
   private String title="";
   private String album="";
   private String track="";
   private String composer="";
   private String artist="";
   private String url="";
   Bitmap bitmap;
   private Uri albumArtUrl;
public Bitmap getBitmap() {
	return bitmap;
}
public void setBitmap(Bitmap bitmap) {
	this.bitmap = bitmap;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public int getAlbum_id() {
	return album_id;
}
public void setAlbum_id(int album_id) {
	this.album_id = album_id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getAlbum() {
	return album;
}
public void setAlbum(String album) {
	this.album = album;
}
public String getTrack() {
	return track;
}
public void setTrack(String track) {
	this.track = track;
}
public String getComposer() {
	return composer;
}
public void setComposer(String composer) {
	this.composer = composer;
}
public String getArtist() {
	return artist;
}
public void setArtist(String artist) {
	this.artist = artist;
}
public Uri getAlbumArtUrl() {
	return albumArtUrl;
}
public void setAlbumArtUrl(Uri albumArtUrl) {
	this.albumArtUrl = albumArtUrl;
}
   
}