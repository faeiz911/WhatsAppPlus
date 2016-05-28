package de.tum.whatsappplus;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

    public static final ArrayList<Contact> contacts;
    public static final String EXTRA_CHAT_ID = "chat_id";

    static {
        contacts = new ArrayList<>();
        ArrayList<Message> messages;
        messages = new ArrayList<>(Arrays.asList(new Message("Michael", "Lust auf Grillen am Wochenende?", "19:25"),
                new Message("self", "Klar, wann gehts los?", "19:29"), new Message("Michael", "Am Samstag um 18 Uhr", "19:31"),
                new Message("self", "Soll ich noch irgendwas mitbringen?", "19:32"), new Message("Michael", "Nein ich hab schon alles eingekauft", "19:34")));
        contacts.add(new Contact("Michael", R.drawable.whatsappplus_person_michael, messages));
        messages = new ArrayList<>(Arrays.asList(new Message("self", "Hast du Zeit heute Abend?", "15:11"), new Message("self", "Wir wollen in eine Bar gehen", "15:11"),
                new Message("Maxi", "Tut mir leid aber ich habe schon was vor", "15:13"), new Message("self", "Ok, schade vielleicht nächstes mal", "15:14")));
        contacts.add(new Contact("Maxi", R.drawable.whatsappplus_person_maxi, messages));
        messages = new ArrayList<>(Arrays.asList(new Message("Maria", "Jaja das machen wir schon vorher", "17:48"),
                new Message("self", "Hopfen wirs", "17:51"), new Message("Maria", "Ich traube die checkt das", "17:52"),
                new Message("self", "Gären wir einfach mal davon aus", "17:53")));
        contacts.add(new Contact("Maria", R.drawable.whatsappplus_person_maria, messages));
        messages = new ArrayList<>(Arrays.asList(new Message("Mina", "Hey", "18:12"), new Message("self", "Hey", "18:13"),
                new Message("Mina", "Treibst so?", "18:15"), new Message("self", "Nix und du?", "18:18"),
                new Message("Mina", "Hab mich Grade in die Pfanne gelegt", "18:19"),
                new Message("self", "Ja dann leg dich mal in die Pfanne aber alle 2 Minuten wenden", "18:20"),
                new Message("Mina", "Hä Wanne Ich meinte Wanne!!", "18:22")));
        contacts.add(new Contact("Mina", R.drawable.whatsappplus_person_mina, messages));
        messages = new ArrayList<>(Arrays.asList(new Message("Markus", "Wie weit bist du mit dem BSB Navigator?", "22:10"),
                new Message("self", "ich arbeite die ganze Zeit daran", "22:13"),
                new Message("Markus", "Das Layout der Popups sieht ja jetzt noch nicht so gut aus...", "22:15"),
                new Message("self", "ich schaus mir nochmal an", "22:19")));
        contacts.add(new Contact("Markus", R.drawable.whatsappplus_person_markus, messages));
        contacts.add(new Contact("Magda", R.drawable.whatsappplus_person_magda, null));
        contacts.add(new Contact("Manuel", R.drawable.whatsappplus_person_manuel, null));
        contacts.add(new Contact("Monika", R.drawable.whatsappplus_person_monika, null));
        contacts.add(new Contact("Miriam", R.drawable.whatsappplus_person_miriam, null));
        contacts.add(new Contact("Moritz", R.drawable.whatsappplus_person_moritz, null));
    }
}
