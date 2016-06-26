package de.tum.whatsappplus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

public class ChatListFragment extends Fragment{

    private static final String TAG = ChatListFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        TableLayout table = (TableLayout) getActivity().findViewById(R.id.chat_list_table);
        if (table != null) {
            table.removeAllViews();
            table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            for (String key : Constants.contacts.keySet()) {
                Contact c = Constants.contacts.get(key);
                if (c.chat == null || c.chat.isEmpty()) continue;

                View chat1 = getActivity().getLayoutInflater().inflate(R.layout.view_chat_list_item, table, false);
                ((ImageView) chat1.findViewById(R.id.chat_list_item_icon)).setImageResource(c.imageID);
                ((TextView) chat1.findViewById(R.id.chat_list_item_text_name)).setText(c.name);
                Message lastMessage = getLastMessage(c.chat);
                if (lastMessage != null) {
                    ((TextView) chat1.findViewById(R.id.chat_list_item_text_lastmessage)).setText(lastMessage.text);
                    ((TextView) chat1.findViewById(R.id.chat_list_item_timestamp)).setText(lastMessage.timeStamp);
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
}
