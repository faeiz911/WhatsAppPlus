package de.tum.whatsappplus;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class GroupCreationActivity extends AppCompatActivity {

    private static final String TAG = GroupCreationActivity.class.getName();
    private EditText groupTitle;
    private String chat_id = getIntent().getStringExtra(Constants.EXTRA_CHAT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        TextView description = (TextView) findViewById(R.id.descriptionView);
        description.setText("Please provide group subject and optional group icon...");

        groupTitle  = (EditText) findViewById(R.id.groupTitle);
        groupTitle.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_add_button), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ImageButton iconButton = (ImageButton) findViewById(R.id.imageButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group_creation, menu);
        return true;
    }

    public void onNextClick(View view) {
        Log.i(TAG, "clicked on " + view.getTag(R.string.tag_chat_id));
        Intent openContactSelection = new Intent(this, ContactSelectionActivity.class);
        openContactSelection.putExtra("groupTitle", groupTitle.getText());
        openContactSelection.putExtra(Constants.EXTRA_CHAT_ID, chat_id);
        startActivity(openContactSelection);
    }
}
