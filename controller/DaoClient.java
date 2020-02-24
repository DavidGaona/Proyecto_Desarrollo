package controller;

import connection.DbManager;
import model.Client;
import utilities.ProjectUtilities;

public class DaoClient {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public void saveNewClient(String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(name, lastName, documentType, documentNumber, email, direction, type);
        if (!client.isBlank()) {
            dbManager.openDBConnection();
            int status = dbManager.saveNewClient(client);
            dbManager.closeDBConnection();
        }
    }

    public void editClient(String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(name, lastName, documentType, documentNumber, email, direction, type);
        if (!client.isBlank()) {
            dbManager.openDBConnection();
            dbManager.editClient(client);
            dbManager.closeDBConnection();
        }
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
