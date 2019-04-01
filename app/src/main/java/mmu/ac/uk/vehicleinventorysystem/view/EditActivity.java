package mmu.ac.uk.vehicleinventorysystem.view;

import android.content.Intent;
import android.os.AsyncTask;
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

public class EditActivity extends AppCompatActivity {

    HashMap<String, String> params = new HashMap<>();
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        final Vehicle vehicle = (Vehicle) extras.get("vehicle");
        System.out.println("received from the intent: "+ vehicle.toString());

        final EditText make = (EditText) findViewById(R.id.make);
        make.setHint("Make : " + vehicle.getMake());

        final EditText model = (EditText) findViewById(R.id.model);
        model.setHint("Model : " + vehicle.getModel());

        final EditText year = (EditText) findViewById(R.id.year);
        year.setHint("Year : " + vehicle.getYear());

        final EditText price = (EditText) findViewById(R.id.price);
        price.setHint("Price : " + vehicle.getPrice());

        final EditText license_number = (EditText) findViewById(R.id.license_number);
        license_number.setHint("License Number : " + vehicle.getLicense_number());

        final EditText colour = (EditText) findViewById(R.id.colour);
        colour.setHint("Colour : " + vehicle.getColour());

        final EditText number_doors = (EditText) findViewById(R.id.number_doors);
        number_doors.setHint("Number of Doors : " + vehicle.getNumber_doors());

        final EditText transmission = (EditText) findViewById(R.id.transmission);
        transmission.setHint("Transmission : " + vehicle.getTransmission());

        final EditText mileage = (EditText) findViewById(R.id.mileage);
        mileage.setHint("Mileage : " + vehicle.getMileage());

        final EditText fuel_type = (EditText) findViewById(R.id.fuel_type);
        fuel_type.setHint("Fuel Type : " + vehicle.getFuel_type());

        final EditText engine_size = (EditText) findViewById(R.id.engine_size);
        engine_size.setHint("Engine Size : " + vehicle.getEngine_size());

        final EditText body_style = (EditText) findViewById(R.id.body_style);
        body_style.setHint("Body Style : " + vehicle.getBody_style());

        final EditText condition = (EditText) findViewById(R.id.condition);
        condition.setHint("Condition : " + vehicle.getCondition());

        final EditText notes = (EditText) findViewById(R.id.notes);
        notes.setHint("Notes : " + vehicle.getNotes());

        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();

                String v_make = (make.getText().toString().equals("")) ? vehicle.getMake() : make.getText().toString();
                String v_model = (model.getText().toString().equals("")) ? vehicle.getModel() : model.getText().toString();

                int v_year = (year.getText().toString().equals("")) ? vehicle.getYear() : Integer.parseInt(year.getText().toString());
                int v_price = (price.getText().toString().equals("")) ? vehicle.getPrice() : Integer.parseInt(price.getText().toString());

                String v_license_number = (license_number.getText().toString().equals("")) ? vehicle.getLicense_number() : license_number.getText().toString();
                String v_colour = (colour.getText().toString().equals("")) ?  vehicle.getColour() : colour.getText().toString();

                int v_number_doors = (number_doors.getText().toString().equals("")) ? vehicle.getNumber_doors() : Integer.parseInt(number_doors.getText().toString());

                String v_transmission = (transmission.getText().toString().equals("")) ?  vehicle.getTransmission() : transmission.getText().toString();

                int v_mileage = (mileage.getText().toString().equals("")) ? vehicle.getMileage() : Integer.parseInt(mileage.getText().toString());

                String v_fuel_type = (fuel_type.getText().toString().equals("")) ? vehicle.getFuel_type() : fuel_type.getText().toString();

                int v_engine_size = (engine_size.getText().toString().equals("")) ? vehicle.getEngine_size() : Integer.parseInt(engine_size.getText().toString());

                String v_body_style = (body_style.getText().toString().equals("")) ? vehicle.getBody_style() : body_style.getText().toString();
                String v_condition = (condition.getText().toString().equals("")) ? vehicle.getCondition() : condition.getText().toString();
                String v_notes = (notes.getText().toString().equals("")) ?  vehicle.getNotes() : notes.getText().toString();

                Vehicle temp = new Vehicle(vehicle.getVehicle_id(), v_make, v_model,
                        v_year, v_price, v_license_number, v_colour, v_number_doors,
                        v_transmission, v_mileage, v_fuel_type, v_engine_size,
                        v_body_style, v_condition, v_notes);

                String vehicleJson = gson.toJson(temp);

                System.out.println(vehicleJson);

                params.put("json", vehicleJson);

                url = "http://10.0.2.2:4000/VDB/server";

                Edit put = new Edit();
                put.execute();
            }
        });
    }

    /*
    * Does a HTTP doPut request
    * @param URL, Parameters
     */
    public void PutCall(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;
        try {
            url = new URL(requestURL);

            //create connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //POST data to the connection using output stream and buffered writer
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(getDataString(postDataParams));

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
            }
            else {
                Toast.makeText(this, "Error failed to save vehicle", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Converts HashMap to String value
     * @param Parameters
     * @return String Value
     */
    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException
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

    private class Edit extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            System.out.println("Start Putting!");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Putting...");
            PutCall(url, params);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            System.out.println("Finished Putting");
        }
    }
}
