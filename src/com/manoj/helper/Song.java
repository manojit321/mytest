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
   private String duration="";
   private String title_key = "";
   private int audio_id;
   private String unique_row_id = "";
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
public String getDuration() {
	return duration;
}
public void setDuration(String duration) {
	this.duration = duration;
}
public String getTitle_key() {
	return title_key;
}
public void setTitle_key(String title_key) {
	this.title_key = title_key;
}
/**
 * @return the audio_id
 */
public int getAudio_id() {
	return audio_id;
}
/**
 * @param audio_id the audio_id to set
 */
public void setAudio_id(int audio_id) {
	this.audio_id = audio_id;
}
/**
 * @return the unique_row_id
 */
public String getUnique_row_id() {
	return unique_row_id;
}
/**
 * @param unique_row_id the unique_row_id to set
 */
public void setUnique_row_id(String unique_row_id) {
	this.unique_row_id = unique_row_id;
}
   
}