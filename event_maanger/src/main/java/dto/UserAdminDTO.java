package dto;

/**
 * User DTO - minimalne dane o użytkowniku dla admin panel
 * Bez zagnieżdżonych kolekcji (tickets, reviews)
 */
public class UserAdminDTO {
    private int id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private boolean admin;

    public UserAdminDTO(int id, String login, String email, String firstName, String lastName,
                       String address, String phoneNumber, boolean admin) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
    }

    // Getters
    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isAdmin() { return admin; }
}

