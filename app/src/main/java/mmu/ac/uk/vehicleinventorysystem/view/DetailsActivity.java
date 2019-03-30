package mmu.ac.uk.vehicleinventorysystem.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mmu.ac.uk.vehicleinventorysystem.R;
import mmu.ac.uk.vehicleinventorysystem.model.Vehicle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Button editButton = findViewById(R.id.EditButton);

        // get the intent
        Bundle extras = getIntent().getExtras();
        // create a cheese object from the cheese object that was passed over from
        // the MainActivity. Notice you use the key ('cheese') to retrieve the value/variable needed.
        final Vehicle vehicle = (Vehicle) extras.get("vehicle");
        System.out.println("received from the intent: "+ vehicle.toString());

        TextView heading = findViewById(R.id.Heading);
        heading.setText(vehicle.getVehicle_id() + " : " + vehicle.getMake() + " " + vehicle.getModel());

        TextView body = findViewById(R.id.Body);
        body.setText(vehicle.toString());

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);

                intent.putExtra("vehicle", vehicle);

                startActivity(intent);
            }
        });
    }
}
