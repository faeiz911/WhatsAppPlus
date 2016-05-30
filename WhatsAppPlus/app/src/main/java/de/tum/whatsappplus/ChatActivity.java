package de.tum.whatsappplus;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = ChatActivity.class.getName();

    private Toolbar toolbar;
    private EditText chatInputEditText;
    private TableLayout table;

    private Contact contact;
    private int selectedMessages;

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
        if (isGroupChat) {
            String[] contacts = intent.getStringArrayExtra(Constants.EXTRA_CONTACTS_ID);
            String contactsConcatString = "";
            for (String contact : contacts) {
                contactsConcatString += contact + ", ";
            }
            Log.i(TAG, "Chat activity is a group chat for " + contactsConcatString.substring(0, contactsConcatString.length() - 2));
        }
        contact = Constants.contacts.get(chatId);

        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView toolbarIcon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        toolbarIcon.setImageDrawable(getResources().getDrawable(Constants.contacts.get(contact.name).imageID));
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(contact.name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        table = (TableLayout) findViewById(R.id.chat_table);
        table.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.v(TAG, "layout changed: " + left + " t=" + top + " r=" + right + " b=" + bottom + " oL=" + oldLeft + " oT=" + oldTop + " oR=" + oldRight + " oB=" + oldBottom);
                ScrollView scrollView = (ScrollView) findViewById(R.id.chat_scrollview);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        if (table != null) {
            getSupportActionBar().setTitle(contact.name);
            for (Message message : contact.chat) {
                addNewChatMessage(message);
            }
        }

        table.requestFocus();

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.EXTRA_CHAT_TYPE, isGroupChat ? Constants.CHAT_TYPE_GROUP : "");
        outState.putString(Constants.EXTRA_CHAT_ID, contact.name);
        super.onSaveInstanceState(outState);
    }

    private void addNewChatMessage(Message message) {
        View chatItem;
        if (Constants.AUTHOR_SYSTEM.equals(message.author)) {
            chatItem = getLayoutInflater().inflate(R.layout.view_chat_item_system, table, false);
        } else {
            if (contact.isGroupContact && !Constants.AUTHOR_SELF.equals(message.author)) {
                chatItem = getLayoutInflater().inflate(R.layout.view_chat_item_group, table, false);
                TextView chatAuthorTextView = (TextView) chatItem.findViewById(R.id.chat_message_author);
                chatAuthorTextView.setText(message.author);
                chatAuthorTextView.setTextColor(Constants.contacts.get(message.author).color);
            } else {
                chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);
            }
            ((TextView) chatItem.findViewById(R.id.chat_message_timestamp)).setText(message.timeStamp);
        }
        ((TextView) chatItem.findViewById(R.id.chat_message_text)).setText(message.text);

        View chatMessageContent = chatItem.findViewById(R.id.chat_message_content);

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
            chatMessageContent.setOnLongClickListener(this);
            chatMessageContent.setOnClickListener(this);
        }

        chatMessageContent.setLayoutParams(chatMessageContentLayoutParams);

        table.addView(chatItem, chatItemLayoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        if (toolbar.getId() == R.id.toolbar_selectionmode)
            inflater.inflate(R.menu.menu_chat_selectionmode, menu);
        else
            inflater.inflate(R.menu.menu_chat, menu);

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
        convertToGroupIntent.putExtra(Constants.EXTRA_PRE_SELECTED_MESSAGES, getSelectedMessages());
        startActivity(convertToGroupIntent);
    }

    private String[] getSelectedMessages() {
        List<String> preSelectedMessages = new ArrayList<>();
        for (int i = 0; i < table.getChildCount(); i++) {
            View chatItem = table.getChildAt(i);
            Message message = (Message) chatItem.getTag(R.string.tag_chat_message);
            if (!Constants.AUTHOR_SYSTEM.equals(message.author)) {
                if ((boolean) chatItem.getTag(R.string.tag_selected)) {
                    preSelectedMessages.add(message.id);
                }
            }
        }
        return preSelectedMessages.toArray(new String[preSelectedMessages.size()]);
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

        if (selectedMessages > 0) {
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
        if (selectedMessages > 0) {
            // toolbar is different, load it
            if (toolbar.getId() != R.id.toolbar_selectionmode) {
                Log.i(TAG, "Selection mode initiated.");
                ViewGroup chatRoot = (ViewGroup) findViewById(R.id.chat_root);
                chatRoot.removeView(toolbar);
                toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar_chat_selectionmode, chatRoot, false);
                chatRoot.addView(toolbar, 0);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.color_toolbar_selection_primary_dark));
                }
            }
            getSupportActionBar().setTitle(Integer.toString(selectedMessages));
        } else {
            // toolbar is different, load it
            if (toolbar.getId() != R.id.toolbar) {
                Log.i(TAG, "Selection mode ended.");
                ViewGroup chatRoot = (ViewGroup) findViewById(R.id.chat_root);
                toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar_chat, chatRoot, false);
                ImageView toolbarIcon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
                toolbarIcon.setImageDrawable(getResources().getDrawable(contact.imageID));
                TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
                toolbarTitle.setText(contact.name);
                chatRoot.removeViewAt(0);
                chatRoot.addView(toolbar, 0);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

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
            Log.i(TAG, "Selected own message '" + ((TextView)messageContent.findViewById(R.id.chat_message_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_message_timestamp)).getText() + "'.");
            selectedMessages++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
            chatItem.setTag(R.string.tag_selected, true);
        } else {
            Log.i(TAG, "Deselected own message '" + ((TextView)messageContent.findViewById(R.id.chat_message_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_message_timestamp)).getText() + "'.");
            selectedMessages--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            chatItem.setTag(R.string.tag_selected, false);
        }
    }

    private void selectOtherMessage(boolean toggle, View messageContent, View chatItem) {
        if (toggle) {
            Log.i(TAG, "Selected " + (contact.isGroupContact ? ((TextView)messageContent.findViewById(R.id.chat_message_author)).getText() : contact.name) + "'s message '"
                    + ((TextView)messageContent.findViewById(R.id.chat_message_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_message_timestamp)).getText() + "'.");
            selectedMessages++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
            chatItem.setTag(R.string.tag_selected, true);
        } else {
            Log.i(TAG, "Deselected " + (contact.isGroupContact ? ((TextView)messageContent.findViewById(R.id.chat_message_author)).getText() : contact.name) + "'s message '"
                    + ((TextView)messageContent.findViewById(R.id.chat_message_text)).getText() +
                    "' from '" + ((TextView)messageContent.findViewById(R.id.chat_message_timestamp)).getText() + "'.");
            selectedMessages--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            chatItem.setTag(R.string.tag_selected, false);
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
}
