package model;

import java.util.Date;

public class Bill {
    private double bill_cost;
    private Date bill_date;
    private int bill_mins;
    private int bill_gb;
    private int bill_msg;
    private long phone;
    private Client client;
    private Plan plan;

    public Bill(double bill_cost, Date bill_date, int bill_mins, int bill_gb, int bill_msg, long phone, Client client, Plan plan) {
        this.bill_cost = bill_cost;
        this.bill_date = bill_date;
        this.bill_mins = bill_mins;
        this.bill_gb = bill_gb;
        this.bill_msg = bill_msg;
        this.phone = phone;
        this.client = client;
        this.plan = plan;
    }

    public double getBill_cost() {
        return bill_cost;
    }

    public Date getBill_date() {
        return bill_date;
    }

    public int getBill_mins() {
        return bill_mins;
    }

    public int getBill_gb() {
        return bill_gb;
    }

    public int getBill_msg() {
        return bill_msg;
    }

    public long getBill_Phone() {
        return phone;
    }

    public Client getBill_Client() {
        return client;
    }

    public Plan getBillPlan() {
        return plan;
    }

}
