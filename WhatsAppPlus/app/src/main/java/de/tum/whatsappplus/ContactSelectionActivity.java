package de.tum.whatsappplus;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactSelectionActivity extends AppCompatActivity {

    private static final String TAG = ContactSelectionActivity.class.getName();
    private static final int SELECT_CONTACTS_REQUEST_CODE = 1337;

    private TableLayout tableLayout;
    private String groupTitle;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Contact fromChat = Constants.contacts.get(getIntent().getStringExtra(Constants.EXTRA_CHAT_ID));
        groupTitle = getIntent().getStringExtra(Constants.EXTRA_GROUP_TITLE);

        contacts = new ArrayList<>();
        contacts.add(fromChat);

        setContentView(R.layout.activity_contact_selection);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        EditText contactNameEditText = (EditText) findViewById(R.id.contactName);
        contactNameEditText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();
        tableLayout.removeAllViews();
        for(Contact c : contacts) {
            View contact_item = getLayoutInflater().inflate(R.layout.view_contact_remove, tableLayout, false);
            ((ImageView) contact_item.findViewById(R.id.chat_icon)).setImageResource(c.imageID);
            ((TextView) contact_item.findViewById(R.id.chat_name)).setText(c.name);
            ImageButton removeButton = (ImageButton) contact_item.findViewById(R.id.removeContact);
            removeButton.setTag(R.string.tag_remove_contact, c.name);
            tableLayout.addView(contact_item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called with reqc=" + requestCode + " resc=" + resultCode + " data=" + data);
        List<Contact> newContacts = new ArrayList<>();
        String[] selectedContacts = data.getStringArrayExtra(Constants.RESULT_SELECTED_CONTACTS);
        if (requestCode == SELECT_CONTACTS_REQUEST_CODE && resultCode == RESULT_OK) {
            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                for (String contactName : selectedContacts) {
                    if (contactName.equals(c.name)) {
                        newContacts.add(c);
                    }
                }
            }
            contacts = newContacts;
        }
    }

    public void onAddContactsClick(View view) {
        Log.d(TAG, "clicked on " + view.getTag(R.string.tag_chat_id));
        Intent contactSelectionIntent = new Intent(this, ContactListActivity.class);
        String[] stringArray = new String[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            stringArray[i] = contacts.get(i).name;
        }

        contactSelectionIntent.putExtra(Constants.EXTRA_PRE_SELECTED_CONTACTS, stringArray);
        startActivityForResult(contactSelectionIntent, SELECT_CONTACTS_REQUEST_CODE);
    }

    public void removeContact(View view) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            if(c.name.equals(view.getTag(R.string.tag_remove_contact))) {
                contacts.remove(c);
                --i;
                tableLayout.removeView((View) view.getParent());
            }
        }
    }

    public void onNextClick(View view) {
        Log.d(TAG, "onNextClick in " + ContactSelectionActivity.class.getSimpleName());
        Intent messageSelectionIntent = new Intent(this, MessageSelectionActivity.class);
        String[] selectedContacts = new String[contacts.size()];
        for(int i = 0; i < selectedContacts.length; i++) {
            selectedContacts[i] = contacts.get(i).name;
        }
        messageSelectionIntent.putExtra(Constants.EXTRA_CONTACTS_ID, selectedContacts);
        messageSelectionIntent.putExtra(Constants.EXTRA_GROUP_TITLE, groupTitle);
        startActivity(messageSelectionIntent);
    }

}
