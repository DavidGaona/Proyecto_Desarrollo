package controller;

import connection.DbManager;
import model.User;

public class DaoUser {
    // Cambiar al usuario correspondiente
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public void saveNewUser(String nombre_usuario, String apellidos_usuario, String documento_id_usuario, short tipo_usuario, Boolean estado_usuario, String pass_usuario){
        User user = new User(nombre_usuario,apellidos_usuario,documento_id_usuario,tipo_usuario,estado_usuario,pass_usuario);
        if(!user.isBlank()){
            dbManager.abrirConexionBD();
            int status = dbManager.saveNewUser(user);
            dbManager.cerrarConexionBD();
        }
    }

    /* ToDo */
}
