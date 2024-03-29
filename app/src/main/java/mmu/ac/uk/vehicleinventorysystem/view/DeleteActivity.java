package mmu.ac.uk.vehicleinventorysystem.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import mmu.ac.uk.vehicleinventorysystem.R;
import mmu.ac.uk.vehicleinventorysystem.model.Vehicle;

public class DeleteActivity extends AppCompatActivity {

    int vehicle_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Bundle extras = getIntent().getExtras();
        final Vehicle vehicle = (Vehicle) extras.get("vehicle");
        System.out.println("received from the intent: "+ vehicle.toString());

        vehicle_id = vehicle.getVehicle_id();

        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete delete = new Delete();

                delete.execute();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });
    }

    /*
    * Does a HTTP doDelete Call
    * @param Vehicle ID
     */
    public void deleteCall(int id)
    {
        URL url;
        try {
            url = new URL("http://10.0.2.2:4000/VDB/server?vehicle_id=" + id);

            //create connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            //get the server response code
            int responseCode = conn.getResponseCode();
            System.out.print("Response Code = " + responseCode);

            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                Toast.makeText(this, "Vehicle Deleted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Error failed to delete vehicle", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class Delete extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            System.out.println("Start Deleting!");
        }

        @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Deleting...");
                deleteCall(vehicle_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            System.out.println("Finished Deleting");
        }
    }
}