package de.tum.whatsappplus;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = ContactListActivity.class.getName();

    private List<CheckBox> checkBoxList;

    private int selectedContacts;

    private boolean loggingSelections = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        checkBoxList = new ArrayList<>();
        String[] preSelectedContacts = getIntent().getStringArrayExtra(Constants.EXTRA_PRE_SELECTED_CONTACTS);

        String selectedContactsConcatString = "";
        for (String preSelectedContact : preSelectedContacts) {
            selectedContactsConcatString += preSelectedContact + ", ";
        }
        if (!selectedContactsConcatString.isEmpty())
            Log.i(TAG, "Contact list activity started with pre-selected contacts: " + selectedContactsConcatString.substring(0, selectedContactsConcatString.length() - 2));
        else
            Log.i(TAG, "Contact list activity started.");

        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setSubtitle("0 selected");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TableLayout table = (TableLayout) findViewById(R.id.contact_list_table);
        if (table != null) {
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (String contactId : Constants.contacts.keySet()) {
                Contact contact = Constants.contacts.get(contactId);
                if (!contact.isGroupContact) {
                    View contact_item = getLayoutInflater().inflate(R.layout.view_contact_item, table, false);
                    ((ImageView) contact_item.findViewById(R.id.contact_item_icon)).setImageResource(contact.imageID);
                    ((TextView) contact_item.findViewById(R.id.contact_item_name)).setText(contact.name);
                    List<String> preSelectedContactsList = Arrays.asList(preSelectedContacts);
                    CheckBox checkBox = (CheckBox) contact_item.findViewById(R.id.contact_item_checkbox);
                    checkBox.setOnCheckedChangeListener(this);
                    checkBox.setTag(R.string.tag_checkbox_id, contact.name);
                    if (preSelectedContactsList.contains(contact.name)) {
                        checkBox.setChecked(true);
                    }
                    checkBoxList.add(checkBox);
                    table.addView(contact_item);
                }
            }
            loggingSelections = true;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.contact_list_root, new ClickInterceptorOverlayFragment(), "click_interceptor").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (loggingSelections) Log.i(TAG, "Selected '" + buttonView.getTag(R.string.tag_checkbox_id) + "'.");
            selectedContacts++;
        } else {
            if (loggingSelections) Log.i(TAG, "Deselected '" + buttonView.getTag(R.string.tag_checkbox_id) + "'.");
            selectedContacts--;
        }
        getSupportActionBar().setSubtitle(selectedContacts + " selected");
    }

    public void onDoneClick(View view) {
        List<String> selectedContacts = new ArrayList<>();
        String selectedContactsConcatString = "";
        for(CheckBox checkBox : checkBoxList) {
            if (checkBox.isChecked()) {
                String contactId = (String) checkBox.getTag(R.string.tag_checkbox_id);
                selectedContacts.add(contactId);
                selectedContactsConcatString += contactId + ", ";
            }
        }
        if (!selectedContactsConcatString.isEmpty())
            Log.i(TAG, "Clicked on Done in contact list activity with following contacts selected: " + selectedContactsConcatString.substring(0, selectedContactsConcatString.length() - 2));
        else
            Log.i(TAG, "Clicked on Done in contact list activity with no selected contacts.");

        setResult(RESULT_OK, getIntent().putExtra(Constants.RESULT_SELECTED_CONTACTS, selectedContacts.toArray(new String[selectedContacts.size()])));
        finish();
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
        setResult(RESULT_CANCELED, getIntent().putExtra(Constants.RESULT_SELECTED_CONTACTS, getIntent().getStringArrayExtra(Constants.EXTRA_PRE_SELECTED_CONTACTS)));
        finish();
    }
}
