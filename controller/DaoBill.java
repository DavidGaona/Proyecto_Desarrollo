package controller;

import connection.DbManager;

public class DaoBill {
    private DbManager dbManager = new DbManager("postgres", "postgres", "MobilePlan", "localhost");

    public String generateBills(int idUser){
        int response;
        dbManager.openDBConnection();
        response = dbManager.generateBills(idUser);
        dbManager.closeDBConnection();
        if(response>0)
            return "Operación realizada con exíto, facturas generadas "+response;

        switch (response){
            case  0: return "Las facturas ya fueron generadas";
            case -1: return "Existe un error en la base de datos";
            default: return "Error interno del sistema";
        }
    }
}
