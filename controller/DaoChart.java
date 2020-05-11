package controller;

import connection.DbManager;
import model.DataChart;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DaoChart {
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public ArrayList<DataChart> getDataAboutClientsNC(boolean activos){
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataAboutClientsNC(activos);
        dbManager.closeDBConnection();
        if(data.isEmpty()){
            return null;
        }
        return data;
    }

    public ArrayList<DataChart> getDataPlansPerMonths(LocalDate from, LocalDate to){
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataPlansPerMonths(Timestamp.valueOf(from.atStartOfDay()),Timestamp.valueOf(to.atStartOfDay()));
        dbManager.closeDBConnection();
        if(data.isEmpty()){
            return null;
        }

        return data;
    }

    public ArrayList<DataChart> getDataPlansOnRange(LocalDate from, LocalDate to){
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataPlansOnRange(Timestamp.valueOf(from.atStartOfDay()),Timestamp.valueOf(to.atStartOfDay()));
        dbManager.closeDBConnection();
        if(data.isEmpty()){
            return null;
        }

        return data;
    }

    public ArrayList<DataChart> getOldestClients(int numberOfClients){
        if(numberOfClients>2 && numberOfClients<20) return null;
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getOldestClients(numberOfClients);
        dbManager.closeDBConnection();
        if(data.isEmpty()){
            return null;
        }

        return data;
    }

    public ArrayList<DataChart> getHighestPayers(int numberOfClients){
        if(numberOfClients>1 && numberOfClients<10) return null;
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getHighestPayers(numberOfClients);
        dbManager.closeDBConnection();
        if(data.isEmpty()){
            return null;
        }

        return data;
    }

}
