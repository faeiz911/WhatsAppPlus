package de.tum.whatsappplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = ChatListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

//        getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup) findViewById(R.id.chat_list_root), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
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
                ((TextView) chat1.findViewById(R.id.chat_history_last)).setText(lastMessage.text);
                ((TextView) chat1.findViewById(R.id.chat_timestamp)).setText(lastMessage.timeStamp);
                chat1.setTag(R.string.tag_chat_id, c.name);

                table.addView(chat1);
            }
        }
    }

    private Message getLastMessage(List<Message> chat) {
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
        Log.i(TAG, "clicked on " + view.getTag(R.string.tag_chat_id));
        Intent openChat = new Intent(this, ChatActivity.class);
        openChat.putExtra(Constants.EXTRA_CHAT_ID, (String) view.getTag(R.string.tag_chat_id));
        startActivity(openChat);
    }

}
