package de.tum.whatsappplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final Map<String, Contact> contacts;

    public static final int MESSAGE_MARGIN_TOP = 5;
    public static final long HELP_DISPLAY_TIMEOUT = 5400000;    // 90 min

    public static final String PREFERENCES_GENERAL = "general";
    public static final String PREFERENCE_FIRST_START = "firststart";
    public static final String PREFERENCE_LAST_START = "laststart";

    public static final String AUTHOR_SELF = "author_self";
    public static final String AUTHOR_SYSTEM = "author_system";

    public static final String EXTRA_CHAT_ID = "chat_id";
    public static final String EXTRA_CONTACTS_ID = "contacts";
    public static final String EXTRA_CHAT_TYPE = "chat_type";
    public static final String EXTRA_GROUP_TITLE = "group_title";
    public static final String EXTRA_GROUP_ICON = "group_icon";
    public static final String EXTRA_PRE_SELECTED_CONTACTS = "pre_selected_contacts";
    public static final String EXTRA_PRE_SELECTED_MESSAGES = "pre_selected_messages";

    public static final String RESULT_SELECTED_CONTACTS = "de.tum.whatsappplus.SelectedContacts";

    public static final String CHAT_TYPE_GROUP = "group";
    public static final String TAG_CLICK_COUNTER = "ClickCounter";

    static {
        contacts = new HashMap<>();
        ArrayList<Message> messages;
        messages = new ArrayList<>(Arrays.asList(new Message("Michael", "Lust auf Grillen am Wochenende?", "19:25"),
                new Message(Constants.AUTHOR_SELF, "Klar, wann gehts los?", "19:29"), new Message("Michael", "Am Samstag um 18 Uhr", "19:31"),
                new Message(Constants.AUTHOR_SELF, "Soll ich noch irgendwas mitbringen?", "19:32"), new Message("Michael", "Nein ich hab schon alles eingekauft", "19:34")));
        contacts.put("Michael" , new Contact("Michael", R.drawable.whatsappplus_person_michael, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message(Constants.AUTHOR_SELF, "Hast du Zeit heute Abend?", "15:11"), new Message(Constants.AUTHOR_SELF, "Wir wollen in eine Bar gehen", "15:11"),
                new Message("Maxi", "Tut mir leid aber ich habe schon was vor", "15:13"), new Message(Constants.AUTHOR_SELF, "Ok, schade vielleicht nächstes mal", "15:14")));
        contacts.put("Maxi", new Contact("Maxi", R.drawable.whatsappplus_person_maxi, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Maria", "Jaja das machen wir schon vorher", "17:48"),
                new Message(Constants.AUTHOR_SELF, "Hopfen wirs", "17:51"), new Message("Maria", "Ich traube die checkt das", "17:52"),
                new Message(Constants.AUTHOR_SELF, "Gären wir einfach mal davon aus", "17:53")));
        contacts.put("Maria", new Contact("Maria", R.drawable.whatsappplus_person_maria, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Mina", "Hey", "18:12"), new Message(Constants.AUTHOR_SELF, "Hey", "18:13"),
                new Message("Mina", "Treibst so?", "18:15"), new Message(Constants.AUTHOR_SELF, "Nix und du?", "18:18"),
                new Message("Mina", "Hab mich Grade in die Pfanne gelegt", "18:19"),
                new Message(Constants.AUTHOR_SELF, "Ja dann leg dich mal in die Pfanne aber alle 2 Minuten wenden", "18:20"),
                new Message("Mina", "Hä Wanne Ich meinte Wanne!!", "18:22")));
        contacts.put("Mina", new Contact("Mina", R.drawable.whatsappplus_person_mina, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Markus", "Wie weit bist du mit dem BSB Navigator?", "22:10"),
                new Message(Constants.AUTHOR_SELF, "ich arbeite die ganze Zeit daran", "22:13"),
                new Message("Markus", "Das Layout der Popups sieht ja jetzt noch nicht so gut aus...", "22:15"),
                new Message(Constants.AUTHOR_SELF, "ich schaus mir nochmal an", "22:19")));
        contacts.put("Markus", new Contact("Markus", R.drawable.whatsappplus_person_markus, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message(Constants.AUTHOR_SELF, "Was machst du heute Abend?", "16:24"),
                new Message("Magda", "Ich weis noch nicht. Wollte eigentlich weg gehen!", "16:27"),
                new Message(Constants.AUTHOR_SELF, "Wir wollen heute Nacht noch ins Irish Pub", "16:30")));
        contacts.put("Magda", new Contact("Magda", R.drawable.whatsappplus_person_magda, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Manuel", "Schaust du das Spiel morgen?", "14:43"),
                new Message(Constants.AUTHOR_SELF, "Michael und ich wollte morgen vorher noch grillen und das Spiel im Anschluss anschauen", "14:52"),
                new Message("Manuel", "Da wäre ich dabei. Wann gehts los?", "15:01"),
                new Message(Constants.AUTHOR_SELF, "Um 18 Uhr. Ich mach noch eine Gruppe auf.", "15:03")));
        contacts.put("Manuel", new Contact("Manuel", R.drawable.whatsappplus_person_manuel, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Monika", "Game of Thrones am Montag?", "12:51"),
                new Message(Constants.AUTHOR_SELF, "Klar. Wieder um 20 Uhr?", "13:05"),
                new Message("Monika", "Jup", "13:10")));
        contacts.put("Monika", new Contact("Monika", R.drawable.whatsappplus_person_monika, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Miriam", "Ich hab die große Liebe meines Lebens gefunden!", "10:42"),
                new Message(Constants.AUTHOR_SELF, "Wer isses?", "10:42"),
                new Message("Miriam", "Steaks", "10:43"),
                new Message(Constants.AUTHOR_SELF, "Dein ernst?", "10:43")));
        contacts.put("Miriam", new Contact("Miriam", R.drawable.whatsappplus_person_miriam, messages, false));
        messages = new ArrayList<>(Arrays.asList(new Message("Moritz", "Bin gleich da. Nur noch den drecks Berg hoch -.-'", "12:08"),
                new Message("Moritz", "Warum ist hier in Snackautomat... Auf dem Bürgersteig?!?!", "12:09"),
                new Message("Moritz", "Wtf??", "12:09"),
                new Message(Constants.AUTHOR_SELF, "Der steht da schon lange :D", "12:10"),
                new Message("Moritz", "Für alle denen er Berg zu anstrenged ist? Erstmal n Engergydrink und ein Snickers?", "12:10")));
        contacts.put("Moritz", new Contact("Moritz", R.drawable.whatsappplus_person_moritz, messages, false));
    }

    public static String getCurrentTimeStamp() {
        String timeStampString = "";
        Calendar cal = Calendar.getInstance();
        int timeElement = cal.get(Calendar.HOUR_OF_DAY);
        timeStampString += timeElement < 10 ? ("0" + timeElement) : timeElement;
        timeElement =  cal.get(Calendar.MINUTE);
        timeStampString += ":" + (timeElement < 10 ? ("0" + timeElement) : timeElement);
        return timeStampString;
    }

    public static Contact getRandomContact() {
        Collection<Contact> contactsVals = contacts.values();
        Contact[] contactsArray = contactsVals.toArray(new Contact[contacts.size()]);
        return contactsArray[(int)(Math.random()*contactsArray.length)];
    }
}
