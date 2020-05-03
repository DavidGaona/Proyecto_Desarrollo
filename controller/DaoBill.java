package controller;

import connection.DbManager;
import model.Bill;
import utilities.GeneratorPDF;

import java.util.ArrayList;

public class DaoBill {
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public String generateBills(){
        int response;
        dbManager.openDBConnection();
        response = dbManager.generateBills();
        dbManager.closeDBConnection();
        if(response>0)
            return "Operación realizada con exíto, facturas generadas "+response;

        switch (response){
            case  0: return "Las facturas ya fueron generadas";
            case -1: return "Existe un error en la base de datos";
            default: return "Error interno del sistema";
        }
    }

    public String getAllBills(String absolutePath){
        ArrayList<Bill> bills;
        GeneratorPDF pdf = new GeneratorPDF();
        dbManager.openDBConnection();
        bills = dbManager.getAllBills();
        dbManager.closeDBConnection();
        if(bills.isEmpty()){
            return "Error al momento de obtener los PDF";
        }
        int iterator = 0;
        for (Bill bill:
             bills) {
            pdf.createPDF(absolutePath+"factura_numero_"+iterator,bill);
            iterator++;
        }
        return "Se han creado los PDF con exito";
    }
}
