package de.tum.whatsappplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class GroupCreationActivity extends AppCompatActivity {

    private EditText groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        TextView description = (TextView) findViewById(R.id.descriptionView);
        description.setText("Please provide group subject and optional group icon...");

        groupTitle  = (EditText) findViewById(R.id.groupTitle);

        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().a

        //ImageButton iconButton = (ImageButton) findViewById(R.id.imageButton);
    }
}
