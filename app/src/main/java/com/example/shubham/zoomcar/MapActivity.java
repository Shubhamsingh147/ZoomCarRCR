package com.example.shubham.zoomcar;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private GoogleMap mMap;
    private float latitude, longitude;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private static final String TIME_PATTERN = "HH:mm";

    private TextView lblDate;
    private TextView lblTime;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    Bundle input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        input = this.getIntent().getExtras().getBundle("carData");
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
        lblDate.setOnClickListener(this);
        lblTime.setOnClickListener(this);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblDate:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.lblTime:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
            case R.id.bookButton:
                Toast.makeText(this,"I have interned in PayU so I can easily handle the payment gateway part, I wish you'd have given the API access to payments too :(",Toast.LENGTH_LONG).show();
                break;
            case R.id.historyButton:
                Toast.makeText(this,"Sorry, this feature is paid. To avail this feature, HIRE ME in no less than 20L, Just kiddin! :P",Toast.LENGTH_LONG).show();
                break;
            case R.id.smsButton:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + "8979524541")));
                break;
            case R.id.shareData:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = input.getString("name")+" @"+input.getInt("price")+"/hr, with rating "+input.getString("rating");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this cool car");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
