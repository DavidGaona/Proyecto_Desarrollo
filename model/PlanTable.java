package model;


import javafx.scene.control.Button;

public class PlanTable {
    private String planName;
    private int quantity;
    private Button selectPerson;
    private boolean isUsed;
    private int type;

    public PlanTable(){
        planName = "";
        quantity = 0;
        selectPerson = new Button();
        isUsed = false;
        type = -1;
    }

    public PlanTable(String planName, int quantity, boolean isUsed, int type){
        this.planName = planName;
        this.quantity = quantity;
        this.isUsed = isUsed;
        if (isUsed)
            selectPerson = new Button("Quitar");
        else
            selectPerson = new Button("Agregar");
        this.type = type;
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

    public void setQuantity(int quantity) {
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
