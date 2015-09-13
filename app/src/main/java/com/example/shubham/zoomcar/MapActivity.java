package com.example.shubham.zoomcar;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.zoomcar.app.AppController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private GoogleMap mMap;
    private float latitude, longitude;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private static final String TIME_PATTERN = "HH:mm";

    private TextView lblDate;
    private TextView lblTime;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle input = this.getIntent().getExtras().getBundle("carData");
        latitude = input.getFloat("latitude");
        longitude = input.getFloat("longitude");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
        TextView title = (TextView) findViewById(R.id.title);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView price = (TextView) findViewById(R.id.price);
        TextView seaterAC = (TextView) findViewById(R.id.seaterAC);
        thumbNail.setImageUrl(input.getString("imageURL"), imageLoader);
        title.setText(input.getString("name"));
        rating.setText("Rating : " + input.getString("rating"));
        price.setText("₹" + input.getInt("price") + "/ hour");
        if(this.getIntent().getExtras().getInt("AC") == 1)
            seaterAC.setText(input.getInt("seater") + "-seater/AC");
        else
            seaterAC.setText(input.getInt("seater") + "-seater/NO-AC");

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        lblDate = (TextView) findViewById(R.id.lblDate);
        lblTime = (TextView) findViewById(R.id.lblTime);

        update();
    }

    private void update() {
        lblDate.setText(dateFormat.format(calendar.getTime()));
        lblTime.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker where car is and move the camera
        LatLng carLocation = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(carLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.car2)).title("Car is Here");
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));


    }
    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblDate:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.lblTime:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }
}