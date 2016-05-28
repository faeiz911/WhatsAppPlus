package de.tum.whatsappplus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Constants {

    public static final ArrayList<Contact> contacts;

    static {
        contacts = new ArrayList<>();
        ArrayList<Message> messages;
        messages = new ArrayList<>(Arrays.asList(new Message("Michael", "Lust auf Grillen am Wochenende?"),
                new Message("self", "Klar, wann gehts los?"), new Message("Michael", "Am Samstag um 18 Uhr"),
                new Message("self", "Soll ich noch irgendwas mitbringen?"), new Message("Michael", "Nein ich hab schon alles eingekauft")));
        contacts.add(new Contact("Michael", 0, messages, "19:34"));
        messages = new ArrayList<>(Arrays.asList(new Message("self", "Hast du Zeit heute Abend?"), new Message("self", "Wir wollen in eine Bar gehen"),
                new Message("Maxi", "Tut mir leid aber ich habe schon was vor"), new Message("self", "Ok, schade vielleicht nächstes mal")));
        contacts.add(new Contact("Maxi", 1, messages, "15:14"));
        messages = new ArrayList<>(Arrays.asList(new Message("self", "Hallo"), new Message("Maria", "")));
        contacts.add(new Contact("Maria", 2, messages, "17:53"));
        messages = new ArrayList<>(Arrays.asList(new Message("", "")));
        contacts.add(new Contact("Mina", 3, messages, "12:22"));
        messages = new ArrayList<>(Arrays.asList(new Message("", "")));
        contacts.add(new Contact("Markus", 4, messages, "22:19"));
        contacts.add(new Contact("Magda", 5, null, null));
        contacts.add(new Contact("Manuel", 6, null, null));
        contacts.add(new Contact("Monika", 7, null, null));
        contacts.add(new Contact("Miriam", 8, null, null));
        contacts.add(new Contact("Moritz", 9, null, null));
    }
}
