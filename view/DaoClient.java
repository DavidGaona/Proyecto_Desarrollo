package view;

import connection.DbManager;
import model.Client;

public class DaoClient {


    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public DaoClient(){

    }

    public void saveNewClient(){
        dbManager.abrirConexionBD();
        dbManager.saveNewClient(new Client());
        dbManager.cerrarConexionBD();
    }
}
