package controller;

import connection.DbManager;
import javafx.collections.ObservableList;
import model.App;
import model.Plan;
import model.PlanTable;
import model.Voice;
import utilities.ProjectUtilities;

public class DaoPlan {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewPlan(String planName, double planCost, int planMinutes, int planData, int planTextMsn) {
        Plan plan = new Plan(planName, planCost, planMinutes, planData, planTextMsn);
        String response = "";
        if (plan.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewPlan(plan);
            dbManager.closeDBConnection();
        }
        return response;
    }

    public String saveNewVoiceMins(String voiceName, String voiceMinutes) {
        Voice voice;
        voice = new Voice(voiceName, Integer.parseInt(voiceMinutes));
        String succes = "";
        if (voice.isNotBlank()) {
            dbManager.openDBConnection();
            succes = dbManager.saveNewVoiceMins(voice);
            dbManager.closeDBConnection();
        }
        return succes;
    }

    public String saveApp(String appName, String appMb) {
        App app;
        app = new App(appName, Integer.parseInt(appMb));
        String succes = "";
        if (app.isNotBlank()) {
            dbManager.openDBConnection();
            succes = dbManager.saveApp(app);
            dbManager.closeDBConnection();
        }
        return succes;
    }

    public ObservableList<PlanTable> listExtras() {
        dbManager.openDBConnection();
        ObservableList<PlanTable> extras = dbManager.listExtras();
        dbManager.closeDBConnection();

        return extras;
    }

}
