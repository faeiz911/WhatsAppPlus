package de.tum.whatsappplus;

import java.util.ArrayList;

public class Contact {
    public String name;
    public int imageID;
    public ArrayList<Message> chat;
    public String time;

    public Contact(String name, int imageID, ArrayList<Message> chat, String time) {
        this.name = name;
        this.imageID = imageID;
        this.chat = chat;
        this.time = time;
    }

}
