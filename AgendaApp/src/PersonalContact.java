// PersonalContact.java
package agenda;

public class PersonalContact extends Contact {
    private String birthday; // formato simple: "dd/MM/yyyy"
    private String relation; // ej. "Amigo", "Familiar"

    public PersonalContact(String name, String phone, String email, String birthday, String relation) {
        super(name, phone, email);
        this.birthday = birthday;
        this.relation = relation;
    }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }

    @Override
    public String getDisplayInfo() {
        return String.format("[Personal] %s - %s - %s (%s)", name, phone, email, relation);
    }
}
