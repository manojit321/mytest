package com.manoj.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.helper.ImageLoader;
import com.manoj.macawplayer.CustomizedListView;
import com.manoj.macawplayer.R;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext(),activity);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.song_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap song = new HashMap();
        song = data.get(position);
        
        // Setting all values in listview
        title.setText(song.get(CustomizedListView.KEY_TITLE).toString());
        artist.setText(song.get(CustomizedListView.KEY_ARTIST).toString());
        duration.setText(song.get(CustomizedListView.KEY_DURATION).toString());
        imageLoader.DisplayImage((Uri) song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}