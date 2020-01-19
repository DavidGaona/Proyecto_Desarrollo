package controller;

import connection.DbManager;
import model.User;

public class DaoUser {
    // Cambiar al usuario correspondiente
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public void saveNewUser(String userName, String userLastName, String userIdDocumentNumber, short userType, Boolean userState, String userPassword) {
        User user = new User(userName, userLastName, userIdDocumentNumber, userType, userState, userPassword);
        if (!user.isBlank()) {
            dbManager.openDBConnection();
            int status = dbManager.saveNewUser(user);
            dbManager.closeDBConnection();
        }
    }

    public int loginUser(String id_usuario, String password){
        dbManager.openDBConnection();
        int role = dbManager.loginUser(id_usuario,password);
        dbManager.closeDBConnection();
        if (role==0){
            System.out.println("Iniciada sesión correctamente");
        }else{
            System.out.println("No se pudo iniciar sesión");
        }
        return role;
    }
}
