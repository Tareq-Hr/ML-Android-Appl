package com.example.machine_learning_app;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter {

    ArrayList<item> birdList = new ArrayList<item>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        birdList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid, null);
        TextView textView = (TextView) v.findViewById(R.id.textView15);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView4);
        textView.setText(birdList.get(position).getbirdName());
        imageView.setImageResource(birdList.get(position).getbirdImage());
        return v;

    }

}
