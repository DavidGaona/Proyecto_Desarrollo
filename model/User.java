package model;

public class User {

    private int id;
    private String name;
    private String lastName;
    private String DocumentIdNumber;
    private short documentType;
    private short Type;
    private Boolean State;
    private boolean passwordReset;

    public boolean isNotBlank() {
        if (name == null || lastName == null || DocumentIdNumber == null)
            return false;
        else
            return !name.isBlank() && !lastName.isBlank() && !DocumentIdNumber.isBlank();
    }

    public User(){

    }

    public User(int id, String name, String lastName, String documentIdNumber, short documentType, short type, Boolean state, boolean passwordReset) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        DocumentIdNumber = documentIdNumber;
        this.documentType = documentType;
        Type = type;
        State = state;
        this.passwordReset = passwordReset;
    }

    public int getId(){
        return id;
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

    public short getDocumentType() {
        return documentType;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }
}
