package model;

public class Plan {

    private String planName;
    private double planCost;
    private int planMinutes;
    private int planData;
    private int planTextMsn;

    public Plan(String planName, double planCost, int planMinutes, int planData, int planTextMsn) {
        this.planName = planName;
        this.planCost = planCost;
        this.planMinutes = planMinutes;
        this.planData = planData;
        this.planTextMsn = planTextMsn;
    }


    public boolean isNotBlank() {
        if (planName == null || planMinutes >=  0 || planData >= 0 || planTextMsn >= 0)
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
}
