package model;

import javafx.collections.ObservableList;

public class Plan {

    private int id;
    private String planName;
    private double planCost;
    private int planMinutes;
    private int planData;
    private int planTextMsn;
    private boolean planIsGeneric;

    public Plan(){ }

    public Plan(String planName, double planCost, int planMinutes, int planData, int planTextMsn) {
        this.planName = planName;
        this.planCost = planCost;
        this.planMinutes = planMinutes;
        this.planData = planData;
        this.planTextMsn = planTextMsn;
    }

    public Plan(int id, String planName, double planCost, int planMinutes, int planData, int planTextMsn) {
        this.id = id;
        this.planName = planName;
        this.planCost = planCost;
        this.planMinutes = planMinutes;
        this.planData = planData;
        this.planTextMsn = planTextMsn;
    }


    public boolean isNotBlank() {
        if (planName == null || planCost < 0 || planMinutes <  0 || planData < 0 || planTextMsn < 0)
            return false;
        else
            return !planName.isBlank();
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getPlanCost() {
        return planCost;
    }

    public void setPlanCost(double planCost) {
        this.planCost = planCost;
    }

    public int getPlanMinutes() {
        return planMinutes;
    }

    public void setPlanMinutes(int planMinutes) {
        this.planMinutes = planMinutes;
    }

    public int getPlanData() {
        return planData;
    }

    public void setPlanData(int planData) {
        this.planData = planData;
    }

    public int getPlanTextMsn() {
        return planTextMsn;
    }

    public void setPlanTextMsn(int planTextMsn) {
        this.planTextMsn = planTextMsn;
    }

    public boolean isPlanIsGeneric() {
        return planIsGeneric;
    }

    public void setPlanIsGeneric(boolean planIsGeneric) {
        this.planIsGeneric = planIsGeneric;
    }

    public int getId() {
        return id;
    }
}
