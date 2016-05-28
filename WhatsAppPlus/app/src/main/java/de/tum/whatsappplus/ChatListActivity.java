package de.tum.whatsappplus;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        TableLayout table = (TableLayout) findViewById(R.id.chat_list_table);
        if (table != null) {
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (Contact c : Constants.contacts) {
                if (c.chat == null) continue;

                View chat1 = getLayoutInflater().inflate(R.layout.view_chat_history_item, table, false);
                ((ImageView) chat1.findViewById(R.id.chat_icon)).setImageResource(c.imageID);
                ((TextView) chat1.findViewById(R.id.chat_name)).setText(c.name);
                ((TextView) chat1.findViewById(R.id.chat_history_last)).setText(c.chat.get(c.chat.size()-1).text);
                ((TextView) chat1.findViewById(R.id.chat_timestamp)).setText(c.time == null ? "" : c.time);

                table.addView(chat1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_list, menu);
        return true;
    }

}
