package controller;

import connection.DbManager;
import model.Bank;

public class DaoBank {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");
    public DaoBank() {
    }

    public String saveNewBank(String bank_name, String account_number, String bankNIT){
        dbManager.openDBConnection();
        String response = dbManager.saveBank(bank_name, account_number, bankNIT);
        dbManager.closeDBConnection();
        return  response;
    }
    public String editBank(boolean state, String bankNIT){
        dbManager.openDBConnection();
        String response = dbManager.set_state_bank(state, bankNIT);
        dbManager.closeDBConnection();
        return  response;
    }

    public Bank loadBank(String bankNIT) {
        dbManager.openDBConnection();
        Bank bank = dbManager.loadBank(bankNIT);
        dbManager.closeDBConnection();
        return bank;
    }
}
