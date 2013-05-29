package com.manoj.fragments;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manoj.adapters.LazyAdapter;
import com.manoj.customview.helper.ImageCache.ImageCacheParams;
import com.manoj.customview.helper.ImageFetcher;
import com.manoj.customview.helper.RecyclingImageView;
import com.manoj.customview.helper.Utils;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.BuildConfig;
import com.manoj.macawplayer.CustomizedListView;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.SongManager;


/**
 * This fragment will populate the children of the ViewPager from {@link ImageDetailActivity}.
 */
public class SongCustomFragment extends Fragment {
	private static final String TAG = "SongCustomFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    ArrayList<HashMap> filteredSongMap = new ArrayList<HashMap>();
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private Activity activity;
    ArrayList songsList1=new ArrayList();
	private Utilities utilities;
	ArrayList<HashMap> songsListData;
	int textlength = 0;
	EditText edittext;
	private int heigth;
	private int width;
	private LinearLayout homeScreen;
	ListView mGridView;
	private int currentSongIndex;
	public boolean scroll;
    /**
     * Empty constructor as per the Fragment documentation
     */
    public SongCustomFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.activity = getActivity();
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail1_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail2_spacing);

         songsListData = new ArrayList<HashMap>();
		SongManager songManager=new SongManager();
		SongInfo si=new SongInfo();
		utilities = new Utilities();
		heigth = getActivity().getResources().getDisplayMetrics().heightPixels;
		width = getActivity().getResources().getDisplayMetrics().widthPixels;
		currentSongIndex = getActivity().getIntent().getExtras().getInt("songPlaying");
		if(songsList1!=null && this.songsList1.isEmpty())
		 songsList1=si.getSongs(getActivity().getContentResolver());
		
			for (int i = 0; i < songsList1.size(); i++) {
				Song songs=(Song)songsList1.get(i);
	            HashMap song1=new HashMap();
	            String title=songs.getTitle();
	            song1.put("songTitle",title);
	            song1.put("KEY_ID","wq");
				song1.put("KEY_TITLE",songs.getTitle());
				song1.put("KEY_ARTIST",songs.getArtist());
				song1.put("KEY_DURATION",songs.getDuration());
				song1.put("KEY_THUMB_URL",songs.getAlbumArtUrl());
				song1.put("KEY_TITLE_UNIQUE", songs.getTitle_key());
				song1.put("KEY_Unique_row_id", songs.getUnique_row_id());
	            songsListData.add(song1);
	        }
		scroll = true;
        mAdapter = new ImageAdapter(getActivity(),songsListData);

        ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize,activity);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.song, container, false);
        mGridView=(ListView)v.findViewById(R.id.song_list);
        edittext = (EditText)v.findViewById(R.id.song_search);
        mGridView.setAdapter(mAdapter);
        
        homeScreen = (LinearLayout) v.findViewById(R.id.song_layout);
		utilities.colorSeter(homeScreen,activity.getApplicationContext());
        
        
        edittext.addTextChangedListener(new TextWatcher()
        {

        public void afterTextChanged(Editable s)
        {

        }

        public void beforeTextChanged(CharSequence s, int start,
        int count, int after)
        {

        }

        public void onTextChanged(CharSequence s, int start,
        int before, int count)
        {
        	scroll = false;
        	filteredSongMap.clear();
        	try{

		        textlength = edittext.getText().length();
		        if(textlength == 0){
		        	 mAdapter = new ImageAdapter(getActivity(),songsListData);
				     mGridView.setAdapter(mAdapter);
		        }
		        else{
			        for (int i = 0; i < songsListData.size(); i++)
			        {
			        	HashMap songMap=(HashMap)songsListData.get(i);
			        	String toFilterText = songMap.get("KEY_TITLE").toString();
				        if (textlength <= toFilterText.length())
				        {
					        if (edittext.getText().toString().
					        equalsIgnoreCase((String) toFilterText.subSequence(0, textlength)))
					        {
					        	filteredSongMap.add(songMap);
					        }
				        }
			        }
			        
			        mAdapter = new ImageAdapter(getActivity(),filteredSongMap);
			        mGridView.setAdapter(mAdapter);
		        }
        	}catch(Exception e){
        		Log.i("", e.toString());
        	}
		        
		        //mAdapter.songsListData = filteredSongMap;
		        /*mAdapter = new ImageAdapter(getActivity(),filteredSongMap);

		        ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

		        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

		        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
		        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize,activity);
		        mImageFetcher.setLoadingImage(R.drawable.adele);
		        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
*/
		        
		      /*  for (int i = 0; i < text.length; i++)
		        {
		        if (textlength <= text[i].length())
		        {
		        if (edittext.getText().toString().
		        equalsIgnoreCase((String) text[i].subSequence(0, textlength)))
		        {
		        text_sort.add(text[i]);
		        image_sort.add(image[i]);
		        }
		        }*/
        }
      });


        
        
        
        
        
        
        mGridView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mImageFetcher.setPauseWork(true);
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }
        });
        
       // select single listview item
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
			
			}
		});

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        /*mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                            }
                        }
                    }
                });*/

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

   /* @TargetApi(16)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
        if (Utils.hasJellyBean()) {
            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
            // show plus the thumbnail image in GridView is cropped. so using
            // makeScaleUpAnimation() instead.
            ActivityOptions options =
                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

*/    /**
     * The main adapter that backs the GridView. This is fairly standard except the number of
     * columns in the GridView is used to create a fake top row of empty views as we use a
     * transparent ActionBar and don't want the real top row of images to start off covered by it.
     */
    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private int mActionBarHeight = 0;
        private ListView.LayoutParams mImageViewLayoutParams;
        ArrayList<HashMap> tempSongsListData = new ArrayList<HashMap>();
        private  LayoutInflater inflater=null;
        private boolean scroll = true;
        public ImageAdapter(Context context,ArrayList<HashMap> data) {
            //super();
        	super();
            tempSongsListData = data;
            
            mContext = context;
            mImageViewLayoutParams = new ListView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }



        @Override
        public int getCount() {
            // Size + number of columns for top empty row
            return tempSongsListData.size();
        }

        @Override
        public Object getItem(int position) {
            return position < mNumColumns ?
                    null : tempSongsListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position < mNumColumns ? 0 : position;
        }

        @Override
        public int getViewTypeCount() {
            // Two types of views, the normal ImageView and the top row of empty views
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position < mNumColumns) ? 1 : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup container) {
        	try{
             if(convertView==null)
            	 convertView = inflater.inflate(R.layout.song_list_row, null);

        	 TextView title = (TextView)convertView.findViewById(R.id.title); // title
             TextView artist = (TextView)convertView.findViewById(R.id.artist); // artist name
             TextView duration = (TextView)convertView.findViewById(R.id.duration); // duration
             ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image); // thumb image
             ImageButton arrow_image=(ImageButton)convertView.findViewById(R.id.arrowImage); // thumb image
  /*          // First check if this is the top row
            if (position < mNumColumns) {
                if (convertView == null) {
                    convertView = new View(mContext);
                }
                // Set empty view with height of ActionBar
                convertView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
                return convertView;
            }

            // Now handle the main ImageView thumbnails
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView)convertView.findViewById(R.id.list_image);
            }

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }
*/
            HashMap song = new HashMap();
            song = tempSongsListData.get(position);
            // Setting all values in listview
            title.setText(song.get("KEY_TITLE").toString());
            artist.setText(song.get("KEY_ARTIST").toString());
            duration.setText(utilities.milliSecondsToTimer(Long.parseLong(song.get("KEY_DURATION").toString())));
            
           /* LayoutParams lp = new LayoutParams(width*100/20, 
                    LayoutParams.WRAP_CONTENT);
            
            thumb_image.setLayoutParams(lp);
            
            lp = new LayoutParams(width*100/70, 
                    LayoutParams.WRAP_CONTENT);
            title.setLayoutParams(lp);
            lp = new LayoutParams(width*100/10, 
                    LayoutParams.WRAP_CONTENT);
            arrow_image.setLayoutParams(lp);*/
            title.setWidth(width*60/100);
            final int t=position;
            arrow_image.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					Toast.makeText(mContext, ""+t,Toast.LENGTH_LONG).show();
					AvailablePlaylistDialogFragment.newInstance(tempSongsListData.get(t)).show(getFragmentManager(), TAG);
				}
			});
            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            mImageFetcher.loadImage((Uri) song.get("KEY_THUMB_URL"), thumb_image);
            
            
            convertView.setOnClickListener(new View.OnClickListener(){
        			@Override
        			public void onClick(View view){
        			//// getting listitem index
                        int songIndex = t;
                     // Starting new intent
                        Intent in =new Intent(activity.getApplicationContext(),MainActivity.class);
                     
                        //sending song index to player activity
                        if(filteredSongMap.isEmpty())
                        	in.putExtra("title_key", tempSongsListData.get(songIndex).get("KEY_TITLE_UNIQUE").toString());
                        else
                        	in.putExtra("title_key", filteredSongMap.get(songIndex).get("KEY_TITLE_UNIQUE").toString());
                        activity.setResult(203, in);
                        
                        //closing playlist view
                        activity.finish();
        			}
        		});
            
            
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            return convertView;
        }

        /**
         * Sets the item height. Useful for when we know the column width so the height can be set
         * to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
            mImageFetcher.setImageSize(height);
            notifyDataSetChanged();
        }

        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }

    }
}
