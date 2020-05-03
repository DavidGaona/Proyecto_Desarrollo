package controller;

import connection.DbManager;
import model.DataChart;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DaoChart {
    private DbManager dbManager = new DbManager("postgres", "postgres", "MobilePlan", "localhost");

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

}
