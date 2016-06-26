package de.tum.whatsappplus;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactSelectionActivity extends AppCompatActivity {

    private static final String TAG = ContactSelectionActivity.class.getName();
    private static final int SELECT_CONTACTS_REQUEST_CODE = 1337;

    private TableLayout tableLayout;

    private String groupTitle;
    private String chatId;
    private int groupIcon;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        chatId = intent.getStringExtra(Constants.EXTRA_CHAT_ID);
        groupTitle = intent.getStringExtra(Constants.EXTRA_GROUP_TITLE);
        groupIcon = intent.getIntExtra(Constants.EXTRA_GROUP_ICON, R.drawable.whatsappplus_icon_group);

        Log.i(TAG, "Contact selection activity started." + (chatId != null ? " Pre-selected contact is '" + chatId + "', group title is '" + groupTitle + "'." : ""));

        Contact fromChat = Constants.contacts.get(chatId);

        contacts = new ArrayList<>();
        if (fromChat != null)
            contacts.add(fromChat);

        setContentView(R.layout.activity_contact_selection);
        tableLayout = (TableLayout) findViewById(R.id.contact_selection_content_table);
        EditText contactNameEditText = (EditText) findViewById(R.id.contact_selection_content_input_edittext);
        contactNameEditText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_selection_toolbar);
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_whatsapp_plus_features), false)) {
            TextView textView = (TextView) toolbar.findViewById(R.id.group_creation_toolbar_content_actiondesc);
            textView.setText("CREATE");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.contact_selection_root, new ClickInterceptorOverlayFragment(), "click_interceptor").commit();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();
        tableLayout.removeAllViews();
        for(Contact c : contacts) {
            View contact_item = getLayoutInflater().inflate(R.layout.view_contact_remove, tableLayout, false);
            ((ImageView) contact_item.findViewById(R.id.contact_item_icon)).setImageResource(c.imageID);
            ((TextView) contact_item.findViewById(R.id.contact_item_name)).setText(c.name);
            ImageButton removeButton = (ImageButton) contact_item.findViewById(R.id.contact_item_remove);
            removeButton.setTag(R.string.tag_remove_contact, c.name);
            tableLayout.addView(contact_item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Contact> newContacts = new ArrayList<>();
        if (requestCode == SELECT_CONTACTS_REQUEST_CODE && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            String[] selectedContacts = data.getStringArrayExtra(Constants.RESULT_SELECTED_CONTACTS);
            String selectedContactsConcatString = "";
            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                for (String contactName : selectedContacts) {
                    if (contactName.equals(c.name)) {
                        selectedContactsConcatString += key + ", ";
                        newContacts.add(c);
                    }
                }
            }

            if (!selectedContactsConcatString.isEmpty())
                Log.i(TAG, "Contact list activity returned following contacts: " + selectedContactsConcatString.substring(0, selectedContactsConcatString.length() - 2));
            else
                Log.i(TAG, "Contact list activity returned with no selected contacts.");

            contacts = newContacts;
        }
    }

    public void onAddContactsClick(View view) {
        Log.i(TAG, "Starting contact list activity.");
        Intent contactSelectionIntent = new Intent(this, ContactListActivity.class);
        String[] stringArray = new String[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            stringArray[i] = contacts.get(i).name;
        }

        contactSelectionIntent.putExtra(Constants.EXTRA_PRE_SELECTED_CONTACTS, stringArray);
        startActivityForResult(contactSelectionIntent, SELECT_CONTACTS_REQUEST_CODE);
    }

    public void onRemoveContactClick(View view) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            if(c.name.equals(view.getTag(R.string.tag_remove_contact))) {
                contacts.remove(c);
                --i;
                tableLayout.removeView((View) view.getParent());
                Log.i(TAG, "Removed '" + c.name + "'.");
            }
        }
    }

    public void onNextClick(View view) {
        if (contacts.isEmpty()) {
            Toast.makeText(this, "At least 1 contact must be selected.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Attempted to create group with no participants.");
            return;
        }

        boolean isLastActivityBeforeGroup = !PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_whatsapp_plus_features), false);
        Intent nextIntent;
        if (!isLastActivityBeforeGroup) {
            nextIntent = new Intent(this, MessageSelectionActivity.class);
            nextIntent.putExtra(Constants.EXTRA_CHAT_ID, chatId);
        } else {
            nextIntent = new Intent(this, ChatActivity.class);
            List<Message> groupMessages = new ArrayList<>();
            groupMessages.add(new Message(Constants.AUTHOR_SYSTEM, "You created group \"" + groupTitle + "\".", Constants.getCurrentTimeStamp()));
            Constants.contacts.put(groupTitle, new Contact(groupTitle, groupIcon, groupMessages, true));
            nextIntent.putExtra(Constants.EXTRA_CHAT_ID, groupTitle);
            nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        String[] selectedContacts = new String[contacts.size()];
        String selectedContactsConcatString = "";
        for (int i = 0; i < selectedContacts.length; i++) {
            selectedContacts[i] = contacts.get(i).name;
            selectedContactsConcatString += selectedContacts[i] + ", ";
        }
        if (!selectedContactsConcatString.isEmpty())
            Log.i(TAG, "Clicked on Next in contact selection activity with following contacts selected: " + selectedContactsConcatString.substring(0, selectedContactsConcatString.length() - 2));
        else
            Log.i(TAG, "Clicked on Next in contact selection activity with no selected contacts.");

        nextIntent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHAT_TYPE_GROUP);
        nextIntent.putExtra(Constants.EXTRA_CONTACTS_ID, selectedContacts);
        nextIntent.putExtra(Constants.EXTRA_GROUP_TITLE, groupTitle);
        nextIntent.putExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES, getIntent().getStringArrayExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES));
        startActivity(nextIntent);

        if (isLastActivityBeforeGroup)
            finishAffinity();
    }

    public void notImplemented(MenuItem item) {
        notImplemented((View) null);
    }

    public void notImplemented(View view) {
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Log.i(Constants.TAG_CLICK_COUNTER, "Back pressed");
        super.onBackPressed();
    }
}
