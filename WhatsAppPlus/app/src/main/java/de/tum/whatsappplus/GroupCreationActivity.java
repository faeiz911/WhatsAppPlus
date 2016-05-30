package de.tum.whatsappplus;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GroupCreationActivity extends AppCompatActivity {

    private static final String TAG = GroupCreationActivity.class.getName();
    private EditText groupTitle;
    private String chat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        chat_id = getIntent().getStringExtra(Constants.EXTRA_CHAT_ID);

        // test that indeed the correct messages are selected:
//        List<Message> contactMessages = Constants.contacts.get(chat_id).chat;
//        for (Message m : contactMessages) {
//            if (m.selected)
//                Log.i(TAG, m.text);
//        }

        TextView description = (TextView) findViewById(R.id.descriptionView);
        description.setText("Please provide group subject and optional group icon...");

        groupTitle  = (EditText) findViewById(R.id.groupTitle);
        groupTitle.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ImageButton iconButton = (ImageButton) findViewById(R.id.imageButton);
    }

    public void onNextClick(View view) {
        int titleLength = groupTitle.getText().length();
        if (titleLength < 1) {
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show();
        } else {
            Intent openContactSelection = new Intent(this, ContactSelectionActivity.class);

            char[] groupTitleChars = new char[titleLength];
            groupTitle.getText().getChars(0, titleLength, groupTitleChars, 0);
            openContactSelection.putExtra(Constants.EXTRA_GROUP_TITLE, new String(groupTitleChars));
            openContactSelection.putExtra(Constants.EXTRA_CHAT_ID, chat_id);
            openContactSelection.putExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES, getIntent().getStringArrayExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES));

            startActivity(openContactSelection);
        }
    }
}
