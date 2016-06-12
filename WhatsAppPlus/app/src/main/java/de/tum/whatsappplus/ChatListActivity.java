package de.tum.whatsappplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = ChatListActivity.class.getName();

    private boolean activityJustCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Chat list activity started.");
        activityJustCreated = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_GENERAL, MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean(Constants.PREFERENCE_FIRST_START, true);
        long lastStart = preferences.getLong(Constants.PREFERENCE_LAST_START, 0);
        if (firstStart || System.currentTimeMillis()-lastStart > Constants.HELP_DISPLAY_TIMEOUT) {
            showHelpFragment();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.PREFERENCE_FIRST_START, false);
            editor.putLong(Constants.PREFERENCE_LAST_START, System.currentTimeMillis());
            editor.apply();
        }
    }

    @Override
    protected void onResume() {
        if (!activityJustCreated) Log.i(TAG, "Returned to chat list activity.");
        super.onResume();

        TableLayout table = (TableLayout) findViewById(R.id.chat_list_table);
        if (table != null) {
            table.removeAllViews();
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                if (c.chat == null || c.chat.isEmpty()) continue;

                View chat1 = getLayoutInflater().inflate(R.layout.view_chat_history_item, table, false);
                ((ImageView) chat1.findViewById(R.id.chat_icon)).setImageResource(c.imageID);
                ((TextView) chat1.findViewById(R.id.chat_name)).setText(c.name);
                Message lastMessage = getLastMessage(c.chat);
                if (lastMessage != null) {
                    ((TextView) chat1.findViewById(R.id.chat_history_last)).setText(lastMessage.text);
                    ((TextView) chat1.findViewById(R.id.chat_timestamp)).setText(lastMessage.timeStamp);
                }
                chat1.setTag(R.string.tag_chat_id, c.name);

                table.addView(chat1);
            }
        }
    }

    private Message getLastMessage(List<Message> chat) {
        if (chat == null || chat.isEmpty()) {
            return null;
        }
        int i = chat.size() - 1;
        Message message = chat.get(i);
        while (Constants.AUTHOR_SYSTEM.equals(message.author)) {
            if (i == 0)
                return new Message(Constants.AUTHOR_SYSTEM, "", message.timeStamp);
            message = chat.get(--i);
        }
        return message;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_list, menu);
        return true;
    }

    public void onChatSummaryClick(View view) {
        Log.i(TAG, "Clicked on chat with '" + view.getTag(R.string.tag_chat_id) + "\'.");
        Intent openChat = new Intent(this, ChatActivity.class);
        openChat.putExtra(Constants.EXTRA_CHAT_ID, (String) view.getTag(R.string.tag_chat_id));
        startActivity(openChat);
    }

    public void onNewGroupClick(MenuItem item) {
        Intent newGroupIntent = new Intent(this, GroupCreationActivity.class);
        startActivity(newGroupIntent);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Left chat list activity.");
        super.onStop();
    }

    public void onHelpMenuItemClick(MenuItem item) {
        showHelpFragment();
    }

    public void onHelpFragmentClick(View view) {
        dismissHelpFragment();
    }

    @Override
    public void onBackPressed() {
        if (!dismissHelpFragment())
            super.onBackPressed();
    }

    private void showHelpFragment() {
        Fragment helpFragment = new HelpFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.chat_list_root, helpFragment, "helpfragment").commit();
    }

    private boolean dismissHelpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment helpFragment = fragmentManager.findFragmentByTag("helpfragment");
        if (helpFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.remove(helpFragment).commit();
            return true;
        } else {
            return false;
        }
    }

    public void onFormsLinkClick(View view) {
        String url = ((TextView) view).getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void notImplemented(MenuItem item) {
        notImplemented((View) null);
    }

    public void notImplemented(View view) {
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
    }
}
