package de.tum.whatsappplus;

import java.util.ArrayList;

public class Contact {
    public String name;
    public int imageID;
    public ArrayList<Message> chat;

    public Contact(String name, int imageID, ArrayList<Message> chat) {
        this.name = name;
        this.imageID = imageID;
        this.chat = chat;
    }

}
