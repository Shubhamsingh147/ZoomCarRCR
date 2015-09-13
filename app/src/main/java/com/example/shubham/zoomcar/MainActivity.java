package com.example.shubham.zoomcar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.zoomcar.adapter.CustomListAdapter;
import com.example.shubham.zoomcar.app.AppController;
import com.example.shubham.zoomcar.model.Car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends Activity implements View.OnClickListener{
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://zoomcar.0x10.info/api/zoomcar?type=json&query=list_cars";
    private static final String api_hit_url = "https://zoomcar.0x10.info/api/zoomcar?type=json&query=api_hits";
    private ProgressDialog pDialog;
    private List<Car> carList = new ArrayList<Car>();
    private ListView listView;
    private CustomListAdapter adapter;
    private TextView numCars,api_hits;
    private Button sortPrice,sortRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numCars  = (TextView)findViewById(R.id.numCars);
        api_hits = (TextView) findViewById(R.id.api_hits);
        sortPrice = (Button) findViewById(R.id.sortPrice);
        sortPrice.setOnClickListener(this);
        sortRating = (Button) findViewById(R.id.sortRating);
        sortRating.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, carList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
     //   getActionBar().setBackgroundDrawable(
        //        new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonObjectRequest carReq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        listView.setVisibility(View.VISIBLE);
                        hidePDialog();
                        try {
                            JSONArray carArray = response.getJSONArray("cars");
                            numCars.setText("Total Cars : " + String.valueOf(carArray.length()));
                            // Parsing json
                            for (int i = 0; i < carArray.length(); i++) {

                                JSONObject obj = carArray.getJSONObject(i);
                                Car car = new Car();
                                car.setTitle(obj.getString("name"));
                                car.setThumbnailUrl(obj.getString("image"));
                                car.setRating(obj.getString("rating"));
                                car.setAC(obj.getInt("ac"));
                                car.setPrice(obj.getInt("hourly_rate"));
                                car.setSeater(obj.getInt("seater"));
                                car.setLatitude(Float.parseFloat(obj.getJSONObject("location").getString("latitude")));
                                car.setLongitude(Float.parseFloat(obj.getJSONObject("location").getString("longitude")));
                                // adding car to carArray
                                carList.add(car);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                TextView tvError = (TextView)findViewById(R.id.tvError);
                tvError.setVisibility(View.VISIBLE);
                Button historyButton = (Button)findViewById(R.id.historyBtn);
                historyButton.setVisibility(View.VISIBLE);
                historyButton.setOnClickListener(MainActivity.this);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.footer);
                linearLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                hidePDialog();
            }
        });
        JsonObjectRequest api_hit_Req = new JsonObjectRequest(Request.Method.GET,api_hit_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        try {
                            api_hits.setText("API Hits : " + response.getString("api_hits"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(carReq);
        AppController.getInstance().addToRequestQueue(api_hit_Req);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.sortPrice){

            Collections.sort(carList, new Comparator<Car>() {
                @Override
                public int compare(Car car1, Car car2) {
                    if(car1.getPrice() == car2.getPrice())
                        return 0;
                    return car1.getPrice() < car2.getPrice() ? -1 : 1;
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if(id == R.id.sortRating){

            Collections.sort(carList, new Comparator<Car>() {
                @Override
                public int compare(Car car1, Car car2) {
                    if(car1.getRating() == car2.getRating())
                        return 0;
                    return Double.parseDouble(car1.getRating()) > Double.parseDouble(car2.getRating()) ? -1 : 1;
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if(id == R.id.historyBtn){
            Intent intent = new Intent(this,History.class);
            startActivity(intent);
        }
    }

}