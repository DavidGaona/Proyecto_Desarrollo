package view;

import connection.DbManager;
import model.Client;

public class DaoClient {

    private DbManager dbManager = new DbManager("postgres", "postgres", "MobilePlan", "localhost");

    public String saveNewClient(String name, String lastName, short documentType, String documentNumber, String email, String direction, short type){
        String message = "Error client not saved";
        Client client = new Client(name, lastName, documentType, documentNumber, email, direction, type);
        if (!client.isBlank()){
            dbManager.abrirConexionBD();
            int status = dbManager.saveNewClient(client);
            dbManager.cerrarConexionBD();
            if (status > 0){
                message = "Client saved successfully";
            }
        }

        return message;
    }

    public void editClient(String name, String lastName, short documentType, String documentNumber, String email, String direction, short type){
        Client client = new Client(name, lastName, documentType, documentNumber, email, direction, type);
        if (!client.isBlank()){
            dbManager.abrirConexionBD();
            dbManager.editClient(client);
            dbManager.cerrarConexionBD();
        }
    }

    public Client loadClient(String documentNumber){
        dbManager.abrirConexionBD();
        Client client = dbManager.loadClient(documentNumber);
        dbManager.cerrarConexionBD();
        return client;
    }

}
