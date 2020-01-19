package model;

public class User {

    private String userName;
    private String userLastName;
    private String userIdDocumentNumber;
    private short userType;
    private Boolean userState;
    private String userPassword;

    public boolean isBlank() {
        return userName.isBlank() || userLastName.isBlank() || userIdDocumentNumber.isBlank() || userPassword.isBlank();
    }

    public User(String userName, String userLastName, String userIdDocumentNumber, short userType, Boolean userStatus, String userPassword) {
        this.userName = userName;
        this.userLastName = userLastName;
        this.userIdDocumentNumber = userIdDocumentNumber;
        this.userType = userType;
        this.userState = userStatus;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserIdDocumentNumber() {
        return userIdDocumentNumber;
    }

    public short getUserType() {
        return userType;
    }

    public Boolean getUserState() {
        return userState;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
