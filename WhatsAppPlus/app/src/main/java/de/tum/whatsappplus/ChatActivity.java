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
        Log.d(TAG, "onCreate called with " + savedInstanceState);

        Intent intent = getIntent();
        String chatType, chatId;

        if (savedInstanceState == null) {
            chatType = intent.getStringExtra(Constants.EXTRA_CHAT_TYPE);
            chatId = intent.getStringExtra(Constants.EXTRA_CHAT_ID);
        } else {
            chatType = savedInstanceState.getString(Constants.EXTRA_CHAT_TYPE);
            chatId = savedInstanceState.getString(Constants.EXTRA_CHAT_ID);
        }

        isGroupChat = Constants.CHAT_TYPE_GROUP.equals(chatType);
        if (isGroupChat) {
            String[] contacts = intent.getStringArrayExtra(Constants.EXTRA_CONTACTS_ID);
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
                TextView chatAuthorTextView = (TextView) chatItem.findViewById(R.id.chat_author);
                chatAuthorTextView.setText(message.author);
                chatAuthorTextView.setTextColor(Constants.contacts.get(message.author).color);
            } else {
                chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);
            }
            ((TextView) chatItem.findViewById(R.id.chat_timestamp)).setText(message.timeStamp);
        }
        ((TextView) chatItem.findViewById(R.id.chat_message)).setText(message.text);

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

            Message message = new Message(Constants.AUTHOR_SELF, messageText, Constants.getCurrentTimeStamp());

            addNewChatMessage(message);

            if (contact != null) {
                contact.chat.add(message);
            }

            editable.clear();
        }
    }

    public void onConvertToGroupClick(MenuItem menuItem) {
        Log.d(TAG, "onConvertToGroupClick with action: " + menuItem.getItemId());
        Intent convertToGroupIntent = new Intent(this, GroupCreationActivity.class);
        convertToGroupIntent.putExtra(Constants.EXTRA_CHAT_ID, contact.name);
        startActivity(convertToGroupIntent);
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
        boolean chatItemSelected = message.selected;
        if (Constants.AUTHOR_SELF.equals(message.author)) {
            selectSelfMessage(!chatItemSelected, messageContent, chatItem);
        } else if (!Constants.AUTHOR_SYSTEM.equals(message.author)){
            selectOtherMessage(!chatItemSelected, messageContent, chatItem);
        } else {
            return;
        }

        message.selected = !message.selected;

        // change of action bar
        if (selectedMessages > 0) {
            // toolbar is different, load it
            if (toolbar.getId() != R.id.toolbar_selectionmode) {
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
            selectedMessages++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
        } else {
            selectedMessages--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void selectOtherMessage(boolean toggle, View messageContent, View chatItem) {
        if (toggle) {
            selectedMessages++;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
        } else {
            selectedMessages--;
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    public void onBackButtonClick(View view) {
        finish();
    }

}
