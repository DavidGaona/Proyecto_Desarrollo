package controller;

import connection.DbManager;
import model.Plan;
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
}
