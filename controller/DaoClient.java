package controller;

import connection.DbManager;
import model.Client;
import utilities.ProjectUtilities;

public class DaoClient {


    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewClient(int clientId, String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(clientId, name, lastName, documentType, documentNumber, email, direction, type);
        String response = "";
        if (client.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewClient(client);
            dbManager.closeDBConnection();
        }
        return response;
    }

    public String editClient(int clientId, String name, String lastName, short documentType, String documentNumber, String email, String direction, short type) {
        Client client = new Client(clientId, name, lastName, documentType, documentNumber, email, direction, type);
        String response = "";
        if (client.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.editClient(client);
            dbManager.closeDBConnection();
        }
        return response;
    }

    public Client loadClient(String documentNumber, String clientDocumentType) {
        dbManager.openDBConnection();
        Client client = dbManager.loadClient(documentNumber, clientDocumentType.equals("NIT") ? (short) 4 :
                ProjectUtilities.convertDocumentType(clientDocumentType));
        dbManager.closeDBConnection();
        return client;
    }
}
