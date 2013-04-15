package com.manoj.helper;

import com.manoj.macawplayer.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Utilities {
//convert milliseconds to Time format
	public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
 
        // Convert total duration into time
           int hours = (int)( milliseconds / (1000*60*60));
           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
           // Add hours if there
           if(hours > 0){
               finalTimerString = hours + ":";
           }
 
           // Prepending 0 to seconds if it is one digit
           if(seconds < 10){
               secondsString = "0" + seconds;
           }else{
               secondsString = "" + seconds;}
 
           finalTimerString = finalTimerString + minutes + ":" + secondsString;
 
        // return timer string
        return finalTimerString;
    }
	
	
	 /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;
 
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
 
        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;
 
        // return percentage
        return percentage.intValue();
    }
    
    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);
 
        // return current duration in milliseconds
        return currentDuration * 1000;
    }
    
    public RelativeLayout colorSeter(RelativeLayout homeScreen,Context context){
    	FileHandlers fileHandlers = new FileHandlers();
    	if(fileHandlers.findKeyValue("temp.txt", context, "backgroundColor").equals("theme_black")){
			homeScreen.setBackgroundResource(R.drawable.theme_black);
		}
		else if(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor").equals("theme_pink")){
			homeScreen.setBackgroundResource(R.drawable.theme_pink);
		}
		else if(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor").equals("theme_skyblue")){
			homeScreen.setBackgroundResource(R.drawable.theme_skyblue);
		}
		else{
			homeScreen.setBackgroundColor(Color.parseColor(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor")));
		}
		return homeScreen;
    }
    public LinearLayout colorSeter(LinearLayout homeScreen,Context context){
    	FileHandlers fileHandlers = new FileHandlers();
    	if(fileHandlers.findKeyValue("temp.txt", context, "backgroundColor").equals("theme_black")){
			homeScreen.setBackgroundResource(R.drawable.theme_black);
		}
		else if(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor").equals("theme_pink")){
			homeScreen.setBackgroundResource(R.drawable.theme_pink);
		}
		else if(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor").equals("theme_skyblue")){
			homeScreen.setBackgroundResource(R.drawable.theme_skyblue);
		}
		else{
			homeScreen.setBackgroundColor(Color.parseColor(fileHandlers.findKeyValue("temp.txt",context, "backgroundColor")));
		}
		return homeScreen;
    }
}
