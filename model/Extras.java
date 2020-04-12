package model;


import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class Extras {
    private int id;
    private String planName;
    private int quantity;
    private Label selectPerson;
    private boolean isUsed;
    private int type; //type 0 = minutos, type 1 megas

    public Extras(){
        planName = "";
        quantity = 0;
        selectPerson = new Label();
        isUsed = false;
        type = -1;
    }

    public Extras(int id, String extraName, int quantity, boolean isUsed, int type){
        this.planName = extraName;
        this.quantity = quantity;
        this.isUsed = isUsed;
        if (isUsed)
            selectPerson = new Label("Quitar");
        else
            selectPerson = new Label("Agregar");
        selectPerson.setAlignment(Pos.CENTER);
        this.type = type;
        this.id = id;
    }

    public Extras(String extraName, int quantity, boolean isUsed, int type){
        this.planName = extraName;
        this.quantity = quantity;
        this.isUsed = isUsed;
        if (isUsed)
            selectPerson = new Label("Quitar");
        else
            selectPerson = new Label("Agregar");
        selectPerson.setAlignment(Pos.CENTER);
        this.type = type;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Label getSelectPerson() {
        return selectPerson;
    }

    public void setSelectPerson(Label selectPerson) {
        this.selectPerson = selectPerson;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
