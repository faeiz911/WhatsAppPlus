package de.tum.whatsappplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getName();

    private EditText chatInputEditText;
    private TableLayout table;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String chatId = getIntent().getStringExtra(Constants.EXTRA_CHAT_ID);

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
            Log.i(TAG, "table=" + table);

            contact = Constants.contacts.get(chatId);
            for (Message message : contact.chat) {

                View chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);
                ((TextView) chatItem.findViewById(R.id.chat_message)).setText(message.text);
                ((TextView) chatItem.findViewById(R.id.chat_timestamp)).setText(message.timeStamp);

                TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) chatItem.getLayoutParams();
                layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                if (message.author.equals("self")) {
                    layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    chatItem.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));
                } else {
                    layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    chatItem.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_other));
                }

                table.addView(chatItem, layoutParams);
            }
        }

        table.requestFocus();

        chatInputEditText = (EditText) findViewById(R.id.chat_input_edittext);
        if (chatInputEditText != null) {
            chatInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

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
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void onSendClick(View view) {
        Editable editable = chatInputEditText.getText();
        if (editable.length() > 0) {
            View chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, false);

            char[] messageTextChars = new char[editable.length()];
            editable.getChars(0, editable.length(), messageTextChars, 0);
            String messageText = new String(messageTextChars);
            ((TextView) chatItem.findViewById(R.id.chat_message)).setText(messageText);

            Calendar cal = Calendar.getInstance();
            String timeStampString = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            ((TextView) chatItem.findViewById(R.id.chat_timestamp)).setText(timeStampString);

            TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) chatItem.getLayoutParams();
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

            chatItem.setBackground(getResources().getDrawable(R.drawable.drawable_chat_item_background_self));

            table.addView(chatItem);

            if (contact != null) {
                contact.chat.add(new Message("self", messageText, timeStampString));
            }

            editable.clear();
        }
    }

}
