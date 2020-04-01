package model;


import javafx.scene.control.Button;

public class PlanTable {
    private String planName;
    private double quantity;
    private Button selectPerson;
    private boolean isUsed;

    public PlanTable(){
        planName = "";
        quantity = 0;
        selectPerson = new Button();
        isUsed = false;
    }

    public PlanTable(String planName, double quantity, boolean isUsed){
        this.planName = planName;
        this.quantity = quantity;
        this.isUsed = isUsed;
        if (isUsed)
            selectPerson = new Button("Quitar");
        else
            selectPerson = new Button("Agregar");
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Button getSelectPerson() {
        return selectPerson;
    }

    public void setSelectPerson(Button selectPerson) {
        this.selectPerson = selectPerson;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
