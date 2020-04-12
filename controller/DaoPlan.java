package controller;

import connection.DbManager;
import javafx.collections.ObservableList;
import model.App;
import model.Plan;
import model.Extras;
import model.Voice;

public class DaoPlan {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewPlan(String planName, String planCost, String planMinutes, String planData, String planTextMsn, ObservableList<Extras> extras) {
        Plan plan = new Plan(planName, Double.parseDouble(planCost),
                Integer.parseInt(planMinutes), Integer.parseInt(planData), Integer.parseInt(planTextMsn));
        String response = "";
        if (plan.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewPlan(plan, extras);
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

    public ObservableList<Extras> listExtras() {
        dbManager.openDBConnection();
        ObservableList<Extras> extras = dbManager.listExtras();
        dbManager.closeDBConnection();

        return extras;
    }

}
