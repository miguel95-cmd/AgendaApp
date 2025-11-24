// Contact.java
package agenda;

public abstract class Contact {
    protected String name;
    protected String phone;
    protected String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Método abstracto: cada tipo de contacto lo implementa (polimorfismo)
    public abstract String getDisplayInfo();

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}
