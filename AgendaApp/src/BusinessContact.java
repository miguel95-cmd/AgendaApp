// BusinessContact.java
package agenda;

public class BusinessContact extends Contact {
    private String company;
    private String jobTitle;

    public BusinessContact(String name, String phone, String email, String company, String jobTitle) {
        super(name, phone, email);
        this.company = company;
        this.jobTitle = jobTitle;
    }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    @Override
    public String getDisplayInfo() {
        return String.format("[Business] %s - %s - %s @%s (%s)", name, phone, email, company, jobTitle);
    }
}
