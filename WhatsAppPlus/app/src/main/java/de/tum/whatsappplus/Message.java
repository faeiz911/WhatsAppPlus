package de.tum.whatsappplus;

import java.util.Random;

public class Message {

    public String id;
    public String author;
    public String text;
    public String timeStamp;

    private static Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    public Message(String author, String text, String timeStamp) {
        byte[] buff = new byte[32];
        random.nextBytes(buff);
        this.id = new String(buff);
        this.author = author;
        this.text = text;
        this.timeStamp = timeStamp;
    }
}
