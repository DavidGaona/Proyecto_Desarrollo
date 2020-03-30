package controller;

import connection.DbManager;
import model.User;
import utilities.ProjectUtilities;

public class DaoUser {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewUser(int userId, String userName, String userLastName, String userIdDocumentNumber, short userDocumentType, short userType, Boolean userState) {
        User user = new User(userId, userName, userLastName, userIdDocumentNumber, userDocumentType, userType, userState, true);
        String response="";
        if (user.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewUser(user);
            dbManager.closeDBConnection();
        }
        return response;
    }

    public String editUser(int userId, String userName, String userLastName, String userIdDocumentNumber, short userDocumentType, short userType, Boolean userState, boolean userPasswordReset) {
        User user = new User(userId, userName, userLastName, userIdDocumentNumber, userDocumentType, userType, userState, userPasswordReset);
        String success = "";
        if (user.isNotBlank()) {
            dbManager.openDBConnection();
            success = dbManager.editUser(user);
            dbManager.closeDBConnection();
        }
        return success;
    }

    public User loadUser(String documentNumber,String userDocumentType) {
        dbManager.openDBConnection();
        User user = dbManager.loadUser(documentNumber, ProjectUtilities.convertDocumentType(userDocumentType));
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
            //ToDo
        }
        return role;
    }

    public boolean checkPassword(int id, String password){
        dbManager.openDBConnection();
        boolean success = dbManager.checkPassword(id, password);
        dbManager.closeDBConnection();
        return success;
    }

    public short changePassword(int id, String password){
        short response;
        dbManager.openDBConnection();
        response = dbManager.changePassword(id, password);
        dbManager.closeDBConnection();
        return response;
    }
}
