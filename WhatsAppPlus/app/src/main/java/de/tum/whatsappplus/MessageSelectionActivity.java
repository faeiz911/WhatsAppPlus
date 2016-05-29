package de.tum.whatsappplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MessageSelectionActivity extends AppCompatActivity {

    private String[] selectedContacts;
    private String groupTitle;
    private ArrayList<Contact> contacts;
    private Spinner contactSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_selection);
        selectedContacts = getIntent().getStringArrayExtra(Constants.EXTRA_CONTACTS_ID);
        groupTitle = getIntent().getStringExtra("groupTitle");

        contactSpinner = (Spinner) findViewById(R.id.contactSpinner);
        ArrayAdapter<String > adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, selectedContacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactSpinner.setAdapter(adapter);

        contacts = new ArrayList<>();
        for(String key : Constants.contacts.keySet()) {
            Contact c = Constants.contacts.get(key);
            for(String selected : selectedContacts) {
                if(c.name.equals(selected)) {
                    contacts.add(c);
                }
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void onDoneClick(View view) {

    }
}
