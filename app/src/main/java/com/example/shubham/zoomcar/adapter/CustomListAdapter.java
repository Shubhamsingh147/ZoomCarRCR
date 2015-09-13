package com.example.shubham.zoomcar.adapter;

/**
 * Created by shubham on 13/9/15.
 */

import com.example.shubham.zoomcar.MapActivity;
import com.example.shubham.zoomcar.R;
import com.example.shubham.zoomcar.app.AppController;
import com.example.shubham.zoomcar.model.Car;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Car> carItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Car> movieItems) {
        this.activity = activity;
        this.carItems = movieItems;
    }

    @Override
    public int getCount() {
        return carItems.size();
    }

    @Override
    public Object getItem(int location) {
        return carItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView seaterAC = (TextView) convertView.findViewById(R.id.seaterAC);
        ImageButton showLocation = (ImageButton) convertView.findViewById(R.id.mapButton);
        // getting car data for the row
        final Car m = carItems.get(position);
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name",m.getTitle());
                bundle.putString("rating",m.getRating());
                bundle.putInt("price",m.getPrice());
                bundle.putInt("seater",m.getSeater());
                bundle.putInt("AC",m.getAC());
                bundle.putFloat("latitude",m.getLatitude());
                bundle.putFloat("longitude",m.getLongitude());
                bundle.putString("imageURL", m.getThumbnailUrl());
                Intent intent = new Intent(activity.getApplicationContext(),MapActivity.class);
                intent.putExtra("carData",bundle);
                activity.startActivity(intent);
            }
        });


        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText("Rating : " + m.getRating());

        // hourly_rate

        price.setText("₹" + String.valueOf(m.getPrice()) + "/ hour");

        //AC
        if(m.getAC() == 1)
            seaterAC.setText(m.getSeater() + "-seater/AC");
        else
            seaterAC.setText(m.getSeater() + "-seater/NO-AC");

        return convertView;
    }

}