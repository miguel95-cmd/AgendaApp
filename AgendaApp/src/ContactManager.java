// ContactManager.java
package agenda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactManager {
    private final List<Contact> contacts;

    public ContactManager() {
        this.contacts = new ArrayList<>();
    }

    public void addContact(Contact c) {
        contacts.add(c);
    }

    public void removeContact(Contact c) {
        contacts.remove(c);
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    public List<Contact> searchByName(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        return contacts.stream()
                .filter(c -> c.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public void clearAll() {
        contacts.clear();
    }
}
