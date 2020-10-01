package com.example.machine_learning_app;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid, null);
        final TextView textView = (TextView) v.findViewById(R.id.textView15);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView4);
        Button button = v.findViewById(R.id.button5);
        textView.setText(birdList.get(position).getbirdName());
        imageView.setImageResource(birdList.get(position).getbirdImage());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RaceDetaille.class);
                intent.putExtra("raceName", String.valueOf(birdList.get(position).getbirdName()));
                intent.putExtra("image", birdList.get(position).getbirdImage());
                getContext().startActivity(intent);
            }
        });
        return v;

    }

}
