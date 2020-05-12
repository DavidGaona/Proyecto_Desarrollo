package controller;

import connection.DbManager;
import model.Client;
import utilities.ProjectUtilities;

import java.util.ArrayList;

public class DaoClient {


    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewClient(int clientId, String name, String lastName, short documentType, String documentNumber, String email, String direction, short type, int currentLoginUser) {
        Client client = new Client(clientId, name, lastName, documentType, documentNumber, email, direction, type);
        String response = "";
        if (client.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewClient(client, currentLoginUser);
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

    public ArrayList<Long> loadPhoneNumbers(int clientId) {
        dbManager.openDBConnection();
        ArrayList<Long> numbers = dbManager.loadPhoneNumbers(clientId);
        dbManager.closeDBConnection();
        return numbers;
    }

    public String addNewClientLine(int clientId, String planName) {
        dbManager.openDBConnection();
        String response = dbManager.addNewClientLine(clientId, planName);
        dbManager.closeDBConnection();
        return response;
    }

    public String getPhonePlan(long phoneNumber) {
        dbManager.openDBConnection();
        String response = dbManager.getPhonePlan(phoneNumber);
        dbManager.closeDBConnection();
        return response;
    }

    public String queueNewPlan(int clientId, long phoneNumber, String planName) {
        dbManager.openDBConnection();
        String response = dbManager.queueNewPlan(clientId, phoneNumber, planName);
        dbManager.closeDBConnection();
        return response;
    }

    public String payPlan(long phoneNumber, int userId, String bankName) {
        dbManager.openDBConnection();
        String response = dbManager.payPlan(phoneNumber, userId, bankName);
        dbManager.closeDBConnection();
        return response;
    }

    public double getValueToPay(long clientId) {
        dbManager.openDBConnection();
        double response = dbManager.getValueToPay(clientId);
        dbManager.closeDBConnection();
        return response;
    }

    public String checkForBills(long phoneNumber) {
        dbManager.openDBConnection();
        String response = dbManager.checkForBills(phoneNumber);
        dbManager.closeDBConnection();
        return response;
    }

    public String cancelLine(long phoneNumber) {
        dbManager.openDBConnection();
        String response = dbManager.cancelLine(phoneNumber);
        dbManager.closeDBConnection();
        return response;
    }

    public String cancelLineDebt(long phoneNumber) {
        dbManager.openDBConnection();
        String response = dbManager.cancelLineDebt(phoneNumber, 1);
        dbManager.closeDBConnection();
        return response;
    }

    public String cancelLineTransferCost(long phoneNumber, int clientId) {
        dbManager.openDBConnection();
        String response = dbManager.cancelLineTransferCost(phoneNumber, clientId);
        dbManager.closeDBConnection();
        return response;
    }

    public boolean hasDebt(int clientId) {
        dbManager.openDBConnection();
        boolean response = dbManager.hasDebt(clientId);
        dbManager.closeDBConnection();
        return response;
    }

    public double getDept(int clientId) {
        dbManager.openDBConnection();
        double response = dbManager.getDept(clientId);
        dbManager.closeDBConnection();
        return response;
    }

    public String payDebt(int clientId, String bankName, int userId) {
        dbManager.openDBConnection();
        String response = dbManager.payDebt(clientId, bankName, userId);
        dbManager.closeDBConnection();
        return response;
    }
}
