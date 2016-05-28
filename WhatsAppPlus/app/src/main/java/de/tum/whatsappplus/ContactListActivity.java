package de.tum.whatsappplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private String[] nameArray;
    TableLayout table;
    List<CheckBox> checkBoxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        checkBoxList = new ArrayList<>();
        nameArray = getIntent().getStringArrayExtra("nameArray");

        table = (TableLayout) findViewById(R.id.contactList);
        if (table != null) {
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                View contact_item = getLayoutInflater().inflate(R.layout.view_contact_item, table, false);
                ((ImageView) contact_item.findViewById(R.id.chat_icon)).setImageResource(c.imageID);
                ((TextView) contact_item.findViewById(R.id.chat_name)).setText(c.name);
                List<String> nameList = new ArrayList<>(Arrays.asList(nameArray));
                CheckBox checkBox = (CheckBox) contact_item.findViewById(R.id.checkBox);
                checkBox.setTag(R.string.tag_checkbox_id, c.name);
                if(nameList.contains(c.name)) {
                    checkBox.setChecked(true);
                }
                checkBoxList.add(checkBox);
                table.addView(contact_item);
            }
        }


    }

    // TODO call this from the DONE-button in the ActionBar
    public void selectionDone() {
        List<String> selectedContacts = new ArrayList<>();
        for(CheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                selectedContacts.add((String) checkBox.getTag(R.string.tag_checkbox_id));
            }
        }
        setResult(RESULT_OK, getIntent().putExtra("de.tum.whatsappplus.SelectedContacts", (String[])selectedContacts.toArray()));
        finish();
    }
}
