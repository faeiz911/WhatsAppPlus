package de.tum.whatsappplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getLayoutInflater().inflate(R.layout.activity_chat, , false);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TableLayout table = (TableLayout) findViewById(R.id.chat_table);
        if (table != null) {
            Log.i(TAG, "table=" + table);

            View chatItem = getLayoutInflater().inflate(R.layout.view_chat_item, table, true);
            ((TextView) chatItem.findViewById(R.id.chat_message)).setText("");
        }
    }
}
