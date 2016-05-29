package de.tum.whatsappplus;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    public String name;
    public int imageID;
    public ArrayList<Message> chat;
    public int selectedMessagesAmount;

    public Contact(String name, int imageID, ArrayList<Message> chat) {
        this.name = name;
        this.imageID = imageID;
        this.chat = chat;
    }

    public List<Message> getSelectedMessages() {
        List<Message> selectedMessages = new ArrayList<>();
        for(Message m : chat) {
            if(m.selected) {
                selectedMessages.add(m);
            }
        }
        return selectedMessages;
    }

}
