package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zappycode.memorableplaces.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static  ArrayList<String> places=new ArrayList<>();
    static ArrayList<LatLng> locations=new ArrayList<>();               //static: so that we can comfortably access them from another acivity also.
    static  ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);  //we recieve data in main activity

        ArrayList<String> latitudes=new ArrayList<>();
        ArrayList<String> longitudes=new ArrayList<>();

        latitudes.clear();
        longitudes.clear();
        places.clear();
        locations.clear();

        try{
            places=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("longs",ObjectSerializer.serialize(new ArrayList<String>())));

        }catch (Exception e){
            e.printStackTrace();
        }

         if(places.size()>0 && latitudes.size()>0 && longitudes.size()>0) {
             if (places.size() == latitudes.size() && latitudes.size() == longitudes.size()) {
                for(int i=0;i<latitudes.size();i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i)))); //this line constructs bcak everything i.e. latitudes and longitudes are again converted to loactions and added to loactions arraylist
                }
             }
         }  else{
             //this is the first time we've opened the app

             places.add("Add a memorable place..");
             locations.add(new LatLng(0,0));  //so that jdo aapa next activity cho location.get() call kraange odo dona(places and locations) da index same rhe...hun dona ch ikk ikk element paake same krta ehnu

         }

        ListView listView=findViewById(R.id.mylistView);
         arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), memorableMapsActivity.class);

                intent.putExtra("placenumber", position);

                startActivity(intent);
            }
        });
    }
}
