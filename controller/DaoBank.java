package controller;

import connection.DbManager;
import model.Bank;

public class DaoBank {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");
    public DaoBank() {
    }

    public String saveNewBank(int bank_id, String bank_name, String account_number){
        dbManager.openDBConnection();
        String response = dbManager.save_bank(bank_id, bank_name, account_number);
        dbManager.closeDBConnection();
        return  response;
    }
    public String editBank(boolean state, int bankNIT){
        dbManager.openDBConnection();
        String response = dbManager.set_state_bank(state, bankNIT);
        dbManager.closeDBConnection();
        return  response;
    }

    public Bank loadBank(int bankNIT) {
        dbManager.openDBConnection();
        Bank bank = dbManager.loadBank(bankNIT);
        dbManager.closeDBConnection();
        return bank;
    }
}
