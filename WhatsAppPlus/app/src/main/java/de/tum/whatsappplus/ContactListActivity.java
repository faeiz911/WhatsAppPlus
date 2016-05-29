package de.tum.whatsappplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private String[] nameArray;
    TableLayout table;
    List<CheckBox> checkBoxList;

    private int selectedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        checkBoxList = new ArrayList<>();
        nameArray = getIntent().getStringArrayExtra("nameArray");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setSubtitle("0 selected");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        table = (TableLayout) findViewById(R.id.contactList);
        if (table != null) {
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                View contact_item = getLayoutInflater().inflate(R.layout.view_contact_item, table, false);
                ((ImageView) contact_item.findViewById(R.id.chat_icon)).setImageResource(c.imageID);
                ((TextView) contact_item.findViewById(R.id.chat_name)).setText(c.name);
                List<String> nameList = Arrays.asList(nameArray);
                CheckBox checkBox = (CheckBox) contact_item.findViewById(R.id.checkBox);
                checkBox.setOnCheckedChangeListener(this);
                checkBox.setTag(R.string.tag_checkbox_id, c.name);
                if(nameList.contains(c.name)) {
                    checkBox.setChecked(true);
                }
                checkBoxList.add(checkBox);
                table.addView(contact_item);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            selectedContacts++;
        else
            selectedContacts--;
        getSupportActionBar().setSubtitle(selectedContacts + " selected");
    }

    public void onDoneClick(View view) {
        List<String> selectedContacts = new ArrayList<>();
        for(CheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                selectedContacts.add((String) checkBox.getTag(R.string.tag_checkbox_id));
            }
        }
        setResult(RESULT_OK, getIntent().putExtra("de.tum.whatsappplus.SelectedContacts", selectedContacts.toArray(new String[selectedContacts.size()])));
        finish();
    }
}
