package controller;

import connection.DbManager;
import model.Bill;
import utilities.GeneratorPDF;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DaoBill {
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");
    private boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public String generateBills() {
        int response;
        dbManager.openDBConnection();
        response = dbManager.generateBills();
        dbManager.closeDBConnection();
        if (response > 0)
            return "Operación realizada con exíto, facturas generadas " + response;

        switch (response) {
            case 0:
                return "Las facturas ya fueron generadas";
            case -1:
                return "Existe un error en la base de datos";
            default:
                return "Error interno del sistema";
        }
    }

    public String getAllBills(String absolutePath) {
        System.out.println(absolutePath);
        ArrayList<Bill> bills;
        GeneratorPDF pdf = new GeneratorPDF();
        dbManager.openDBConnection();
        bills = dbManager.getAllBills();
        dbManager.closeDBConnection();
        if (bills.isEmpty()) {
            return "Error al momento de obtener los PDF";
        }
        int iterator = 1;

        try {
            for (Bill bill : bills) {
                if (isWindows)
                    pdf.createPDF(absolutePath + "\\factura_numero_" + iterator + ".pdf", bill);
                else
                    pdf.createPDF(absolutePath + "/factura_numero_" + iterator + ".pdf", bill);
                iterator++;
            }
        } catch (FileNotFoundException ex) {
            return "El lugar donde desea guardar no existe o no tiene permisos de escritura";
        }
        return "Se han creado los PDF con exito";
    }
}
