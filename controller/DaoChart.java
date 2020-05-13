package controller;

import connection.DbManager;
import model.DataChart;
import model.TableClient;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

public class DaoChart {
    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");

    public ArrayList<DataChart> getDataAboutClientsNC(boolean actives) {
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataAboutClientsNC(actives);
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

    public ArrayList<DataChart> getDataPlansPerMonths(LocalDate from, LocalDate to) {
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataPlansPerMonths(Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atStartOfDay()));
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

    public ArrayList<DataChart> getDataPlansOnRange(LocalDate from, LocalDate to) {
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getDataPlansOnRange(Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atStartOfDay()));
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

    public ArrayList<TableClient> getOldestClients(int numberOfClients) {
        if (!(numberOfClients > 1 && numberOfClients < 20)) return null;
        ArrayList<TableClient> data;
        dbManager.openDBConnection();
        data = dbManager.getOldestClients(numberOfClients);
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

    public ArrayList<TableClient> getHighestPayers(int numberOfClients) {
        if (!(numberOfClients > 1 && numberOfClients < 50)) return null;
        ArrayList<TableClient> data;
        dbManager.openDBConnection();
        data = dbManager.getHighestPayers(numberOfClients);
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

    public ArrayList<DataChart> getCancelledClientsOnRange(LocalDate from, LocalDate to) {
        ArrayList<DataChart> data;
        dbManager.openDBConnection();
        data = dbManager.getCancelledClientsOnRange(Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atStartOfDay()));
        dbManager.closeDBConnection();
        return (data.isEmpty()) ? null : data;
    }

}
