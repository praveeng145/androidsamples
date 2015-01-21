package com.goparaju.samples.gmaildelete;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.goparaju.samples.R;

public class GmailDeleteFeatureActivity extends ListActivity {
	
	// when an item is deleted, by swiping from left to right on screen
	// this variable is set to true
	private boolean markedForDelete = false;
	
	// holds the current list item position marked for delete
	private int deleteItemPosition = -1;
	
	// holds the current list item where user selects
	private int listItemPosition = -1;
	
	private static final String TAG = "GmailDeleteFeatureActivity";
	
	private String []listData = {"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December",
			"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
	private List<String> listAdapterData;
	
	private MyListAdapter listAdapter;
	
	private android.view.GestureDetector gestureDectector;
	
	private OnTouchListener customTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return gestureDectector.onTouchEvent(event);
		}
	};
	
	android.view.GestureDetector.OnGestureListener gestureListener = new android.view.GestureDetector.OnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			Log.i(TAG, "onDown");
			listItemPosition = getListView()
					.pointToPosition((int)e.getX(), (int)e.getY());
			if(listItemPosition >= 0 
					&& listItemPosition < listData.length
					&& markedForDelete) {
				if(deleteItemPosition != listItemPosition) {
					// delete the item from list
					listAdapterData.remove(deleteItemPosition);
				}
				markedForDelete = false;
				deleteItemPosition = -1;
				listAdapter.notifyDataSetChanged();
				return true;
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Log.i(TAG, "onShowPress");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Log.i(TAG, "single tap or click");
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.i(TAG, "onScrollEvent.. distanceX = "+ distanceX + ", distanceY = " + distanceY);
			listItemPosition = getListView()
					.pointToPosition((int)e1.getX(), (int)e1.getY());
			if(listItemPosition >= 0 && listItemPosition < listData.length) {
				markedForDelete = true;
				deleteItemPosition = listItemPosition;
			} else {
				markedForDelete = false;
				deleteItemPosition = -1;
			}
			Log.i(TAG, "listItemPosition = " + listItemPosition 
					+ ", markedForDelete = " + markedForDelete);
			listAdapter.notifyDataSetChanged();
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Log.i(TAG, "onLongPress");
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.i(TAG, "onFlingEvent..");
			return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gmail_delete);
		
		listAdapterData = new ArrayList<String>();
		for (String str : listData) {
			listAdapterData.add(str);
		}
		
		// init gesture dectector
		gestureDectector = new android.view.GestureDetector(this, gestureListener);
		
		// set touch listener
		getListView().setOnTouchListener(customTouchListener);
		
		// set adapter
		listAdapter = new MyListAdapter(this);
		getListView().setAdapter(listAdapter);
		
	}
	
	class MyListAdapter extends BaseAdapter {

		private LayoutHolder holder;
		private Context context;
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAdapterData.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = ((LayoutInflater)context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.layout_list_item, null);
				holder = new LayoutHolder();
				holder.tvText = (TextView)convertView.findViewById(R.id.list_item_text);
				holder.tvUndo = (TextView)convertView.findViewById(R.id.list_item_undo);
				
				convertView.setTag(holder);
			} else {
				holder = (LayoutHolder) convertView.getTag();
			}
			if(markedForDelete && (listItemPosition == position)) {
				// show the undo layout
				holder.tvUndo.setVisibility(View.VISIBLE);
				holder.tvText.setVisibility(View.GONE);
			} else {
				// show default layout
				holder.tvText.setText(listAdapterData.get(position));
				holder.tvUndo.setVisibility(View.GONE);
				holder.tvText.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		public MyListAdapter(Context context) {
			super();
			this.context = context;
		}
	}
	
	class LayoutHolder {
		TextView tvText;
		TextView tvUndo;
	}
}
