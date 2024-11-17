import java.util.ArrayList;
import java.util.List;

public class Contact {
    private final List<String> contacts = new ArrayList<>();

    public Contact() {
        contacts.add("Saad");
        contacts.add("Danish");
        contacts.add("Ahmad");
        contacts.add("Ali");
        contacts.add("Ayesha");
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void display() {
        System.out.println("Contacts:");
        for (String contact : contacts) {
            System.out.println(contact);
        }
    }

    public boolean isContactExists(String name) {
        for (String contact : contacts) {
            if (contact.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void displayContacts() {
        System.out.println("Contacts:");
    }
}