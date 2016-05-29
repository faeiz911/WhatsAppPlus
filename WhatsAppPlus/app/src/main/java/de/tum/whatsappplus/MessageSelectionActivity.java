package de.tum.whatsappplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] selectedContacts;
    private String groupTitle;
    private Spinner contactSpinner;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_selection);
        selectedContacts = getIntent().getStringArrayExtra(Constants.EXTRA_CONTACTS_ID);
        groupTitle = getIntent().getStringExtra("groupTitle");
        table = (TableLayout) findViewById(R.id.messageList);

        contactSpinner = (Spinner) findViewById(R.id.contactSpinner);
        List<String> contacts = new ArrayList<>();
        for(String contact : selectedContacts) {
            Contact c = Constants.contacts.get(contact);
            if(c.chat != null) {
                contacts.add(c.name);
            }
        }
        ArrayAdapter<String > adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contacts.toArray(new String[contacts.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactSpinner.setAdapter(adapter);
        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                table.removeAllViews();
                String contact = (String) ((TextView) selectedItemView).getText();
                List<Message> messages = Constants.contacts.get(contact).chat;
                for(Message m : messages) {
                    View messageView = addNewChatMessage(m);
                    if(m.selected) {
                        if(m.author.equals("self")) {
                            selectSelfMessage(true, messageView);
                        } else {
                            selectOtherMessage(true, messageView);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onCreateClick(View view) {
        Intent startGroupChatActivity = new Intent(this, ChatActivity.class);
        startGroupChatActivity.putExtra(Constants.EXTRA_CHAT_TYPE, "group");
        startGroupChatActivity.putExtra(Constants.EXTRA_GROUP_TITLE, groupTitle);
        startGroupChatActivity.putExtra(Constants.EXTRA_CONTACTS_ID, selectedContacts);
        startActivity(startGroupChatActivity);
    }

    private View addNewChatMessage(Message message) {
        View chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);
        ((TextView) chatItem.findViewById(R.id.chat_message)).setText(message.text);
        ((TextView) chatItem.findViewById(R.id.chat_timestamp)).setText(message.timeStamp);

        View chatMessageContent = chatItem.findViewById(R.id.chat_message_content);

        TableLayout.LayoutParams chatItemLayoutParams = (TableLayout.LayoutParams) chatItem.getLayoutParams();
        chatItemLayoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constants.MESSAGE_MARGIN_TOP, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams chatMessageContentLayoutParams = (LinearLayout.LayoutParams) chatMessageContent.getLayoutParams();

        if (message.author.equals("self")) {
            chatMessageContentLayoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            chatMessageContentLayoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            chatMessageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
        } else {
            chatMessageContentLayoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            chatMessageContentLayoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            chatMessageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
        }

        chatItem.setTag(R.string.tag_chat_message, message);
        chatMessageContent.setOnClickListener(this);

        chatMessageContent.setLayoutParams(chatMessageContentLayoutParams);

        table.addView(chatItem, chatItemLayoutParams);
        return chatItem;
    }

    @Override
    public void onClick(View v) {
        selectOrDeselectMessage(v);
    }

    private void selectOrDeselectMessage(View messageContent) {
        View chatItem = (View) messageContent.getParent();
        Message message = (Message) chatItem.getTag(R.string.tag_chat_message);
        boolean chatItemSelected = message.selected;
        if ("self".equals(message.author)) {
            selectSelfMessage(!chatItemSelected, chatItem);
        } else {
            selectOtherMessage(!chatItemSelected, chatItem);
        }

        message.selected = !message.selected;
    }

    private void selectSelfMessage(boolean toggle, View chatItem) {
        View messageContent = chatItem.findViewById(R.id.chat_message_content);
        if (toggle) {
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
        } else {
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void selectOtherMessage(boolean toggle, View chatItem) {
        View messageContent = chatItem.findViewById(R.id.chat_message_content);
        if (toggle) {
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other_sel));
            chatItem.setBackgroundColor(getResources().getColor(R.color.color_chat_item_background_sel));
        } else {
            messageContent.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
            chatItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }
}
