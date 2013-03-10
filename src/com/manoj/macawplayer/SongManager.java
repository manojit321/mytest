package com.manoj.macawplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SongManager {
    // SDCard Path
    final String MEDIA_PATH = new String("/mnt/sdcard/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> song = new HashMap<String, String>();
    // Constructor
    public SongManager(){
 
    }
 
    
    public ArrayList<HashMap<String, String>> getPlayList(){
    	File home = new File(MEDIA_PATH);
    	if(songsList.isEmpty()){
    	getPlayList2(home);
    	}
    	return songsList;
    }
    
  /*  public ArrayList<HashMap<String, String>> getPlayList2(File home){
    	try{
    	int size=0;
    	 File[] fileList=null;
    	 File file;
    	if(home.isDirectory()){
    	  fileList=home.listFiles();
    	 size=fileList.length-1;
    	}
    	 while(size>=0){
    		 if(home.isDirectory()){
    		  file=fileList[size];
    		 }
    		 else
    			 file=home; 
    		 size--;
    		 if(file==null)
    			 break;
    		 if(file.isFile()){
    			 if(file.getName().endsWith(".mp3") ||file.getName().endsWith(".MP3") ){
    			 song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                 song.put("songPath", file.getPath());
  
                 // Adding each song to SongList
                 songsList.add(song);
    			 }
    		 }else{
    			 getPlayList2(file);
    		 }
    		 
    	 }
    	}
    	catch(Exception e){
    		
    	}
    	 return songsList;
    }*/
    
    public void getPlayList2(File home){
    	try{
    	String[] files=home.list();
    	int i=0;
    	ArrayList dir=new ArrayList();
    	while(i<files.length){
    		dir.add(files[i]);
    		i++;
    	}
    	if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());
 
                // Adding each song to SongList
                songsList.add(song);
                dir.remove( file.getName());
            }
        }
    	Iterator itr=dir.iterator();
    	while(itr.hasNext()){
    		String nextHome=home.getAbsolutePath()+"/"+(String)itr.next();
    		File f=new File(nextHome);
    		getPlayList2(f);
    	}
    	}catch(Exception e){
    		return;
    	}
     }
    
    
    
    
    
    
    
    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList1(){
        File home = new File(MEDIA_PATH);
        System.out.println(home.list());
        
        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());
 
                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;
    }
 
    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}