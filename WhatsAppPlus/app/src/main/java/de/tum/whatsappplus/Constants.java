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
        contacts.add(new Contact("Michael", R.drawable.whatsappplus_person_michael, messages, "19:34"));
        messages = new ArrayList<>(Arrays.asList(new Message("self", "Hast du Zeit heute Abend?"), new Message("self", "Wir wollen in eine Bar gehen"),
                new Message("Maxi", "Tut mir leid aber ich habe schon was vor"), new Message("self", "Ok, schade vielleicht nächstes mal")));
        contacts.add(new Contact("Maxi", R.drawable.whatsappplus_person_maxi, messages, "15:14"));
        messages = new ArrayList<>(Arrays.asList(new Message("Maria", "Jaja das machen wir schon vorher"),
                new Message("self", "Hopfen wirs"), new Message("Maria", "Ich traube die checkt das"),
                new Message("self", "Gären wir einfach mal davon aus")));
        contacts.add(new Contact("Maria", R.drawable.whatsappplus_person_maria, messages, "17:53"));
        messages = new ArrayList<>(Arrays.asList(new Message("Mina", "Hey"), new Message("self", "Hey"),
                new Message("Mina", "Treibst so?"), new Message("self", "Nix und du?"),
                new Message("Mina", "Hab mich Grade in die Pfanne gelegt"),
                new Message("self", "Ja dann leg dich mal in die Pfanne aber alle 2 Minuten wenden"),
                new Message("Mina", "Hä Wanne Ich meinte Wanne!!")));
        contacts.add(new Contact("Mina", R.drawable.whatsappplus_person_mina, messages, "12:22"));
        messages = new ArrayList<>(Arrays.asList(new Message("Markus", "Wie weit bist du mit dem BSB Navigator?"),
                new Message("self", "ich arbeite die ganze Zeit daran"),
                new Message("Markus", "Das Layout der Popups sieht ja jetzt noch nicht so gut aus..."),
                new Message("self", "ich schaus mir nochmal an")));
        contacts.add(new Contact("Markus", R.drawable.whatsappplus_person_markus, messages, "22:19"));
        contacts.add(new Contact("Magda", R.drawable.whatsappplus_person_magda, null, null));
        contacts.add(new Contact("Manuel", R.drawable.whatsappplus_person_manuel, null, null));
        contacts.add(new Contact("Monika", R.drawable.whatsappplus_person_monika, null, null));
        contacts.add(new Contact("Miriam", R.drawable.whatsappplus_person_miriam, null, null));
        contacts.add(new Contact("Moritz", R.drawable.whatsappplus_person_moritz, null, null));
    }
}
