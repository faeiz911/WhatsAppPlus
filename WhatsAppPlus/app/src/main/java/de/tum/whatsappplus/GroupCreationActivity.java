package de.tum.whatsappplus;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GroupCreationActivity extends AppCompatActivity {

    private static final String TAG = GroupCreationActivity.class.getName();
    private EditText groupTitle;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        chatId = getIntent().getStringExtra(Constants.EXTRA_CHAT_ID);
        Log.i(TAG, "Group creation activity started"  + (chatId != null ? " from chat with '" + chatId + "'." : "."));

        // test that indeed the correct messages are selected:
//        List<Message> contactMessages = Constants.contacts.get(chatId).chat;
//        for (Message m : contactMessages) {
//            if (m.selected)
//                Log.i(TAG, m.text);
//        }

        TextView description = (TextView) findViewById(R.id.group_creation_content_desc);
        description.setText("Please provide group subject and optional group icon...");

        groupTitle  = (EditText) findViewById(R.id.group_creation_content_input);
        groupTitle.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.group_creation_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.group_creation_root, new ClickInterceptorOverlayFragment(), "click_interceptor").commit();
    }

    public void onNextClick(View view) {
        int titleLength = groupTitle.getText().length();
        if (titleLength < 1) {
            Log.i(TAG, "Attempted to create group with no title.");
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent openContactSelection = new Intent(this, ContactSelectionActivity.class);

        char[] groupTitleChars = new char[titleLength];
        groupTitle.getText().getChars(0, titleLength, groupTitleChars, 0);
        String groupTitleString = new String(groupTitleChars);

        Log.i(TAG, "Clicked on Next in group creation activity with entered group title '" + groupTitleString + "'.");
        openContactSelection.putExtra(Constants.EXTRA_GROUP_TITLE, groupTitleString);
        openContactSelection.putExtra(Constants.EXTRA_CHAT_ID, chatId);
        openContactSelection.putExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES, getIntent().getStringArrayExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES));

        startActivity(openContactSelection);
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
