package de.tum.whatsappplus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = ChatActivity.class.getName();

    private Toolbar toolbar;
    private EditText chatInputEditText;
    private TableLayout table;

    private Contact contact;
    private int selectedMessagesCount;

    private boolean isGroupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String chatType, chatId;

        if (savedInstanceState == null) {
            chatType = intent.getStringExtra(Constants.EXTRA_CHAT_TYPE);
            chatId = intent.getStringExtra(Constants.EXTRA_CHAT_ID);
        } else {
            chatType = savedInstanceState.getString(Constants.EXTRA_CHAT_TYPE);
            chatId = savedInstanceState.getString(Constants.EXTRA_CHAT_ID);
        }

        Log.i(TAG, "Chat activity for '" + chatId + "' started.");

        isGroupChat = Constants.CHAT_TYPE_GROUP.equals(chatType);
        String contactsConcatString = "";
        if (isGroupChat) {
            String[] contacts = intent.getStringArrayExtra(Constants.EXTRA_CONTACTS_ID);
            for (String contact : contacts) {
                contactsConcatString += contact + ", ";
            }
            Log.i(TAG, "Chat activity is a group chat for " + contactsConcatString.substring(0, contactsConcatString.length() - 2));
        }
        contact = Constants.contacts.get(chatId);

        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        ImageView toolbarIcon = (ImageView) toolbar.findViewById(R.id.chat_toolbar_icon);
        toolbarIcon.setImageDrawable(getResources().getDrawable(contact.imageID));
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.chat_toolbar_content_title);
        toolbarTitle.setText(contact.name);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
                @Override
                public void onMenuVisibilityChanged(boolean isVisible) {
                    if (!isVisible)
                        Log.i(Constants.TAG_CLICK_COUNTER, "Options menu popup hidden");
                }
            });

        table = (TableLayout) findViewById(R.id.chat_table);
        table.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.v(TAG, "layout changed: " + left + " t=" + top + " r=" + right + " b=" + bottom + " oL=" + oldLeft + " oT=" + oldTop + " oR=" + oldRight + " oB=" + oldBottom);
                ScrollView scrollView = (ScrollView) findViewById(R.id.chat_scrollview);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        if (isGroupChat) {
            TextView toolbarSubtitle = (TextView) toolbar.findViewById(R.id.chat_toolbar_content_subtitle);
            toolbarSubtitle.setText(contactsConcatString + "You");
            toolbarSubtitle.setVisibility(View.VISIBLE);
        }
        for (Message message : contact.chat) {
            addNewChatMessage(message);
        }

        chatInputEditText = (EditText) findViewById(R.id.chat_input_edittext);
        if (chatInputEditText != null) {
            chatInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ImageButton sendButton = (ImageButton) findViewById(R.id.chat_input_voice_send);
                    if (sendButton != null)
                        if (s.length() > 0) {
                            sendButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_white_24dp));
                        } else {
                            sendButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_white_24dp));
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.chat_root, new ClickInterceptorOverlayFragment(), "click_interceptor").commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.EXTRA_CHAT_TYPE, isGroupChat ? Constants.CHAT_TYPE_GROUP : "");
        outState.putString(Constants.EXTRA_CHAT_ID, contact.name);
        super.onSaveInstanceState(outState);
    }

    private void addNewChatMessage(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View chatItem;
                if (Constants.AUTHOR_SYSTEM.equals(message.author)) {
                    chatItem = getLayoutInflater().inflate(R.layout.view_chat_item_system, table, false);
                } else {
                    if (contact.isGroupContact && !Constants.AUTHOR_SELF.equals(message.author)) {
                        chatItem = getLayoutInflater().inflate(R.layout.view_chat_item_group, table, false);
                        TextView chatAuthorTextView = (TextView) chatItem.findViewById(R.id.chat_item_content_author);
                        chatAuthorTextView.setText(message.author);
                        chatAuthorTextView.setTextColor(Constants.contacts.get(message.author).color);
                    } else {
                        chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);
                    }
                    ((TextView) chatItem.findViewById(R.id.chat_item_content_timestamp)).setText(message.timeStamp);
                }
                ((TextView) chatItem.findViewById(R.id.chat_item_content_text)).setText(message.text);

                View chatMessageContent = chatItem.findViewById(R.id.chat_item_content);

                TableLayout.LayoutParams chatItemLayoutParams = (TableLayout.LayoutParams) chatItem.getLayoutParams();
                chatItemLayoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constants.MESSAGE_MARGIN_TOP, getResources().getDisplayMetrics());

                LinearLayout.LayoutParams chatMessageContentLayoutParams = (LinearLayout.LayoutParams) chatMessageContent.getLayoutParams();

                if (Constants.AUTHOR_SELF.equals(message.author)) {
                    chatMessageContentLayoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    chatMessageContentLayoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    chatMessageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
                } else if (!Constants.AUTHOR_SYSTEM.equals(message.author)){
                    chatMessageContentLayoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    chatMessageContentLayoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    chatMessageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
                }

                chatItem.setTag(R.string.tag_chat_message, message);
                chatItem.setTag(R.string.tag_selected, false);
                if (!Constants.AUTHOR_SYSTEM.equals(message.author)) {
                    chatMessageContent.setOnLongClickListener(ChatActivity.this);
                    chatMessageContent.setOnClickListener(ChatActivity.this);
                }

                chatMessageContent.setLayoutParams(chatMessageContentLayoutParams);

                table.addView(chatItem, chatItemLayoutParams);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        if (toolbar.getId() == R.id.toolbar_chat_selectionmode)
            inflater.inflate(R.menu.menu_chat_selectionmode, menu);
        else {
            if (isGroupChat)
                inflater.inflate(R.menu.menu_chat_group, menu);
            else
                inflater.inflate(R.menu.menu_chat, menu);
        }

        menu.setGroupVisible(R.id.group_convert_to_group, PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_whatsapp_plus_features), false));

        return true;
    }

    public void onSendClick(View view) {
        Editable editable = chatInputEditText.getText();
        if (editable.length() > 0) {
            char[] messageTextChars = new char[editable.length()];
            editable.getChars(0, editable.length(), messageTextChars, 0);
            String messageText = new String(messageTextChars);

            Log.i(TAG, "Sent message '" + messageText + "' to " + contact.name + ".");

            Message message = new Message(Constants.AUTHOR_SELF, messageText, Constants.getCurrentTimeStamp());

            addNewChatMessage(message);

            if (contact != null) {
                contact.chat.add(message);
            }

            editable.clear();

            getReaction(messageText);
        } else {
            notImplemented(view);
        }
    }

    public void onConvertToGroupClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_convert_to_group)
            Log.i(TAG, "Clicked on 'Convert to group' in standard options menu (without selected messages).");
        else if (menuItem.getItemId() == R.id.action_convert_to_group_with_messages) {
            Log.i(TAG, "Clicked on 'Convert to group' in action bar (while having messages selected).");
        }

        Intent convertToGroupIntent = new Intent(this, GroupCreationActivity.class);
        convertToGroupIntent.putExtra(Constants.EXTRA_CHAT_ID, contact.name);
        convertToGroupIntent.putExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES, getSelectedMessagesIDs());
        startActivity(convertToGroupIntent);

        deselectAllMessages();
    }

    private String[] getSelectedMessagesIDs() {
        List<String> selectedMessagesIDs = new ArrayList<>();
        for (int i = 0; i < table.getChildCount(); i++) {
            View chatItem = table.getChildAt(i);
            Message message = (Message) chatItem.getTag(R.string.tag_chat_message);
            if (!Constants.AUTHOR_SYSTEM.equals(message.author)) {
                if ((boolean) chatItem.getTag(R.string.tag_selected)) {
                    selectedMessagesIDs.add(message.id);
                }
            }
        }
        return selectedMessagesIDs.toArray(new String[selectedMessagesIDs.size()]);
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "long click: " + v);

        selectOrDeselectMessage(v);
        return true;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "click: " + v);

        if (selectedMessagesCount > 0) {
            selectOrDeselectMessage(v);
        }
    }

    private void selectOrDeselectMessage(View messageContent) {
        View chatItem = (View) messageContent.getParent();
        Message message = (Message) chatItem.getTag(R.string.tag_chat_message);
        boolean chatItemSelected = (boolean) chatItem.getTag(R.string.tag_selected);
        if (Constants.AUTHOR_SELF.equals(message.author)) {
            selectSelfMessage(!chatItemSelected, messageContent, chatItem);
        } else if (!Constants.AUTHOR_SYSTEM.equals(message.author)){
            selectOtherMessage(!chatItemSelected, messageContent, chatItem);
        } else {
            return;
        }

        // change of action bar
        if (selectedMessagesCount > 0) {
            // toolbar is different, load it
            if (toolbar.getId() != R.id.toolbar_chat_selectionmode) {
                Log.i(TAG, "Selection mode initiated.");
                ViewGroup chatRoot = (ViewGroup) findViewById(R.id.chat_root);
                toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar_chat_selectionmode, chatRoot, false);
                chatRoot.addView(toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.color_toolbar_selection_primary_dark));
                }
            }
            getSupportActionBar().setTitle(Integer.toString(selectedMessagesCount));
        } else {
            // toolbar is different, load it
            if (toolbar.getId() != R.id.chat_list_toolbar) {
                Log.i(TAG, "Selection mode ended.");
                ViewGroup chatRoot = (ViewGroup) findViewById(R.id.chat_root);
                chatRoot.removeView(toolbar);
                toolbar = (Toolbar) chatRoot.findViewById(R.id.chat_list_toolbar);
                setSupportActionBar(toolbar);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
    }

    private void selectSelfMessage(boolean toggle, View messageContent, View chatItem) {
        if (toggle) {
            Log.i(TAG, "Selected own message '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_timestamp)).getText() + "'.");
            selectedMessagesCount++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
            chatItem.setTag(R.string.tag_selected, true);
        } else {
            Log.i(TAG, "Deselected own message '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_timestamp)).getText() + "'.");
            selectedMessagesCount--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            chatItem.setTag(R.string.tag_selected, false);
        }
    }

    private void selectOtherMessage(boolean toggle, View messageContent, View chatItem) {
        if (toggle) {
            Log.i(TAG, "Selected " + (contact.isGroupContact ? ((TextView)messageContent.findViewById(R.id.chat_item_content_author)).getText() : contact.name) + "'s message '"
                    + ((TextView)messageContent.findViewById(R.id.chat_item_content_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_timestamp)).getText() + "'.");
            selectedMessagesCount++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
            chatItem.setTag(R.string.tag_selected, true);
        } else {
            Log.i(TAG, "Deselected " + (contact.isGroupContact ? ((TextView)messageContent.findViewById(R.id.chat_item_content_author)).getText() : contact.name) + "'s message '"
                    + ((TextView)messageContent.findViewById(R.id.chat_item_content_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_item_content_timestamp)).getText() + "'.");
            selectedMessagesCount--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            chatItem.setTag(R.string.tag_selected, false);
        }
    }

    private void getReaction(String messageText) {
        final String reactionMessageText;
        if (messageText.toLowerCase().matches(".*(lust.*grillen|grillen.*lust).*")) {
            reactionMessageText = "Das hört sich gut an, habe heute noch nichts vor :)";
        } else if (messageText.toLowerCase().matches(".*einladen.*")) {
            reactionMessageText = "Ja! Wie wäre es, wenn wir auch noch " + getAdditionalPeopleToInviteString(messageText) + " einladen?";
        } else if (messageText.toLowerCase().matches(".*(wann.*treffen|treffen.*wann).*")) {
            reactionMessageText = "Sagen wir um 15 Uhr, am üblichen Ort?";
        } else if (messageText.toLowerCase().matches(".*(abgemacht.*gruppe|gruppe.*abgemacht).*")) {
            reactionMessageText = "Ok";
        } else {
            reactionMessageText = "";
        }

        if (!reactionMessageText.isEmpty()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message(contact.name, reactionMessageText, Constants.getCurrentTimeStamp());

                    addNewChatMessage(message);

                    if (contact != null) {
                        contact.chat.add(message);
                    }
                }
            }, 3000);
        }
    }

    private String getAdditionalPeopleToInviteString(String messageText) {
        List<Contact> peopleToInvite = new ArrayList<>(3);
        peopleToInvite.add(contact);

        for (int i = 0; i < 2; i++) {
            Contact c;
            do {
                c = Constants.getRandomContact();
            } while (peopleToInvite.contains(c) || messageText.toLowerCase().contains(c.name.toLowerCase()));
            peopleToInvite.add(c);
        }
        return peopleToInvite.get(1).name + " und " + peopleToInvite.get(2).name;
    }

    public void onHelpMenuItemClick(MenuItem item) {
        showHelpFragment();
    }

    @Override
    public void onBackPressed() {
        Log.i(Constants.TAG_CLICK_COUNTER, "Back pressed");
        if (selectedMessagesCount > 0) {
            deselectAllMessages();
        } else if (!dismissHelpFragment())
            super.onBackPressed();
    }

    private void deselectAllMessages() {
        for (int i = 0; i < table.getChildCount(); i++) {
            View chatItem = table.getChildAt(i);
            if ((boolean)chatItem.getTag(R.string.tag_selected)) {
                selectOrDeselectMessage(chatItem.findViewById(R.id.chat_item_content));
            }
        }
    }

    public void onBackButtonClick(View view) {
        Log.i(TAG, "Left chat activity.");
        finish();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Left chat activity.");
        super.onStop();
    }

    public void onHelpFragmentClick(View view) {
        dismissHelpFragment();
    }

    private void showHelpFragment() {
        Fragment helpFragment = new HelpFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.add(R.id.chat_root, helpFragment, "helpfragment").commit();
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

    public void onCopyMessagesClick(MenuItem item) {
        List<Message> selectedMessages = new ArrayList<>();
        for (int i = 0; i < table.getChildCount(); i++) {
            View chatItem = table.getChildAt(i);
            Message message = (Message) chatItem.getTag(R.string.tag_chat_message);
            if (!Constants.AUTHOR_SYSTEM.equals(message.author)) {
                if ((boolean) chatItem.getTag(R.string.tag_selected)) {
                    selectedMessages.add(message);
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        boolean firstMessage = true;
        int copiedMessagesCount = 0;
        for (Message message : selectedMessages) {
            if (!firstMessage)
                stringBuilder.append("\n");
            if (selectedMessages.size() > 1) {
                stringBuilder.append("[").append(message.timeStamp).append("] ");
                stringBuilder.append(message.author.equals(Constants.AUTHOR_SELF) ? "Me" : message.author).append(": ");
            }
            stringBuilder.append(message.text);

            copiedMessagesCount++;
            firstMessage = false;
        }

        if (copiedMessagesCount > 0) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("WhatsAppPlus - Copied messages", stringBuilder.toString()));
            deselectAllMessages();
            Toast.makeText(ChatActivity.this, (copiedMessagesCount > 1 ? copiedMessagesCount + " messages " : "Message ") + "copied", Toast.LENGTH_SHORT).show();
        }
    }
}
