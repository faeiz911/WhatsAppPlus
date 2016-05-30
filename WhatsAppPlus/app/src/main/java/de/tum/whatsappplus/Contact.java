package de.tum.whatsappplus;

import android.graphics.Color;
import android.util.Log;

import java.util.List;

public class Contact {

    private static final String TAG = Contact.class.getName();

    public String name;
    public int imageID;
    public List<Message> chat;
    public int selectedMessagesAmount;
    public boolean isGroupContact;
    public int color;

    public Contact(String name, int imageID, List<Message> chat, boolean isGroupContact) {
        this.name = name;
        this.imageID = imageID;
        this.chat = chat;
        this.isGroupContact = isGroupContact;
        this.color = Color.HSVToColor(new float[]{(float) (Math.random() * 360), 0.75f, 1f});
        Log.v(TAG, "contact color chosen: " + color);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", imageID=" + imageID +
                ", chat=" + chat +
                ", selectedMessagesAmount=" + selectedMessagesAmount +
                '}';
    }

}
