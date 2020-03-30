package controller;

import connection.DbManager;
import model.User;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class DaoBank {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");
    public DaoBank() {
    }

    public String save_bank(String bank_name, String account_number){
        dbManager.openDBConnection();
        String response = dbManager.save_bank(bank_name, account_number);
        dbManager.closeDBConnection();
        return  response;
    }
    public String set_state_bank(boolean state, int bank_id){
        dbManager.openDBConnection();
        String response = dbManager.set_state_bank(state, bank_id);
        dbManager.closeDBConnection();
        return  response;
    }
}
