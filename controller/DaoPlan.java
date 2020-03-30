package controller;

import connection.DbManager;
import model.Plan;
import model.Voice;
import utilities.ProjectUtilities;

public class DaoPlan {

    private DbManager dbManager = new DbManager("postgres", "postgres452", "MobilePlan", "localhost");


    public String saveNewPlan(String planName, double planCost, int planMinutes, int planData, int planTextMsn){
        Plan plan = new Plan(planName,planCost,planMinutes, planData,planTextMsn);
        String response="";
        if (plan.isNotBlank()) {
            dbManager.openDBConnection();
            response = dbManager.saveNewPlan(plan);
            dbManager.closeDBConnection();
        }
        return response;
    }

    public String saveNewVoiceMins(String voiceName, int voiceMinutes, boolean unlimited){
        Voice voice;
        if(unlimited){
            voice = new Voice(voiceName, Integer.MAX_VALUE);
        }
        else{
            voice = new Voice(voiceName,voiceMinutes);
        }
        String succes = "";
        if(voice.isNotBlank()){
            dbManager.openDBConnection();
            succes = dbManager.saveNewVoiceMins(voice);
            dbManager.closeDBConnection();
        }
        return succes;
    }
}
