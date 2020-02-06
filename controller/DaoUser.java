package controller;

import connection.DbManager;
import model.User;

public class DaoUser {
    // Cambiar al usuario correspondiente
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public void saveNewUser(String userName, String userLastName, String userIdDocumentNumber, short userDocumentType, short userType, Boolean userState) {
        User user = new User(userName, userLastName, userIdDocumentNumber, userDocumentType, userType, userState);
        if (!user.isBlank()) {
            dbManager.openDBConnection();
            int status = dbManager.saveNewUser(user);
            dbManager.closeDBConnection();
        }
    }

    public void editUser(String userName, String userLastName, String userIdDocumentNumber, short userDocumentType, short userType, Boolean userState) {
        User user = new User(userName, userLastName, userIdDocumentNumber, userDocumentType, userType, userState);
        if (!user.isBlank()) {
            dbManager.openDBConnection();
            dbManager.editUser(user);
            dbManager.closeDBConnection();
        }
    }

    public User loadUser(String documentNumber) {
        dbManager.openDBConnection();
        User user = dbManager.loadUser(documentNumber);
        dbManager.closeDBConnection();
        return user;
    }

    public int loginUser(String DocumentNumber, String password) {
        dbManager.openDBConnection();
        int role = dbManager.loginUser(DocumentNumber, password);
        dbManager.closeDBConnection();
        if (role >= 0) {
            System.out.println("Iniciada sesión correctamente");
        } else {
            System.out.println("No se pudo iniciar sesión");
        }
        return role;
    }

    public boolean checkPassword(String DocumentNumber, String password){
        dbManager.openDBConnection();
        boolean success = dbManager.checkPassword(DocumentNumber, password);
        dbManager.closeDBConnection();
        return success;
    }

    public void changePassword(String DocumentNumber, String password){
        dbManager.openDBConnection();
        dbManager.changePassword(DocumentNumber, password);
        dbManager.closeDBConnection();
    }
}
