package model;

public class Client {

    private int id;
    private String name;
    private String lastName;
    private short documentType;
    private String documentId;
    private String email;
    private String direction;
    private short type;

    public Client(){}

    public Client(int id, String name, String lastName, short documentType, String documentId, String email, String direction, short type) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentId = documentId;
        this.email = email;
        this.direction = direction;
        this.type = type;
    }

    public boolean isNotBlank() {
        if (name == null || lastName == null || documentId == null || email == null || direction == null)
            return false;
        else
            return !name.isBlank() && !lastName.isBlank() && !documentId.isBlank() && !email.isBlank() && !direction.isBlank();
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public short getDocumentType() {
        return documentType;
    }

    public void setDocumentType(short documentType) {
        this.documentType = documentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
