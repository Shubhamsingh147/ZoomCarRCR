package com.example.shubham.zoomcar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class History extends Activity {
    ListView listView ;
    String appName = "ZoomCar";
    File fileBackupDir = new File(Environment.getExternalStorageDirectory(), appName + "/backup");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        listView = (ListView) findViewById(R.id.list2);
        try {
            if (!fileBackupDir.exists()) {
                fileBackupDir.mkdirs();
            }
            File file = new File(fileBackupDir, "history.txt");
            if(!file.exists()) {
                file.createNewFile();
                TextView tv = (TextView) findViewById(R.id.tv);
                tv.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line = "";
                int i = 0;
                ArrayList<String> values = new ArrayList<String>();
                while ((line = bufferedReader.readLine()) != null) {
                    values.add(line);
                    i++;
                }
                if (values == null) {
                    TextView tv = (TextView) findViewById(R.id.tv);
                    tv.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, values);

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}