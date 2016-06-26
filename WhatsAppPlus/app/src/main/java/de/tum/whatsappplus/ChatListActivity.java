package de.tum.whatsappplus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = ChatListActivity.class.getName();

    private boolean activityJustCreated = false;
    private boolean settingsDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Chat list activity started.");
        activityJustCreated = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        setSupportActionBar((Toolbar) findViewById(R.id.chat_list_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
                @Override
                public void onMenuVisibilityChanged(boolean isVisible) {
                    if (!isVisible)
                        Log.i(Constants.TAG_CLICK_COUNTER, "Options menu popup hidden");
                }
            });

//        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_GENERAL, MODE_PRIVATE);
//        boolean firstStart = preferences.getBoolean(Constants.PREFERENCE_FIRST_START, true);
//        long lastStart = preferences.getLong(Constants.PREFERENCE_LAST_START, 0);
//        if (firstStart || System.currentTimeMillis()-lastStart > Constants.HELP_DISPLAY_TIMEOUT) {
//            showHelpFragment();
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(Constants.PREFERENCE_FIRST_START, false);
//            editor.putLong(Constants.PREFERENCE_LAST_START, System.currentTimeMillis());
//            editor.apply();
//        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.chat_list_content, new ChatListFragment(), "chat_list");
        transaction.add(R.id.chat_list_root, new ClickInterceptorOverlayFragment(), "click_interceptor");
        transaction.commit();
    }

    @Override
    protected void onResume() {
        if (!activityJustCreated) Log.i(TAG, "Returned to chat list activity.");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!settingsDisplayed) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_chat_list, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
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
        Log.i(Constants.TAG_CLICK_COUNTER, "Back pressed");
        if (!dismissHelpFragment())
            super.onBackPressed();
    }

    private void showHelpFragment() {
        Fragment helpFragment = new HelpFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.add(R.id.chat_list_root, helpFragment, "helpfragment").commit();
    }

    private boolean dismissHelpFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment helpFragment = fragmentManager.findFragmentByTag("helpfragment");
        if (helpFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
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

    public void onEmailClick(View view) {
        String mailToUrl;
        switch (view.getTag().toString()) {
            case "michi": mailToUrl = "mailto:mkratzer@mytum.de"; break;
            default:
            case "tom": mailToUrl = "mailto:ladek@in.tum.de";
        }
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse(mailToUrl));
        startActivity(i);
    }

    public void onSettingsClick(MenuItem item) {
        final FragmentManager manager = getFragmentManager();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (manager.getBackStackEntryCount() == 0) {
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(getString(R.string.app_name));
                        actionBar.setDisplayHomeAsUpEnabled(false);
                    }
                    settingsDisplayed = false;
                    invalidateOptionsMenu();
                }
            }
        });
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.chat_list_content, new SettingsFragment(), "settings").addToBackStack(null);
        transaction.commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Settings");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        settingsDisplayed = true;
        invalidateOptionsMenu();
    }

}
