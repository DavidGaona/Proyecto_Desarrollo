package controller;

import connection.DbManager;
import model.Client;
import utilities.ProjectUtilities;

public class DaoClient {

    private DbManager dbManager = new DbManager("postgres", "cristiano1994", "mobile_plan", "localhost");

    public int saveNewClient(int clientId, String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(clientId, name, lastName, documentType, documentNumber, email, direction, type);
        int success = -1;
        if (!client.isBlank()) {
            dbManager.openDBConnection();
            success = dbManager.saveNewClient(client);
            dbManager.closeDBConnection();
        }
        return success;
    }

    public int editClient(int clientId, String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(clientId, name, lastName, documentType, documentNumber, email, direction, type);
        int success = -1;
        if (!client.isBlank()) {
            dbManager.openDBConnection();
            success = dbManager.editClient(client);
            dbManager.closeDBConnection();
        }
        return success;
    }

    public Client loadClient(String documentNumber,String clientDocumentType) {
        dbManager.openDBConnection();
        Client client = dbManager.loadClient(documentNumber, ProjectUtilities.convertDocumentType(clientDocumentType));
        dbManager.closeDBConnection();
        return client;
    }

    public void changeClientPassword(String password) {
        //ToDo
    }

}
