package com.manoj.helper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;

public class FileHandlers {

	public void saveKeyValue(String filename,Context ctx,String Key,String value) {
			        FileOutputStream fileOutputStream=null;
			        OutputStreamWriter outputStreamWriter=null;
			        BufferedWriter bufferedWriter=null;
			        try {
			        	fileOutputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
			             outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			             bufferedWriter = new BufferedWriter(outputStreamWriter);

			             bufferedWriter.write(Key+"="+value); 
			        } catch (FileNotFoundException e) {
			            e.printStackTrace();
			        }catch(IOException e){
			            e.printStackTrace();
			        }
			        finally{
			        	try{
			            	bufferedWriter.close();
			            	outputStreamWriter.close();
			            	fileOutputStream.close();
			            	}catch(Exception e){
			            		
			            	}
			            }
			    }
	
	public  String findKeyValue(String filename,Context ctx,String Key) {
        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        String color="#0099CC";
        try {
        	fileInputStream = ctx.openFileInput(filename);
        	inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line,value[];
            while ((line = bufferedReader.readLine()) != null) {
            	line.trim();
            	if(line.startsWith(Key)){
            		value=line.split("=");
            		color = value[1];
            	}
            }
           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
        	try{
        	bufferedReader.close();
        	inputStreamReader.close();
        	fileInputStream.close();
        	}catch(Exception e){
        		
        	}
        }
        return color;
    }
}
