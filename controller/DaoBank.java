package controller;

import connection.DbManager;
import model.User;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class DaoBank {
    private DbManager dbManager = new DbManager("postgres", "cristiano1994", "mobile_plan", "localhost");

    public DaoBank() {
    }

    public String save_bank(String bank_name, String account_number){
        dbManager.openDBConnection();
        String response = dbManager.save_bank(bank_name, account_number);
        dbManager.closeDBConnection();
        switch (response){
            case "Operación Realizada":
                AlertBox.display("Operación exitosa", "Banco creado con éxito", "");
                break;
            default:
                AlertBox.display("Error", response, "");
                break;
        }
        return  "";
    }
    public String set_state_bank(boolean state, int bank_id){
        dbManager.openDBConnection();
        String response = dbManager.set_state_bank(state, bank_id);
        dbManager.closeDBConnection();
        switch (response){
            case "Operación Realizada":
                AlertBox.display("Operación exitosa", "Se cambió el estado del banco", "");
                break;
            default:
                AlertBox.display("Error", response, "");
                break;
        }
        return  "";
    }
}
