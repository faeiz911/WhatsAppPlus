package de.tum.whatsappplus;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

public class ContactSelectionActivity extends AppCompatActivity {

    private static final String TAG = ContactSelectionActivity.class.getName();
    private TableLayout tableLayout;
    private EditText contactName;
    private String groupTitle;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        contactName = (EditText) findViewById(R.id.contactName);
        contactName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);
        contacts = new ArrayList<>();
        Contact fromChat = Constants.contacts.get(getIntent().getStringExtra(Constants.EXTRA_CHAT_ID));
        contacts.add(fromChat);
        groupTitle = getIntent().getStringExtra("groupTitle");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO Add contacts to table
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Contact> newContacts = new ArrayList<>();
        String[] selectedContacts = data.getStringArrayExtra("de.tum.whatsappplus.SelectedContacts");
        if(requestCode == 1337 && resultCode == RESULT_OK) {
           for(String key : Constants.contacts.keySet()) {
               Contact c = Constants.contacts.get(key);
               for(String contactName : selectedContacts) {
                   if(contactName.equals(c.name)) {
                       newContacts.add(c);
                   }
               }
           }
        }
        contacts = newContacts;
    }

    public void openContacts(View view) {
        Log.i(TAG, "clicked on " + view.getTag(R.string.tag_chat_id));
        Intent contactSelection = new Intent(this, ContactListActivity.class);
        String[] stringArray = new String[contacts.size()];
        for(int i=0; i < contacts.size(); i++) {
            stringArray[i] = contacts.get(i).name;
        }

        contactSelection.putExtra("nameArray", stringArray);
        startActivityForResult(contactSelection, 1337);
    }

    public void onNextClick(View view) {
        Log.i(TAG, "onNextClick in " + ContactSelectionActivity.class.getSimpleName());
    }

}
