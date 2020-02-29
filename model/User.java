package model;

public class User {

    private String name;
    private String lastName;
    private String DocumentIdNumber;
    private short documentType;
    private short Type;
    private Boolean State;
    private String password;
    private boolean passwordReset;

    public boolean isNotBlank() {
        return !name.isBlank() && !lastName.isBlank() && !DocumentIdNumber.isBlank();
    }

    public User(String name, String lastName, String documentIdNumber, short documentType, short type, Boolean state, boolean passwordReset) {
        this.name = name;
        this.lastName = lastName;
        DocumentIdNumber = documentIdNumber;
        this.documentType = documentType;
        Type = type;
        State = state;
        this.passwordReset = passwordReset;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentIdNumber() {
        return DocumentIdNumber;
    }

    public short getType() {
        return Type;
    }

    public Boolean getState() {
        return State;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getDocumentType() {
        return documentType;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }
}
