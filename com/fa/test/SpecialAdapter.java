package com.fa.test;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

class SpecialAdapter extends SimpleAdapter {
    private int[] colors = new int[] { Color.rgb(255, 255, 255), Color.rgb(237, 237, 237)};


    public SpecialAdapter(YJFL context,
			ArrayList<HashMap<String, Object>> lstImageItem, int main,
			String[] from, int[] to) {  super(context, lstImageItem, main, from, to);}
    public SpecialAdapter(FA2Activity context,
			ArrayList<HashMap<String, Object>> lstImageItem, int main,
			String[] from, int[] to) {  super(context, lstImageItem, main, from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      int colorPos = position % colors.length;
      view.setBackgroundColor(colors[colorPos]);
      
      return view;
    }
	
	
}