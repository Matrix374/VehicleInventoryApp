package mmu.ac.uk.vehicleinventorysystem.view;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import mmu.ac.uk.vehicleinventorysystem.R;
import mmu.ac.uk.vehicleinventorysystem.model.Vehicle;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //run network on main thread hack
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText make = (EditText) findViewById(R.id.make);
        final EditText model = (EditText) findViewById(R.id.model);
        final EditText year = (EditText) findViewById(R.id.year);
        final EditText price = (EditText) findViewById(R.id.price);
        final EditText license_number = (EditText) findViewById(R.id.license_number);
        final EditText colour = (EditText) findViewById(R.id.colour);
        final EditText number_doors = (EditText) findViewById(R.id.number_doors);
        final EditText transmission = (EditText) findViewById(R.id.transmission);
        final EditText mileage = (EditText) findViewById(R.id.mileage);
        final EditText fuel_type = (EditText) findViewById(R.id.fuel_type);
        final EditText engine_size = (EditText) findViewById(R.id.engine_size);
        final EditText body_style = (EditText) findViewById(R.id.body_style);
        final EditText condition = (EditText) findViewById(R.id.condition);
        final EditText notes = (EditText) findViewById(R.id.notes);

        Button submit = findViewById(R.id.submit);

        final HashMap<String, String> params = new HashMap<>();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();

                String v_make = make.getText().toString();
                String v_model = model.getText().toString();
                int v_year = Integer.parseInt(year.getText().toString());
                int v_price = Integer.parseInt(price.getText().toString());
                String v_license_number = license_number.getText().toString();
                String v_colour = colour.getText().toString();
                int v_number_doors = Integer.parseInt(number_doors.getText().toString());
                String v_transmission = transmission.getText().toString();
                int v_mileage = Integer.parseInt(mileage.getText().toString());
                String v_fuel_type = fuel_type.getText().toString();
                int v_engine_size = Integer.parseInt(engine_size.getText().toString());
                String v_body_style = make.getText().toString();
                String v_condition = make.getText().toString();
                String v_notes = notes.getText().toString();

                Vehicle temp = new Vehicle(1, v_make, v_model,
                        v_year, v_price, v_license_number, v_colour, v_number_doors,
                        v_transmission, v_mileage, v_fuel_type, v_engine_size,
                        v_body_style, v_condition, v_notes);

                String vehicleJson = gson.toJson(temp);

                System.out.println(vehicleJson);

                params.put("json", vehicleJson);

                String url = "http://10.0.2.2:4000/VDB/server";

                PostCall(url, params);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });

    }

    public String PostCall(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            //create connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //POST data to the connection using output stream and buffered writer
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(getPostDataString(postDataParams));

            //clear the writer
            writer.flush();
            writer.close();

            //close output stream
            os.close();

            //get the server response code
            int responseCode = conn.getResponseCode();
            System.out.print("Response Code = " + responseCode);

            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                Toast.makeText(this, "Vehicle Saved", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine()) != null)
                {
                    response += line;
                }
            }
            else {
                Toast.makeText(this, "Error failed to save vehicle", Toast.LENGTH_LONG).show();
                response = "";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("response = " + response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
