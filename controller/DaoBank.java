package controller;

import connection.DbManager;
import model.Bank;

public class DaoBank {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public DaoBank() {
    }

    public String saveNewBank(String bankName, String accountNumber, String bankNIT) {
        dbManager.openDBConnection();
        String response = dbManager.saveBank(bankName, accountNumber, bankNIT);
        dbManager.closeDBConnection();
        return response;
    }

    public String editBank(boolean state, String bankNIT) {
        dbManager.openDBConnection();
        String response = dbManager.setStateBank(state, bankNIT);
        dbManager.closeDBConnection();
        return response;
    }

    public Bank loadBank(String bankName) {
        dbManager.openDBConnection();
        Bank bank = dbManager.loadBank(bankName);
        dbManager.closeDBConnection();
        return bank;
    }

    public String[] loadAllBanks(boolean loadAll) {
        dbManager.openDBConnection();
        String[] banks = dbManager.loadAllBank(loadAll);
        dbManager.closeDBConnection();
        return banks;
    }
}
