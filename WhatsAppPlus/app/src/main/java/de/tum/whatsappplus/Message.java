package de.tum.whatsappplus;

public class Message {
    public String author;
    public String text;
    public String timeStamp;

    public Message(String author, String text, String timeStamp) {
        this.author = author;
        this.text = text;
        this.timeStamp = timeStamp;
    }
}
