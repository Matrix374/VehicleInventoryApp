package mmu.ac.uk.vehicleinventorysystem.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import mmu.ac.uk.vehicleinventorysystem.R;
import mmu.ac.uk.vehicleinventorysystem.model.Vehicle;


//check cheese app week for more info
public class MainActivity extends AppCompatActivity {

    String[] vehicles;
    ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //run network on main thread hack
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView vehicleList = findViewById(R.id.vehicleListView);
        Button addVehicle = findViewById(R.id.addVehicle);

        HttpURLConnection urlConnection;
        InputStream in = null;
        try {
            // the url we wish to connect to
            URL url = new URL("http://10.0.2.2:4000/VDB/server");
            // open the connection to the specified URL
            urlConnection = (HttpURLConnection) url.openConnection();
            // get the response from the server in an input stream
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
// covert the input stream to a string
        String response = convertStreamToString(in);
// print the response to android monitor/log cat
        System.out.println("Server response = " + response);

        try {
            JSONArray jsonArray = new JSONArray(response);
            vehicles = new String[jsonArray.length()];

            for (int i=0; i < jsonArray.length(); i++)
            {
                int vehicle_id = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                String make = jsonArray.getJSONObject(i).get("make").toString();
                String model = jsonArray.getJSONObject(i).get("model").toString();
                int year = Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                int price = Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                String license_number = jsonArray.getJSONObject(i).get("license_number").toString();
                String colour = jsonArray.getJSONObject(i).get("colour").toString();
                int number_doors = Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                int mileage = Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                int engine_size = Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                String condition = jsonArray.getJSONObject(i).get("condition").toString();
                String notes = jsonArray.getJSONObject(i).get("notes").toString();

                System.out.println("model = " + model);

                Vehicle v = new Vehicle( vehicle_id, make, model,
                        year, price, license_number, colour, number_doors,
                        transmission, mileage, fuel_type, engine_size,
                        body_style, condition, notes);

                allVehicles.add(v);

                vehicles [i] = model;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicles);
        vehicleList.setAdapter(arrayAdapter);

        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "you pressed " + allVehicles.get(i).getVehicle_id(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

                intent.putExtra("vehicle", allVehicles.get(i));

                startActivity(intent);
            }

        });

        addVehicle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);

                startActivity(intent);
            }
        });

    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
