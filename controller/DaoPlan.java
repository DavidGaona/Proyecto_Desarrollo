package controller;

import connection.DbManager;
import model.Plan;
import model.Voice;
import utilities.ProjectUtilities;

public class DaoPlan {

    private DbManager dbManager = new DbManager("postgres", "cristiano1994", "mobile_plan", "localhost");

    public int saveNewPlan(String planName, double planCost, int planMinutes, int planData, int planTextMsn){
        Plan plan = new Plan(planName,planCost,planMinutes, planData,planTextMsn);
        int success = -1;
        if (plan.isNotBlank()) {
            dbManager.openDBConnection();
            success = dbManager.saveNewPlan(plan);
            dbManager.closeDBConnection();
        }
        return success;
    }

    public int saveNewVoiceMins(String voiceName, int voiceMinutes, boolean unlimited){
        Voice voice;
        if(unlimited){
            voice = new Voice(voiceName, Integer.MAX_VALUE);
        }
        else{
            voice = new Voice(voiceName,voiceMinutes);
        }
        int succes = -1;
        if(voice.isNotBlank()){
            dbManager.openDBConnection();
            succes = dbManager.saveNewVoiceMins(voice);
            dbManager.closeDBConnection();
        }
        return succes;
    }
}
